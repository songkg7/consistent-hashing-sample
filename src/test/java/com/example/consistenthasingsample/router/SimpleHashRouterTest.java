package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.ServiceNode;
import com.example.consistenthasingsample.router.SimpleHashRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleHashRouterTest {

    SimpleHashRouter<ServiceNode> router;

    @BeforeEach
    void setUp() {
        List<ServiceNode> nodes = new ArrayList<>();
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        router = new SimpleHashRouter<>(nodes);
    }

    @Test
    @DisplayName("cache hit")
    void shouldCacheHit() {
        ServiceNode node = router.routeNode("1");
        assertThat(router.getCacheMiss()).isEqualTo(1);
        assertThat(node).isNotNull();

        ServiceNode cachedNode = router.routeNode("1");
        assertThat(router.getCacheHit()).isEqualTo(1);
        assertThat(cachedNode).isEqualTo(node);
    }

    @Test
    @DisplayName("cache miss")
    void shouldCacheMiss() {
        ServiceNode node = router.routeNode("1");
        assertThat(router.getCacheMiss()).isEqualTo(1);
        assertThat(node).isNotNull();

        ServiceNode cachedNode = router.routeNode("2");
        assertThat(router.getCacheMiss()).isEqualTo(2);
        assertThat(cachedNode).isNotEqualTo(node);
    }

    @Test
    @DisplayName("node 삭제로 인한 캐시 미스")
    void removeNode() {
        ServiceNode node = router.routeNode("1");
        assertThat(router.getCacheMiss()).isEqualTo(1);
        assertThat(node).isNotNull();

        router.removeNode(new ServiceNode("192.168.0.2"));
        ServiceNode cachedNode = router.routeNode("1");
        assertThat(router.getCacheMiss()).isEqualTo(2);
        assertThat(cachedNode).isNotEqualTo(node);
    }
}
