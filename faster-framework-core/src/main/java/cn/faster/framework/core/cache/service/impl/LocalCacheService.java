package cn.faster.framework.core.cache.service.impl;

import cn.faster.framework.core.cache.entity.LocalCacheEntity;
import cn.faster.framework.core.cache.service.ICacheService;
import org.apache.shiro.util.SoftHashMap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhangbowen 2018/6/6 14:37
 */
public class LocalCacheService<V> implements ICacheService<V> {
    private Map<String, LocalCacheEntity<V>> softHashMap = new SoftHashMap<>();

    @Override
    public void set(String key, V value, long exp) {
        LocalCacheEntity<V> localCacheEntity = new LocalCacheEntity<>();
        localCacheEntity.setValue(value);
        localCacheEntity.setSaveTime(System.currentTimeMillis());
        localCacheEntity.setExp(exp);
        softHashMap.put(key, localCacheEntity);
    }

    @Override
    public V delete(String key) {
        V value = get(key);
        if (value != null) {
            softHashMap.remove(key);
        }
        return value;
    }

    @Override
    public V get(String key) {
        LocalCacheEntity<V> localCacheEntity = softHashMap.get(key);
        //说明没过期
        if (localCacheEntity != null && (localCacheEntity.getExp() == 0 || ((System.currentTimeMillis() - localCacheEntity.getSaveTime()) <= localCacheEntity.getExp() * 1000))) {
            return localCacheEntity.getValue();
        }
        softHashMap.remove(key);
        return null;
    }

    @Override
    public void clear(String cachePrefix) {
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                softHashMap.remove(k);
            }
        });
    }

    @Override
    public int size(String cachePrefix) {
        AtomicInteger size = new AtomicInteger(0);
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                size.getAndIncrement();
            }
        });
        return size.get();
    }

    @Override
    public Set<String> keys(String cachePrefix) {
        return softHashMap.keySet().stream().filter(k -> k.startsWith(cachePrefix)).collect(Collectors.toSet());
    }

    @Override
    public Collection<V> values(String cachePrefix) {
        List<V> values = new ArrayList<>();
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                V value = get(k);
                if (value != null) {
                    values.add(value);
                }
            }
        });
        return values;
    }
}
