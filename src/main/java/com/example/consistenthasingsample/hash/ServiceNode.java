package com.example.consistenthasingsample.hash;

import java.util.*;

public class ServiceNode implements Node {
    private final String ip;
    private final Map<String, String> cache = new HashMap<>();

    public ServiceNode(String ip) {
        this.ip = ip;
    }

    public void save(String data) {
        cache.put(data, data);
    }

    public String get(String data) {
        return cache.get(data);
    }

    @Override
    public String getKey() {
        return ip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceNode that = (ServiceNode) o;
        return Objects.equals(ip, that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }

    @Override
    public String toString() {
        return "ServiceNode{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
