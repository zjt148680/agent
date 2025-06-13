package com.speedboot.speedbotagent.knowledge.cleaner.impl;

import com.speedboot.speedbotagent.knowledge.cleaner.ICleaner;
import org.springframework.stereotype.Component;

@Component
public class DefaultCleaner implements ICleaner {
    @Override
    public String clean(String text) {
        return text;
    }
}
