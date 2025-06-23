package com.speedboot.speedbotagent.prompttemplate;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.TemplateRenderer;
import org.springframework.ai.template.st.StTemplateRenderer;

public class PromptTemplates {
    private static final char TEMPLATE_START_DELIMITER_TOKEN = '<';

    private static final char TEMPLATE_END_DELIMITER_TOKEN = '>';

    public static final TemplateRenderer DEFAULT_PROMPT_TEMPLATE_RENDERER = StTemplateRenderer.builder()
            .startDelimiterToken(TEMPLATE_START_DELIMITER_TOKEN)
            .endDelimiterToken(TEMPLATE_END_DELIMITER_TOKEN).build();

    public static PromptTemplate.Builder DEFAULT_PROMPT_TEMPLATE_BUILDER = PromptTemplate
            .builder()
            .renderer(DEFAULT_PROMPT_TEMPLATE_RENDERER);

    public static final String CHAT_PROMPT_TEMPLATE_SYSTEM = """
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
            """;
    public static final String CHAT_PROMPT_TEMPLATE_USER = """
            [外部知识]
            <knowledge>

            [问题]
            <query>""";

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
            Input: {
                "text": "<text>",
            }
            Output:""";
}
