package com.example.consistenthasingsample.hash;

import java.io.Serial;
import java.io.Serializable;

public class VirtualNode<T extends Node> implements Node, Serializable {

    @Serial
    private static final long serialVersionUID = -1066173071509622053L;

    final T physicalNode; // NOSONAR

    final Integer replicaIndex;

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

}