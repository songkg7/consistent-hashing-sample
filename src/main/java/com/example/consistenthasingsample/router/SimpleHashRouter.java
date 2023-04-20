package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.HashAlgorithm;
import com.example.consistenthasingsample.hash.Node;
import com.example.consistenthasingsample.hash.SHA256Hash;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleHashRouter<T extends Node> implements HashRouter<T> {

    private final HashAlgorithm hashAlgorithm;
    private final List<T> nodes;


    public SimpleHashRouter(List<T> nodes) {
        this.hashAlgorithm = new SHA256Hash();
        this.nodes = nodes;
    }

    public SimpleHashRouter(HashAlgorithm hashAlgorithm, List<T> nodes) {
        this.hashAlgorithm = hashAlgorithm;
        this.nodes = nodes;
    }

    public List<T> getNodes() {
        return nodes;
    }

    @Override
    public T routeNode(String key) {
        long hash = this.hashAlgorithm.hash(key);
        int index = (int) (hash % nodes.size());
        return nodes.get(index);
    }

    public void removeNode(T node) {
        nodes.remove(node);
    }

    public void addNode(T node) {
        nodes.add(node);
    }

}
