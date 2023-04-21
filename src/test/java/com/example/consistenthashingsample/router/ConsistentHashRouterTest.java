package com.example.consistenthashingsample.router;

import com.example.consistenthashingsample.hash.Node;
import com.example.consistenthashingsample.hash.VirtualNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import static org.assertj.core.api.Assertions.assertThat;

class ConsistentHashRouterTest {

    private static final int VIRTUAL_COUNT = 1;
    private ConsistentHashRouter<Node> consistentHashRouter;

    @BeforeEach
    void setUp() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(() -> "1");
        consistentHashRouter = new ConsistentHashRouter<>(nodes, VIRTUAL_COUNT);
    }

    @Test
    void addNode() {
        consistentHashRouter.addNode(() -> "2", VIRTUAL_COUNT);

        assertThat(consistentHashRouter.getVirtualNodeCountOf(() -> "2")).isEqualTo(VIRTUAL_COUNT);
        assertThat(consistentHashRouter.getRing()).hasSize(2 * VIRTUAL_COUNT);
    }

    @Test
    void getRing() {
        SortedMap<Long, VirtualNode<Node>> ring = consistentHashRouter.getRing();

        assertThat(ring).hasSize(VIRTUAL_COUNT);
    }

    @Test
    void routeNode() {
        Node routeNode = consistentHashRouter.routeNode("1");

        assertThat(routeNode).isNotNull();
    }

    @Test
    void removeNode() {
        consistentHashRouter.removeNode(() -> "1");
        Node node = consistentHashRouter.routeNode("1");

        assertThat(node).isNull();
    }

    @Test
    void getVirtualNodeCountOf() {
        Integer virtualNodeCount = consistentHashRouter.getVirtualNodeCountOf(() -> "1");

        assertThat(virtualNodeCount).isEqualTo(VIRTUAL_COUNT);
    }
}
