package com.supadata.constant;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class LRUDATACache extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    protected int maxElements;

    public LRUDATACache() {
        super(100, 0.75F, true);
        this.maxElements = 100;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
        return (size() > this.maxElements);
    }
}
