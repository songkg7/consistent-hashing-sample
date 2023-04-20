package com.example.consistenthasingsample.router;

import com.example.consistenthasingsample.hash.Node;

public interface HashRouter<T extends Node> {

    T routeNode(String key);

}
