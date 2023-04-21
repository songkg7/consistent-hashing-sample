package com.example.consistenthashingsample.router;

import com.example.consistenthashingsample.hash.Node;

public interface HashRouter<T extends Node> {

    T routeNode(String key);

}
