# Consistent Hashing

## Introduction

Consistent hashing is a special kind of hashing such that when a hash table is resized and consistent hashing is used, only `k/n` keys need to be remapped on average, where `k` is the number of keys, and `n` is the number of slots.

## Graph

### Simple Hash Router

<img width="998" alt="Screenshot 2023-05-01 오후 7 28 41" src="https://user-images.githubusercontent.com/56438906/235440938-da49978b-dd09-40bc-b386-7af6838ec02d.png">

### Consistent Hash Router

<img width="1233" alt="Screenshot 2023-05-01 오후 7 36 47" src="https://user-images.githubusercontent.com/56438906/235441574-fdda9ae8-b40b-4949-a174-f68078e7629b.png">
