package com.example.consistenthasingsample.hash;

import com.example.consistenthasingsample.router.ConsistentHashRouter;
import com.example.consistenthasingsample.router.SimpleHashRouter;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

class ConsistentHashTest {

    private final FixtureMonkey fixture = FixtureMonkey.create();
    private SimpleHashRouter<CacheNode> hashRouter;

    @BeforeEach
    void setUp() {
        List<CacheNode> nodes = new ArrayList<>();
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        nodes.add(new ServiceNode("192.168.0.3"));
        nodes.add(new ServiceNode("192.168.0.4"));
        hashRouter = new SimpleHashRouter<>(nodes);
    }

    @Test
    @DisplayName("데이터의 수가 적은 경우는 별 의미가 없다.")
    void hash() {
        int serverCount = 5;
        // 24 data
        List<String> data = List.of("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", "theta", "iota", "kappa", "lambda", "mu", "nu", "xi", "omicron", "pi", "rho", "sigma", "tau", "upsilon", "phi", "chi", "psi", "omega");

        // when
        var hashes = data.stream()
                .map(Object::hashCode)
                .map(Math::abs)
                .map(v -> v % serverCount) // 0, 1, 2, 3, 4
                .collect(groupingBy(identity(), counting()));

        // then
        System.out.println(hashes);
    }

    @Test
    @DisplayName("랜덤 데이터를 해시, 간단한 해시 함수여도 비교적 균등하게 분산된다.")
    void hash2() {
        var traffics = fixture.giveMe(String.class, 1_000_000);

        for (String traffic : traffics) {
            CacheNode node = hashRouter.routeNode(traffic);
            node.findInCache(traffic);
        }
        printNodeStatus(hashRouter.getNodes());

        // cache 가 적용되어 모든 traffic 이 hit 한다.
        for (String traffic : traffics) {
            CacheNode node = hashRouter.routeNode(traffic);
            String inCache = node.findInCache(traffic);
            assertThat(inCache).isNotNull();
        }
        printNodeStatus(hashRouter.getNodes());
    }

    private static void printNodeStatus(List<CacheNode> nodes) {
        for (CacheNode node : nodes) {
            System.out.println("node: " + node);
            System.out.println("Cache Hit: " + node.getCacheHit());
            System.out.println("Cache Miss: " + node.getCacheMiss());
        }
    }

    @Test
    @DisplayName("하지만 node 목록이 변하면 대부분의 트래픽이 기존과는 다른 노드를 향한다. 결국 많은 캐시 미스가 발생한다.")
    void hash3() {
        var traffics = fixture.giveMe(String.class, 1_000_000);

        for (String traffic : traffics) {
            CacheNode node = hashRouter.routeNode(traffic);
            node.findInCache(traffic);
        }
        printNodeStatus(hashRouter.getNodes());

        // node 가 제거된다면 제거된 노드로 분배되던 트래픽은 대부분 cache miss 가 발생한다.
        hashRouter.removeNode(new ServiceNode("192.168.0.1"));
        hashRouter.removeNode(new ServiceNode("192.168.0.2"));

        for (String traffic : traffics) {
            CacheNode node = hashRouter.routeNode(traffic);
            node.findInCache(traffic);
        }
        printNodeStatus(hashRouter.getNodes());
    }

    @Test
    @DisplayName("그렇다면 Consistent Hashing 을 사용하면 어떨까?")
    void hash4() {
        List<Node> nodes = new ArrayList<>();
        // 4개의 물리적 노드
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        nodes.add(new ServiceNode("192.168.0.3"));
        nodes.add(new ServiceNode("192.168.0.4"));

        ConsistentHashRouter<Node> consistentHashRouter = new ConsistentHashRouter<>(nodes, 1);
        List<String> traffics = fixture.giveMe(String.class, 1_000_000);

        traffics.stream()
                .map(consistentHashRouter::routeNode)
                .collect(groupingBy(Node::getKey, counting()));

        Node node = consistentHashRouter.routeNode("5");
        System.out.println(node.getKey());


        // ring 에는 물리적 노드의 키가 아니라 가상 노드의 키로 구성되어 있다.
        SortedMap<Long, VirtualNode<Node>> ring = consistentHashRouter.getRing();

        assertThat(node).isNotNull();
    }

}
