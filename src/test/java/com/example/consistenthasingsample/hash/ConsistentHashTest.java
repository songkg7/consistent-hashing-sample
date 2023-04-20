package com.example.consistenthasingsample.hash;

import com.example.consistenthasingsample.router.ConsistentHashRouter;
import com.example.consistenthasingsample.router.SimpleHashRouter;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.*;

class ConsistentHashTest {

    private final FixtureMonkey fixture = FixtureMonkey.create();

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
        List<String> traffics = fixture.giveMe(String.class, 1_000_000);

        List<Node> nodes = new ArrayList<>();
        // 4개의 물리적 노드
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        nodes.add(new ServiceNode("192.168.0.3"));
        nodes.add(new ServiceNode("192.168.0.4"));

        SimpleHashRouter<Node> hashRouter = new SimpleHashRouter<>(nodes);

        Map<Node, Long> beforeCacheResult = traffics.stream()
                .map(hashRouter::routeNode)
                .collect(groupingBy(identity(), counting()));

        System.out.println(beforeCacheResult);

        assertThat(hashRouter.getCacheHit()).isLessThan(hashRouter.getCacheMiss());

        System.out.println("cacheHit: " + hashRouter.getCacheHit());
        System.out.println("cacheMiss: " + hashRouter.getCacheMiss());


        // cache 가 적용되어 모든 traffic 이 hit 한다.
        Map<Node, Long> cacheHitResult = traffics.stream()
                .map(hashRouter::routeNode)
                .collect(groupingBy(identity(), counting()));

        assertThat(hashRouter.getCacheHit()).isGreaterThan(hashRouter.getCacheMiss());

        System.out.println(cacheHitResult);

        System.out.println("cacheHit: " + hashRouter.getCacheHit());
        System.out.println("cacheMiss: " + hashRouter.getCacheMiss());
    }

    @Test
    @Disabled("cache 저장소가 Router 객체 내에 존재하기 때문에 node 를 제거해도 무조건 cache 가 hit 된다.")
    @DisplayName("하지만 node 목록이 변하면 대부분의 데이터가 재분배된다. 결국 많은 캐시 미스가 발생한다.")
    void hash3() {
        List<String> traffics = fixture.giveMe(String.class, 1_000_000);

        List<Node> nodes = new ArrayList<>();
        nodes.add(new ServiceNode("192.168.0.1"));
        nodes.add(new ServiceNode("192.168.0.2"));
        nodes.add(new ServiceNode("192.168.0.3"));
        nodes.add(new ServiceNode("192.168.0.4"));

        SimpleHashRouter<Node> hashRouter = new SimpleHashRouter<>(nodes);

        Map<Node, Long> beforeCacheResult = traffics.stream()
                .map(hashRouter::routeNode)
                .collect(groupingBy(identity(), counting()));
        System.out.println(beforeCacheResult);
        System.out.println("cacheHit: " + hashRouter.getCacheHit());
        System.out.println("cacheMiss: " + hashRouter.getCacheMiss());
        assertThat(hashRouter.getCacheHit()).isLessThan(hashRouter.getCacheMiss());

        hashRouter.removeNode(new ServiceNode("192.168.0.1"));

        Map<Node, Long> afterCacheResult = traffics.stream()
                .map(hashRouter::routeNode)
                .collect(groupingBy(identity(), counting()));

        System.out.println(afterCacheResult);
        System.out.println("cacheHit: " + hashRouter.getCacheHit());
        System.out.println("cacheMiss: " + hashRouter.getCacheMiss());

        assertThat(hashRouter.getCacheHit()).isLessThan(hashRouter.getCacheMiss());
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
