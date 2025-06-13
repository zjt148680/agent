package com.speedboot.speedbotagent.knowledge.splitter.impl;

import com.speedboot.speedbotagent.knowledge.splitter.CommonConfig;
import com.speedboot.speedbotagent.knowledge.splitter.ISplitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class RecursiveCharacterTextSplitter implements ISplitter {
    // 分隔符，优先级按下标大小递减
    private final List<String> separators = Arrays.asList("\n\n", "\n", ".", ";", "!", "?", ",", " ", "");

    @Override
    public List<String> split(String text) {
        List<String> res = splitRecursive(text.trim(), 0);
        res.removeIf(String::isBlank);
        return res;
    }

    // 递归，依次选用分隔符尝试分割
    private List<String> splitRecursive(String text, int sepIndex) {
        if (sepIndex >= getSeparators().size()) {
            return simpleOverlapSplit(text);
        }
        if (text.length() <= CommonConfig.CHUNK_SIZE) {
            return Arrays.asList(text);
        }

        String sep = getSeparators().get(sepIndex);
        List<String> parts = splitText(text, sep);

        List<String> results = new ArrayList<>();
        List<String> goodParts = new ArrayList<>();
        List<String> badParts = new ArrayList<>();
        for (String part : parts) {
            if (part.length() <= CommonConfig.CHUNK_SIZE) {
                goodParts.add(part);
            } else {
                // 将当前连续的goodParts尝试合并
                if (!goodParts.isEmpty()) {
                    results.addAll(mergeGoodParts(goodParts, sepIndex));
                    goodParts.clear();
                }
                badParts.add(part);
            }
        }
        if (!goodParts.isEmpty()) {
            results.addAll(mergeGoodParts(goodParts, sepIndex));
        }
        for (String part : badParts) {
            results.addAll(splitRecursive(part, sepIndex + 1));
        }

        return results;
    }

    // Fallback: simple sliding window split
    private List<String> simpleOverlapSplit(String text) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < text.length(); i += CommonConfig.CHUNK_SIZE - CommonConfig.OVERLAP_SIZE) {
            String childStr = StringUtils.substring(text, i, i + CommonConfig.CHUNK_SIZE);
            res.add(childStr);
        }
        return res;
    }

    private List<String> splitText(String text, String separator) {
        List<String> res = new ArrayList<>(List.of(text.split(Pattern.quote(separator)))); // 按字面量分割
        res.removeIf(String::isBlank);
        return res;
    }

    private List<String> mergeGoodParts(List<String> goodParts, int sepIndex) {
        String sep = getSeparators().get(sepIndex);

        List<String> res = new ArrayList<>();

        StringBuilder curText = new StringBuilder();
        for (int i = 0, beginMergedPartsIndex = -1; i < goodParts.size(); ) {
            String part = goodParts.get(i);
            if (curText.isEmpty() || curText.length() + sep.length() + part.length() <= CommonConfig.CHUNK_SIZE) {
                mergePartToCurText(curText, part, sep);
                if (beginMergedPartsIndex == -1) {
                    beginMergedPartsIndex = i;
                }
                i++;
            } else {
                res.add(curText.toString());
                beginMergedPartsIndex = deleteEnoughPartsFromFront(curText, goodParts, beginMergedPartsIndex, i, sep);
            }
        }
        if (!curText.isEmpty()) {
            res.add(curText.toString());
        }
        return res;
    }

    private void mergePartToCurText(StringBuilder curText, String part, String sep) {
        if (!curText.isEmpty()) {
            curText.append(sep);
        }
        curText.append(part);
    }

    /**
     * curText从头开始删除多余的part，至其长度在overlap内
     *
     * @param curText    当前的text，会被改变
     * @param parts
     * @param beginIndex
     * @param endIndex
     * @param sep
     * @return 新curText开始的partsIndex，如果全删了，置为-1
     */
    private int deleteEnoughPartsFromFront(StringBuilder curText, List<String> parts, int beginIndex, int endIndex, String sep) {
        // 先删除，再判断。防止一个overlap不能加上后面的任何段导致死循环
        for (int i = beginIndex; i < endIndex; i++) {
            String part = parts.get(i);
            curText.delete(0, part.length() + sep.length());
            if (curText.length() <= CommonConfig.OVERLAP_SIZE) {
                return i + 1 == endIndex ? -1 : i + 1;
            }
        }
        return -1;
    }

    protected List<String> getSeparators() {
        return this.separators;
    }
}
