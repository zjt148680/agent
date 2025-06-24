from mteb import logging
from typing import Optional, Any, Union

import mteb
import weaviate
from mteb import T2Retrieval, TaskMetadata, HFSubset, ScoresDict, RetrievalEvaluator
from tqdm import tqdm
from weaviate.collections.classes.config import DataType, Property, Configure
from weaviate.collections.classes.grpc import MetadataQuery

"""
mteb
检索任务
数据集T2Retrieval
自定义检索逻辑
使用已有指标
"""


def simple_query(query: str) -> dict[str, float]:
    """
    简单向量检索，
    ref: com.speedboot.speedbotagent.weaviate.dao.impl.DocumentInfoByOverlapChunkDao#retrieve

    :param query: query
    :return: {相关文档id:1-距离,...}; 1-距离=余弦相似度
    """
    top_k = 1000
    vdb = client.collections.get(TMP_VDB_NAME)
    response = vdb.query.near_text(
        query=query,
        limit=top_k,
        target_vector="textVector",
        return_metadata=MetadataQuery(distance=True)
    )

    return {o.properties["uid"]: 1 - o.metadata.distance for o in response.objects}


def hybrid_query(query: str) -> dict[str, float]:
    """
    混合检索，向量相似度+bm25指数
    ref: com.speedboot.speedbotagent.weaviate.dao.impl.DocumentInfoByOverlapChunkDao#retrieve

    :param query: query
    :return: {相关文档id:score,...}
    """
    top_k = 1000
    vdb = client.collections.get(TMP_VDB_NAME)
    response = vdb.query.hybrid(
        query=query,
        limit=top_k,
        target_vector="textVector",
        query_properties=["text"],
        alpha=0.5,
        return_metadata=MetadataQuery(score=True)
    )

    return {o.properties["uid"]: o.metadata.score for o in response.objects}


logger = logging.getLogger(__name__)

TMP_VDB_NAME = "TempT2Retrival"


class CustomRetrievalEvaluator(RetrievalEvaluator):
    def __init__(
            self,
            retriever,
            task_name: Optional[str] = None,
            k_values: list[int] = [1, 3, 5, 10, 20, 100, 1000],
            encode_kwargs: dict[str, Any] = {},
            **kwargs,
    ):
        super().__init__(retriever, task_name, k_values, encode_kwargs, **kwargs)
        if "force_create_vdb" in kwargs:
            self.force_create_vdb = kwargs["force_create_vdb"]
        else:
            self.force_create_vdb = False

        if "search_fn" in kwargs:
            self.search_fn = kwargs["search_fn"]
        else:
            self.search_fn = simple_query

    def __call__(
            self,
            corpus: dict[str, dict[str, str]],
            queries: dict[str, Union[str, list[str]]],
    ) -> dict[str, dict[str, float]]:
        logger.info("开始保存corpus到向量库")
        self._save_corpus_to_vdb(corpus)
        return {q_id: self.search_fn(query) for q_id, query in tqdm(queries.items(), desc="query检索")}

    def _save_corpus_to_vdb(self, corpus: dict[str, dict[str, str]]):
        if client.collections.exists(TMP_VDB_NAME) and not self.force_create_vdb:
            logger.info("向量库已存在，直接运行检索")
            return

        self._create_vdb()
        vdb = client.collections.get(TMP_VDB_NAME)

        data_rows = [
            {
                "uid": id,
                "text": data["text"],
            }
            for id, data in corpus.items()
        ]
        with vdb.batch.fixed_size(batch_size=200) as batch:
            for data_row in tqdm(data_rows, desc="保存数据到向量库"):
                batch.add_object(
                    properties=data_row,
                )
                if batch.number_errors > 0:
                    logger.error("保存数据时出错")
                    raise RuntimeError("保存数据时出错")

    def _create_vdb(self):
        client.collections.delete(TMP_VDB_NAME)
        client.collections.create(
            TMP_VDB_NAME,
            properties=[
                Property(name="uid", data_type=DataType.TEXT),
                Property(name="text", data_type=DataType.TEXT),
            ],
            vectorizer_config=[
                Configure.NamedVectors.text2vec_transformers(
                    name="textVector",
                    source_properties=["text"]
                ),
            ],
        )


class CustomT2Retrieval(T2Retrieval):
    metadata = TaskMetadata(
        name="CustomT2Retrieval",
        description="CustomT2Retrieval",
        dataset={
            "path": "C-MTEB/T2Retrieval",
            "revision": "8731a845f1bf500a4f111cf1070785c793d10e64",
            "qrel_revision": "1c83b8d1544e529875e3f6930f3a1fcf749a8e97",
        },
        type="Retrieval",
        category="s2p",
        modalities=["text"],
        eval_splits=["dev"],
        eval_langs=["cmn-Hans"],
        main_score="ndcg_at_10",
        license="apache-2.0",
        annotations_creators="human-annotated",
        dialect=None,
        sample_creation=None,
        reference="https://arxiv.org/abs/2304.03679",
        bibtex_citation=r"""
    @misc{xie2023t2ranking,
      archiveprefix = {arXiv},
      author = {Xiaohui Xie and Qian Dong and Bingning Wang and Feiyang Lv and Ting Yao and Weinan Gan and Zhijing Wu and Xiangsheng Li and Haitao Li and Yiqun Liu and Jin Ma},
      eprint = {2304.03679},
      primaryclass = {cs.IR},
      title = {T2Ranking: A large-scale Chinese Benchmark for Passage Ranking},
      year = {2023},
    }
    """,
        prompt={
            "query": "Given a Chinese search query, retrieve web passages that answer the question"
        },
    )

    def evaluate(
            self,
            model,
            split: str = "test",
            subsets_to_run: Optional[list[HFSubset]] = None,
            *,
            encode_kwargs: dict[str, Any] = {},
            **kwargs,
    ) -> dict[HFSubset, ScoresDict]:
        retriever = CustomRetrievalEvaluator(
            retriever=model,
            task_name=self.metadata.name,
            encode_kwargs=encode_kwargs,
            **kwargs,
        )

        scores = {}
        hf_subsets = list(self.hf_subsets) if self.is_multilingual else ["default"]
        if subsets_to_run is not None:
            hf_subsets = [s for s in hf_subsets if s in subsets_to_run]

        for hf_subset in hf_subsets:
            logger.info(f"Subset: {hf_subset}")

            if hf_subset == "default":
                corpus, queries, relevant_docs = (
                    self.corpus[split],
                    self.queries[split],
                    self.relevant_docs[split],
                )
            else:
                corpus, queries, relevant_docs = (
                    self.corpus[hf_subset][split],
                    self.queries[hf_subset][split],
                    self.relevant_docs[hf_subset][split],
                )
            scores[hf_subset] = self._evaluate_subset(
                retriever, corpus, queries, relevant_docs, hf_subset, **kwargs
            )
        return scores


if __name__ == "__main__":
    global client
    try:
        client = weaviate.connect_to_local(
            host="127.0.0.1",
            port=22223,
            grpc_port=22224,
        )

        evaluation = mteb.MTEB(tasks=[CustomT2Retrieval()])
        for search_fn in [simple_query, hybrid_query]:
            results = evaluation.run(None,
                                     output_folder=f"eval_res/{search_fn.__name__}",
                                     force_create_vdb=False,
                                     search_fn=search_fn)
    except Exception as e:
        logger.error(e)
    finally:
        if client:
            client.close()
