from weaviate.classes.config import Property, DataType, Configure
import weaviate

client = weaviate.connect_to_local(
    host="127.0.0.1",
    port=8080,
    grpc_port=50051,
)
print(client.is_ready())

client.collections.delete("DocumentInfoByOverlapChunk")
d2 = client.collections.create(
    "DocumentInfoByOverlapChunk",
    properties=[
        Property(name="documentId", data_type=DataType.INT),
        Property(name="documentName", data_type=DataType.TEXT),
        Property(name="documentType", data_type=DataType.TEXT),
        Property(name="chunkId", data_type=DataType.INT),
        Property(name="chunkText", data_type=DataType.TEXT),
    ],
    vectorizer_config=[
        Configure.NamedVectors.text2vec_transformers(
            name="documentNameVector",
            source_properties=["documentName"]
        ),
        Configure.NamedVectors.text2vec_transformers(
            name="chunkTextVector",
            source_properties=["chunkText"]
        ),
    ],
)