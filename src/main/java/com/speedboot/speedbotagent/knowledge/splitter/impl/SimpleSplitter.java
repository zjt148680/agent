package com.speedboot.speedbotagent.knowledge.splitter.impl;

import com.speedboot.speedbotagent.knowledge.splitter.CommonConfig;
import com.speedboot.speedbotagent.knowledge.splitter.ISplitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleSplitter implements ISplitter {
    @Override
    public List<String> split(String text) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < text.length(); i += CommonConfig.CHUNK_SIZE - CommonConfig.OVERLAP_SIZE) {
            String childStr = StringUtils.substring(text, i, i + CommonConfig.CHUNK_SIZE);
            res.add(childStr);
        }
        return res;
    }
}
