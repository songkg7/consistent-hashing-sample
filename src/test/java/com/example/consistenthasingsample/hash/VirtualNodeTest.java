package com.example.consistenthasingsample.hash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class VirtualNodeTest {
    @Test
    void testConstructor() {
        VirtualNode<Node> actualVirtualNode = new VirtualNode<>(mock(Node.class), 1);

        Node expectedPhysicalNode = actualVirtualNode.getPhysicalNode();

        assertThat(actualVirtualNode.getPhysicalNode()).isEqualTo(expectedPhysicalNode);
        assertThat(actualVirtualNode.replicaIndex).isEqualTo(1);
    }

    @Test
    void testGetKey() {
        Node node = mock(Node.class);
        when(node.getKey()).thenReturn("Key");
        assertThat((new VirtualNode<>(node, 1)).getKey()).isEqualTo("Key-1");
        verify(node).getKey();
    }

    @Test
    void testIsVirtualOf() {
        Node node = mock(Node.class);
        when(node.getKey()).thenReturn("Key");
        VirtualNode<Node> virtualNode = new VirtualNode<>(node, 1);
        Node node1 = mock(Node.class);
        when(node1.getKey()).thenReturn("Key");
        assertThat(virtualNode.isVirtualOf(node1)).isTrue();
        verify(node).getKey();
        verify(node1).getKey();
    }

    @Test
    void testIsVirtualOf2() {
        Node node = mock(Node.class);
        when(node.getKey()).thenReturn("foo");
        VirtualNode<Node> virtualNode = new VirtualNode<>(node, 1);
        Node node1 = mock(Node.class);
        when(node1.getKey()).thenReturn("Key");
        assertThat(virtualNode.isVirtualOf(node1)).isFalse();
        verify(node).getKey();
        verify(node1).getKey();
    }
}
