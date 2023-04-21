package com.example.consistenthashingsample.hash;

public interface HashAlgorithm {

    /**
     * @param key to be hashed
     * @return hash value
     */
    long hash(String key);
}
