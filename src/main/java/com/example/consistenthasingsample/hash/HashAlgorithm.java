package com.example.consistenthasingsample.hash;

public interface HashAlgorithm {

    /**
     * @param key to be hashed
     * @return hash value
     */
    long hash(String key);
}
