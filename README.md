# Consistent Hashing

## Introduction

Consistent hashing is a special kind of hashing such that when a hash table is resized and consistent hashing is used, only `k/n` keys need to be remapped on average, where `k` is the number of keys, and `n` is the number of slots.
