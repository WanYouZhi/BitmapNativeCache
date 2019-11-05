package com.wan.natives;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * author: 万友志
 * created on: 2019/10/28 14:00
 * description:
 */
public final class BitmapCache {
    private static HashMap<String, CacheInfo> hashMap = new HashMap<>();

    private BitmapCache() {
        throw new AssertionError();
    }

    public static void add(String path, int reqWidth, int reqHeight) {
        add(path, BitmapUtil.decodeBitmapFromPath(path, reqWidth, reqHeight));
    }

    public static void add(String path) {
        add(path, 0, 0);
    }

    public static Bitmap get(String pathKey) {
        CacheInfo info = hashMap.get(pathKey);
        byte[] bytes = info == null ? null : Cache.get(info.id);
        return bytes == null ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void add(String pathKey, Bitmap bitmap) {
        int size = bitmap.getHeight() * bitmap.getWidth();
        CacheInfo info = hashMap.get(pathKey);
        if (info != null) {
            if (info.size >= size) {
                return;
            } else {
                Cache.remove(info.id);
            }
        }
        byte[] bytes = getCompressByte(bitmap);
        long id = Cache.add(bytes);
        hashMap.put(pathKey, new CacheInfo(id, size));
    }

    public static void remove(String pathKey) {
        CacheInfo info = hashMap.get(pathKey);
        if (info != null) {
            Cache.remove(info.id);
        }
    }

    public static Set<String> getPaths() {
        return hashMap.keySet();
    }

    private static byte[] getCompressByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
        return stream.toByteArray();
    }

    public static void clear() {
        Cache.clear();
    }

    private static final class CacheInfo {
        private long id;
        private int size;

        private CacheInfo(long id, int size) {
            this.id = id;
            this.size = size;
        }
    }
}
