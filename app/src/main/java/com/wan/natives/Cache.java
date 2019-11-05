package com.wan.natives;

import java.util.concurrent.atomic.AtomicLong;

/**
 * author: 万友志
 * created on: 2019/10/30 17:15
 * description:
 */
public final class Cache {
    static {
        System.loadLibrary("cache");
    }

    private Cache() {
        throw new AssertionError();
    }

    private static native void addCache(long id, byte[] bytes);

    private static native void removeCache(long id);

    private static native byte[] getCache(long id);

    private static native long getCacheLength(long id);

    private static native long cacheSize();

    private static native void clearCache();

    public static synchronized long add(byte[] bytes) {
        long id = getIncrementId();
        addCache(id, bytes);
        return id;
    }

    private static AtomicLong atomicLong = new AtomicLong(1);

    private static long getIncrementId() {
        return atomicLong.getAndIncrement();
    }

    public static synchronized void remove(long id) {
        removeCache(id);
    }

    public static synchronized byte[] get(long id) {
        return getCache(id);
    }

    public static synchronized long getLength(long id) {
        return getCacheLength(id);
    }

    public static synchronized long size() {
        return cacheSize();
    }

    public static synchronized void clear() {
        clearCache();
    }
}
