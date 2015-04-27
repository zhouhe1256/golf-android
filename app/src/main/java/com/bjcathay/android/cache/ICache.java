package com.bjcathay.android.cache;

public interface ICache<K, V> {
    void set(K k, V v);

    V get(K k);

    void clear();
}
