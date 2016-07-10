package com.rex.hwong.simplenet.cache;

/**
 * @author dong {hwongrex@gmail.com}
 * @date 16/7/10
 * @time 上午10:44
 */

/**
 * 请求缓存接口
 * @param <K> key的类型
 * @param <V> value的类型
 */
public interface Cache<K, V> {
    V get(K key);

    void put(K key, V value);

    void remove(K key);
}
