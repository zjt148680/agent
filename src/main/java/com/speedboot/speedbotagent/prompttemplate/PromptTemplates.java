package com.speedboot.speedbotagent.prompttemplate;

import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;

import java.util.Map;

public class PromptTemplates {
    private static final char TEMPLATE_START_DELIMITER_TOKEN = '<';

    private static final char TEMPLATE_END_DELIMITER_TOKEN = '>';

    public static final TemplateRenderer DEFAULT_PROMPT_TEMPLATE_RENDERER = StTemplateRenderer.builder()
            .startDelimiterToken(TEMPLATE_START_DELIMITER_TOKEN)
            .endDelimiterToken(TEMPLATE_END_DELIMITER_TOKEN).build();

    public static PromptTemplate.Builder DEFAULT_PROMPT_TEMPLATE_BUILDER = PromptTemplate
            .builder()
            .renderer(DEFAULT_PROMPT_TEMPLATE_RENDERER);

    public static final Map<String, String> CHAT_PROMPT_TEMPLATE =
            Map.of(MessageType.SYSTEM.getValue(), """
                            你是一个基于检索增强生成（RAG）的问答系统，你的任务是基于用户提供的知识回答用户问题。
                            用户会连续对话，你需要对上下文对话进行记忆。


                            # 输入输出格式
                              用户输入：
                              [外部知识] 一系列外部知识
                              [问题] 用户提问内容

                              系统响应：
                              [结果] 基于外部知识对用户问题的回答


                            # 下面是一些合法的对话
                              ## 示例1
                                [外部知识]
                                [1]. 在2023年，大模型行业的应用场景可分为生成和决策两类应用场景,决策场景预期业务值更高。
                                [2]. 大模型行业的生成场景主要有对话交互，代码开发，智能体等。
                                [3]. NLP的应用场景有文本分类，机器翻译，情感分析，自动摘要等。

                                [问题]
                                2023年，大模型行业的应用场景可以分为哪几类？

                                [结果]
                                根据外部知识[1]。大模型行业的应用场景可以分为生成和决策两类应用场景。你想更深入了解某类应用场景吗？

                              ## 示例2
                                [外部知识]
                                [1]. 什么是多模态LLM？正如简介中所暗示的那样，多模态 LLM 是能够处理多种类型输入的大型语言模型，其中每个“模态”都是指一种特定类型的数据，例如文本（如传统 LLM）、声音、图像、视频等。
                                [2]. 构建多模态LLM有两种主要方法：统一嵌入解码器架构方法;跨模态注意力架构方法。
                                [3]. 统一嵌入解码器架构通常更容易实现，因为它不需要对LLM架构本身进行任何修改。
                                [4]. 跨模态注意力架构通常被认为计算效率更高，因为它不会用额外的图像标记使输入上下文过载，而是稍后将它们引入交叉注意力层。

                                [问题]
                                多模态模型是如何做到跨模态的？

                                [结果]
                                根据外部知识[1]，多模态的含义是大预言模型用于处理各种非语言类型的数据。根据知识[2]，构建多模态模型的方法之一是跨模态注意力架构，根据知识[4]，跨模态是通过引入交叉注意力层实现的。

                              ## 示例3
                                [外部知识]
                                [1]. 什么是多模态LLM？正如简介中所暗示的那样，多模态 LLM 是能够处理多种类型输入的大型语言模型，其中每个“模态”都是指一种特定类型的数据，例如文本（如传统 LLM）、声音、图像、视频等。
                                [2]. 构建多模态LLM有两种主要方法：统一嵌入解码器架构方法;跨模态注意力架构方法。
                                [3]. 统一嵌入解码器架构通常更容易实现，因为它不需要对LLM架构本身进行任何修改。
                                [4]. 跨模态注意力架构通常被认为计算效率更高，因为它不会用额外的图像标记使输入上下文过载，而是稍后将它们引入交叉注意力层。

                                [问题]
                                介绍量子力学

                                [结果]
                                参考知识中没有关于这方面的内容，无法回答这个问题。
                            """,
                    MessageType.USER.getValue(), """
                            [外部知识]
                            <knowledge>

                            [问题]
                            <query>""");

    public static final String QUERY_KNOWLEDGE_REL_EVAL_PROMPT_TEMPLATE1 = """
            ### Instructions

            You are a world class expert designed to evaluate the relevance score of a Context in order to answer the Question.
            Your task is to determine if the Context contains proper information to answer the Question.
            Do not rely on your previous knowledge about the Question.
            Use only what is written in the Context and in the Question.
            Follow the instructions below:
            0. If the context does not contains any relevant information to answer the question, say 0.
            1. If the context partially contains relevant information to answer the question, say 1.
            2. If the context contains any relevant information to answer the question, say 2.
            You must provide the relevance score of 0, 1, or 2, nothing else.
            Do not explain.
            Please return the output in a JSON format that complies with the following schema as specified in JSON Schema:
            {"properties": {"score": {"type": "integer", "enum":[0,1,2]}}, "required": ["score"], "type": "object"},
            Do not use single quotes in your response but double quotes,properly escaped with a backslash.


            ### Question

            <query>


            ### Context

            <context>


            Do not try to explain.
            Analyzing Context and Question, give the result""";

    public static final String QUERY_KNOWLEDGE_REL_EVAL_PROMPT_TEMPLATE2 = """
            ### Instructions

            As a specially designed expert to assess the relevance score of a given Context in relation to a Question, my task is to determine the extent to which the Context provides information necessary to answer the Question. I will rely solely on the information provided in the Context and Question, and not on any prior knowledge.

            Here are the instructions I will follow:
            * If the Context does not contain any relevant information to answer the Question, I will respond with a relevance score of 0.
            * If the Context partially contains relevant information to answer the Question, I will respond with a relevance score of 1.
            * If the Context contains any relevant information to answer the Question, I will respond with a relevance score of 2.
            * Please return the output in a JSON format that complies with the following schema as specified in JSON Schema:
            {"properties": {"score": {"type": "integer", "enum":[0,1,2]}}, "required": ["score"], "type": "object"},
            Do not use single quotes in your response but double quotes,properly escaped with a backslash.


            ### Question

            <query>


            ### Context

            <context>


            Do not try to explain.
            Based on the provided Question and Context, give the result""";

    public static final String CREATE_STATEMENT_PROMPT_TEMPLATE = """
            Given a question and an answer, analyze the complexity of each sentence in the answer. Break down each sentence into one or more fully understandable statements. Ensure that no pronouns are used in any statement. Format the outputs in JSON.
            Please return the output in a JSON format that complies with the following schema as specified in JSON Schema:
            {"properties": {"statements": {"description": "The generated statements", "items": {"type": "string"}, "title": "Statements", "type": "array"}}, "required": ["statements"], "title": "StatementGeneratorOutput", "type": "object"}Do not use single quotes in your response but double quotes,properly escaped with a backslash.

            --------EXAMPLES-----------
            Example 1
            Input: {
                "question": "Who was Albert Einstein and what is he best known for?",
                "answer": "He was a German-born theoretical physicist, widely acknowledged to be one of the greatest and most influential physicists of all time. He was best known for developing the theory of relativity, he also made important contributions to the development of the theory of quantum mechanics."
            }
            Output: {
                "statements": [
                    "Albert Einstein was a German-born theoretical physicist.",
                    "Albert Einstein is recognized as one of the greatest and most influential physicists of all time.",
                    "Albert Einstein was best known for developing the theory of relativity.",
                    "Albert Einstein also made important contributions to the development of the theory of quantum mechanics."
                ]
            }
            -----------------------------

            Now perform the same with the following input
            input: {
                "question": "<query>",
                "answer": "<response>"
            }
            Output:""";

    public static final String KNOWLEDGE_RESP_REL_EVAL_PROMPT_TEMPLATE = """
            Your task is to judge the faithfulness of a series of statements based on a given context. For each statement you must return verdict as 1 if the statement can be directly inferred based on the context or 0 if the statement can not be directly inferred based on the context.
            Please return the output in a JSON format that complies with the following schema as specified in JSON Schema:
            {"$defs": {"StatementFaithfulnessAnswer": {"properties": {"statement": {"description": "the original statement, word-by-word", "title": "Statement", "type": "string"}, "reason": {"description": "the reason of the verdict", "title": "Reason", "type": "string"}, "verdict": {"description": "the verdict(0/1) of the faithfulness.", "title": "Verdict", "type": "integer"}}, "required": ["statement", "reason", "verdict"], "title": "StatementFaithfulnessAnswer", "type": "object"}}, "properties": {"statements": {"items": {"$ref": "#/$defs/StatementFaithfulnessAnswer"}, "title": "Statements", "type": "array"}}, "required": ["statements"], "title": "NLIStatementOutput", "type": "object"}Do not use single quotes in your response but double quotes,properly escaped with a backslash.

            --------EXAMPLES-----------
            Example 1
            Input: {
                "context": "John is a student at XYZ University. He is pursuing a degree in Computer Science. He is enrolled in several courses this semester, including Data Structures, Algorithms, and Database Management. John is a diligent student and spends a significant amount of time studying and completing assignments. He often stays late in the library to work on his projects.",
                "statements": [
                    "John is majoring in Biology.",
                    "John is taking a course on Artificial Intelligence.",
                    "John is a dedicated student.",
                    "John has a part-time job."
                ]
            }
            Output: {
                "statements": [
                    {
                        "statement": "John is majoring in Biology.",
                        "reason": "John's major is explicitly mentioned as Computer Science. There is no information suggesting he is majoring in Biology.",
                        "verdict": 0
                    },
                    {
                        "statement": "John is taking a course on Artificial Intelligence.",
                        "reason": "The context mentions the courses John is currently enrolled in, and Artificial Intelligence is not mentioned. Therefore, it cannot be deduced that John is taking a course on AI.",
                        "verdict": 0
                    },
                    {
                        "statement": "John is a dedicated student.",
                        "reason": "The context states that he spends a significant amount of time studying and completing assignments. Additionally, it mentions that he often stays late in the library to work on his projects, which implies dedication.",
                        "verdict": 1
                    },
                    {
                        "statement": "John has a part-time job.",
                        "reason": "There is no information given in the context about John having a part-time job.",
                        "verdict": 0
                    }
                ]
            }

            Example 2
            Input: {
                "context": "Photosynthesis is a process used by plants, algae, and certain bacteria to convert light energy into chemical energy.",
                "statements": [
                    "Albert Einstein was a genius."
                ]
            }
            Output: {
                "statements": [
                    {
                        "statement": "Albert Einstein was a genius.",
                        "reason": "The context and statement are unrelated",
                        "verdict": 0
                    }
                ]
            }
            -----------------------------

            Now perform the same with the following input
            input: {
                "context": "<knowledge>",
                "statements": [
                    <statements>
                ]
            }
            Output:""";

    public static final String RESP_QUERY_REL_EVAL_PROMPT_TEMPLATE = """
            Your task is to generate a question for a given response, and determine whether the generated question can be correctly answered by the given response, and give reasons why the question can be answered or not. If the generated question can be correctly answered by the response, then the question is recorded as "answered". Otherwise, it is recorded as "unanswered". "Unanswered" means that the response to the generated question is vague, unclear, ambiguous, wrong or irrelevant.
            Please return the output in JSON format and follow the following schema as specified in JSON Schema:{"properties": {"question": {"description":"The question generated by the given response.", "title": "Question", "type": "string"}, "answered": {"description":"Whether the generated question can be answered by the response. 0 means not answered, 1 means answered", "title": "Answered", "type": "integer", "enum": [0, 1]}, "reason": {"description":"The reason why the generated question can (cannot) be answered by the response", "title": "Reason", "type": "string"}}, "required": ["question", "answered", "reason"], "title": "ResponseRelevanceOutput", "type": "object"}.Do not use single quotes in the response, use double quotes instead and escape them properly with backslashes.

            --------Example-----------
            Example 1
            Input: {
                "response": "Albert Einstein was born in Germany."
            }
            Output: {
                "question": "Where was Albert Einstein born?",
                "answered": 1,
                "reason": "The response clearly states where Albert Einstein was born."
            }

            Example 2
            Input: {
                "response": "I don't know what the breakthrough feature of a smartphone invented in 2023 will be, because I don't know anything after 2022."
            }
            Output: {
                "question": "What is the breakthrough feature of a smartphone invented in 2023?",
                "answered": 0,
                "reason": "The response says it doesn't know."
            }

            Example 3
            Input: {
                "response": "The Olympic Games were held in Athens in 2004."
            }
            Output: {
                "question": "When did the Olympic Games begin?",
                "answered": 0,
                "reason": "The response only mentions the time and place of a certain Olympic Games, but not the time when the Olympic Games were first held."
            }
            -----------------------------

            Now perform the same operation with the following input
            Input: {
                "response": "<response>"
            }
            Output:""";

    public static final String TEXT_FORMAT_PROMPT_TEMPLATE = """
                        Your task is to correct the incorrect line breaks and give the corrected sentence given a text with possible incorrect line breaks. Note that except for the line break errors, any other errors and formats need to remain exactly the same. 
                        Please return the output in a JSON format that complies with the following schema as specified in JSON Schema:
                        {"properties": {"result": {"description": "Corrected paragraph", "title": "Result", "type": "string"}}, "required": ["result"], "title": "ResOutput", "type": "object"}.
                        Do not use single quotes in your response but double quotes,properly escaped with a backslash.

                        --------EXAMPLES-----------
                        Example 1
                        Input: {
                            "text": "软件的基本使用

            启动软件

            双击桌面的  图标即可启动软件。
                                                                                           
            软件界面说明

            软件主界面采用的是 Dock 的形式，可自由拖拽调整每个模块的位置，或者关闭某个模块的

            显示。以下所有软件示意图仅供功能展示说明，实际因为软件的配置不同、检测的内容不同，

            显示效果及内容会有差异。

            检测数据栏：显示当天的工件检测数据，命名方式为 序号 | 检测时间；标红的项目代表

            该次检测结果为不合格；"
                        }
                        Output: {
                            "result": "软件的基本使用

            启动软件

            双击桌面的  图标即可启动软件。
                                                                                           
            软件界面说明

            软件主界面采用的是 Dock 的形式，可自由拖拽调整每个模块的位置，或者关闭某个模块的显示。以下所有软件示意图仅供功能展示说明，实际因为软件的配置不同、检测的内容不同，显示效果及内容会有差异。

            检测数据栏：显示当天的工件检测数据，命名方式为 序号 | 检测时间；标红的项目代表该次检测结果为不合格；"
                        }
                        -----------------------------

                        Now perform the same with the following input
                        input: {
                            "text": "<text>",
                        }
                        Output:""";
}
