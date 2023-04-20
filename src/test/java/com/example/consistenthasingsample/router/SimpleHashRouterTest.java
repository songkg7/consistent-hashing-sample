package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.ServiceNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleHashRouterTest {

    private SimpleHashRouter<ServiceNode> router;

    @BeforeEach
    void setUp() {
        List<ServiceNode> nodes = new ArrayList<>();
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        router = new SimpleHashRouter<>(nodes);
    }

    @Test
    @DisplayName("같은 키로 router 에 접근하여 데이터를 조회하면 cache hit 된다.")
    void shouldCacheHit() {
        ServiceNode node = router.routeNode("1");
        String data = node.findInCache("alpha");
        assertThat(data).isNull();
        assertThat(node.getCacheMiss()).isEqualTo(1);

        // 1번 키로 다시 접근
        ServiceNode cachedNode = router.routeNode("1");
        String cachedData = node.findInCache("alpha");

        // then
        assertThat(cachedData).isNotNull();
        assertThat(cachedNode)
                .isEqualTo(node)
                .extracting(ServiceNode::getCacheHit).isEqualTo(1L);
    }

    @Test
    @DisplayName("다른 키로 router 에 접근하여 데이터를 조회하면 cache miss 된다.")
    void shouldCacheMiss() {
        // 1번 키로 접근
        ServiceNode node = router.routeNode("1");
        String data = node.findInCache("alpha");
        assertThat(data).isNull();
        assertThat(node.getCacheMiss()).isEqualTo(1L);

        // 2번 키로 접근
        ServiceNode cachedNode = router.routeNode("2");
        String cachedData = cachedNode.findInCache("alpha");
        assertThat(cachedData).isNull();
        assertThat(cachedNode)
                .isNotEqualTo(node)
                .extracting(ServiceNode::getCacheMiss).isEqualTo(1L);
    }

    @Test
    @DisplayName("node 가 제거되면 hash 재분배가 일어나기 때문에 cache miss 된다.")
    void removeNode() {
        ServiceNode node = router.routeNode("1");
        String data = node.findInCache("alpha");
        assertThat(data).isNull();
        assertThat(node.getCacheMiss()).isEqualTo(1L);

        router.removeNode(node);
        ServiceNode cachedNode = router.routeNode("1");
        String cachedData = cachedNode.findInCache("alpha");
        assertThat(cachedData).isNull();
        assertThat(cachedNode).isNotEqualTo(node);
    }
}
