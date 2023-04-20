package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.HashAlgorithm;
import com.example.consistenthasingsample.hash.Node;
import com.example.consistenthasingsample.hash.SHA256Hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHashRouter<T extends Node> implements HashRouter<T> {

    private final HashAlgorithm hashAlgorithm;
    private final Map<Long, T> cache = new HashMap<>();
    private final List<T> nodes;

    private long cacheHit = 0;
    private long cacheMiss = 0;

    public SimpleHashRouter(List<T> nodes) {
        this.hashAlgorithm = new SHA256Hash();
        this.nodes = nodes;
    }

    public SimpleHashRouter(HashAlgorithm hashAlgorithm, List<T> nodes) {
        this.hashAlgorithm = hashAlgorithm;
        this.nodes = nodes;
    }

    @Override
    public T routeNode(String key) {
        long hash = this.hashAlgorithm.hash(key);
        T node = cache.getOrDefault(hash, null);
        if (node == null) {
            cacheMiss++;
            int index = (int) (hash % nodes.size());
            cache.put(hash, nodes.get(index));
            return nodes.get(index);
        } else {
            cacheHit++;
            return node;
        }
    }

    public void removeNode(T node) {
        nodes.remove(node);
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
