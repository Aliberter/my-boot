package cn.aliberter.boot.common.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 本地全局缓存器
 *
 * @author : aliberter
 * @version : 1.0
 */
@Slf4j
@SuppressWarnings("unused")
public class LocalCache {

    private static final int CAPACITY = 4096;
    private static final Cache<String, Object> CACHE = new FIFOCache<>(CAPACITY);

    private LocalCache() {
    }

    public static void put(String key, Object value) {
        CACHE.put(key, value);
    }

    public static void put(String key, Object value, long timeout) {
        CACHE.put(key, value, timeout);
    }

    public static boolean containsKey(String key) {
        return CACHE.containsKey(key);
    }

    public static Object get(String key) {
        return CACHE.get(key);
    }

    public static String getStr(String key) {
        return Convert.convert(String.class, Optional.ofNullable(CACHE.get(key)).orElse(""));
    }

    public static Integer getInt(String key) {
        return Convert.convert(Integer.class, CACHE.get(key));
    }

    public static Map<String, Object> getHash(String key) {
        return Convert.convert(new TypeReference<HashMap<String, Object>>() {
        }, Optional.ofNullable(CACHE.get(key)).orElse(new HashMap<>(2)));
    }

    public static List<Object> getCollection(String key) {
        return Convert.convert(new TypeReference<List<Object>>() {
        }, Optional.ofNullable(CACHE.get(key)).orElse(new ArrayList<>()));
    }

    public static void remove(String key) {
        CACHE.remove(key);
    }

    public static int size() {
        return CACHE.size();
    }

    public static void init() {
        log.info("本地全局缓存器已加载，初始容量：{}", CAPACITY);
    }
}
