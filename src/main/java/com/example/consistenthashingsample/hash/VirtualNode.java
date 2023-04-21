package com.example.consistenthashingsample.hash;

public class VirtualNode<T extends Node> implements Node {

    private final T physicalNode;

    private final Integer replicaIndex;

    public VirtualNode(T physicalNode, Integer replicaIndex) {
        this.physicalNode = physicalNode;
        this.replicaIndex = replicaIndex;
    }

    @Override
    public String getKey() {
        return physicalNode.getKey() + "-" + replicaIndex;
    }

    public boolean isVirtualOf(T anyPhysicalNode) {
        return anyPhysicalNode.getKey().equals(this.physicalNode.getKey());
    }

    public T getPhysicalNode() {
        return this.physicalNode;
    }

    public Integer getReplicaIndex() {
        return this.replicaIndex;
    }

}
