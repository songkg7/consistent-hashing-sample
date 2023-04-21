package com.example.consistenthashingsample.router;

import com.example.consistenthashingsample.hash.HashAlgorithm;
import com.example.consistenthashingsample.hash.MD5Hash;
import com.example.consistenthashingsample.hash.Node;

import java.util.List;

public class SimpleHashRouter<T extends Node> implements HashRouter<T> {

    private final HashAlgorithm hashAlgorithm;
    private final List<T> nodes;


    public SimpleHashRouter(List<T> nodes) {
        this.hashAlgorithm = new MD5Hash();
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
