package com.example.consistenthashingsample.hash;

import java.util.Objects;

public class ServiceNode extends CacheNode {

    private final String ip;

    public ServiceNode(String ip) {
        this.ip = ip;
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
