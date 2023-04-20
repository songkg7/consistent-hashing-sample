package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashRouter<T extends Node> implements HashRouter<T> {

    private final HashAlgorithm hashAlgorithm;

    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();

    public ConsistentHashRouter(Collection<T> physicalNodes, Integer virtualNodeCount) {
        this(physicalNodes, virtualNodeCount, new SHA256Hash());
    }

    public ConsistentHashRouter(Collection<T> physicalNodes, Integer virtualNodeCount, HashAlgorithm hashAlgorithmImpl) {
        this.hashAlgorithm = hashAlgorithmImpl;
        for (T physicalNode : physicalNodes) {
            addNode(physicalNode, virtualNodeCount);
        }
    }

    public SortedMap<Long, VirtualNode<T>> getRing() {
        return ring;
    }

    @Override
    public T routeNode(String businessKey) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashOfBusinessKey = this.hashAlgorithm.hash(businessKey);
        // SortedMap 은 정렬되어 있기 때문에 tailMap 을 통해 hashOfBusinessKey 보다 큰 값들을 가져올 수 있다.
        SortedMap<Long, VirtualNode<T>> biggerTailMap = ring.tailMap(hashOfBusinessKey);
        Long nodeHash;
        if (biggerTailMap.isEmpty()) {
            nodeHash = ring.firstKey();
        } else {
            nodeHash = biggerTailMap.firstKey();
        }
        VirtualNode<T> virtualNode = ring.get(nodeHash);
        return virtualNode.getPhysicalNode();
    }

    public void addNode(T physicalNode, Integer virtualNodeCount) {
        Integer virtualNodeCountExistBefore = getVirtualNodeCountOf(physicalNode);
        for (int i = 0; i < virtualNodeCount; i++) {
            VirtualNode<T> virtualNode = new VirtualNode<>(physicalNode, virtualNodeCountExistBefore + i);
            ring.put(this.hashAlgorithm.hash(virtualNode.getKey()), virtualNode);
        }
    }

    public void removeNode(T physicalNode) {
        Iterator<Long> iterator = ring.keySet().iterator();
        while (iterator.hasNext()) {
            Long nodeHashKey = iterator.next();
            VirtualNode<T> virtualNode = ring.get(nodeHashKey);
            if (virtualNode.isVirtualOf(physicalNode)) {
                iterator.remove();
            }
        }
    }

    protected Integer getVirtualNodeCountOf(T physicalNode) {
        int countVirtualNode = 0;
        for (VirtualNode<T> virtualNode : ring.values()) {
            if (virtualNode.isVirtualOf(physicalNode)) {
                countVirtualNode++;
            }
        }
        return countVirtualNode;
    }


}
