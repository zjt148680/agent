package com.speedboot.speedbotagent.knowledge.splitter.impl;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ChineseRecursiveCharacterTextSplitter extends RecursiveCharacterTextSplitter {
    // 分隔符，优先级按下标大小递减
    private final List<String> separators = Arrays.asList(
            "\n\n", "\n", ".", "。", ";", "；", "!", "！", "?", "？", ",", "，", " ", "");

    @Override
    protected List<String> getSeparators() {
        return separators;
    }
}
