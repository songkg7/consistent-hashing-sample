package com.example.consistenthasingsample.hash;

import java.util.HashMap;
import java.util.Map;

public abstract class CacheNode implements Node {

    private final Map<Long, String> cache = new HashMap<>();
    private final HashAlgorithm hashAlgorithm = new SHA256Hash();
    private long cacheHit = 0;
    private long cacheMiss = 0;

    public String findInCache(String data) {
        long hash = this.hashAlgorithm.hash(data);
        if (cache.containsKey(hash)) {
            cacheHit++;
            return cache.get(hash);
        } else {
            cacheMiss++;
            cache.put(hash, data);
        }
        return null;
    }

    public long getCacheHit() {
        return cacheHit;
    }

    public long getCacheMiss() {
        return cacheMiss;
    }

    public void clearCache() {
        cache.clear();
        cacheHit = 0;
        cacheMiss = 0;
    }

}
