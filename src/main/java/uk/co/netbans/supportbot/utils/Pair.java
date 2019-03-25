package uk.co.netbans.supportbot.utils;

public class Pair<K, V> {
    private final K k;
    private final V v;

    public Pair(K key, V value) {
        k = key;
        v = value;
    }

    public K getKey() {
        return k;
    }

    public V getValue() {
        return v;
    }
}
