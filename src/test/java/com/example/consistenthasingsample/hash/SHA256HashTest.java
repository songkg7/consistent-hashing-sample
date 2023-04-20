package com.example.consistenthasingsample.hash;

import org.junit.jupiter.api.Test;

class SHA256HashTest {

    @Test
    void hash() {
        HashAlgorithm hashAlgorithm = new SHA256Hash();
        long hash1 = hashAlgorithm.hash("1");
        long hash2 = hashAlgorithm.hash("2");
        long hash3 = hashAlgorithm.hash("3");
        long hash4 = hashAlgorithm.hash("4");

        System.out.println("hash1: " + hash1);
        System.out.println("hash2: " + hash2);
        System.out.println("hash3: " + hash3);
        System.out.println("hash4: " + hash4);
    }

}
