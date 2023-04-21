package com.example.consistenthashingsample.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Hash implements HashAlgorithm {
        MessageDigest instance;

        public SHA256Hash() {
            try {
                instance = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("no algorythm found");
            }
        }

        @Override
        public long hash(String key) {
            instance.reset();
            instance.update(key.getBytes());
            byte[] digest = instance.digest();
            long h = 0;
            for (int i = 0; i < 8; i++) {
                h <<= 8;
                h |= (digest[i]) & 0xFF;
            }
            return h;
        }
    }
