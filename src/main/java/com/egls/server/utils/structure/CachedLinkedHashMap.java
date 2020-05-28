package com.egls.server.utils.structure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 利用{@link LinkedHashMap}实现一个LRU算法的缓存.支持线程安全的使用方式.
 * <pre>
 *     线程安全的用法:
 *     Collections.synchronizedMap(new CachedLinkedHashMap<>(cacheSize));
 * </pre>
 *
 * @author mayer - [Created on 2018-08-21 12:30]
 */
public final class CachedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedLinkedHashMap.class);

    private static final long serialVersionUID = 1L;

    /**
     * The default initial capacity 16 - MUST be a power of two.
     */
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    /**
     * The maximum capacity, used if a higher value is implicitly specified
     * by either of the constructors with arguments.
     * MUST be a power of two <= 1<<30.
     */
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * The load factor used when none specified in constructor.
     */
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final int cacheSize;

    private final BiConsumer<K, V> onRemoveEldestEntry;

    public CachedLinkedHashMap() {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.cacheSize = MAXIMUM_CAPACITY;
        this.onRemoveEldestEntry = null;
    }

    public CachedLinkedHashMap(BiConsumer<K, V> onRemoveEldestEntry) {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.cacheSize = MAXIMUM_CAPACITY;
        this.onRemoveEldestEntry = onRemoveEldestEntry;
    }

    public CachedLinkedHashMap(final int cacheSize) {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.cacheSize = cacheSize;
        this.onRemoveEldestEntry = null;
    }

    public CachedLinkedHashMap(final int cacheSize, final BiConsumer<K, V> onRemoveEldestEntry) {
        super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, true);
        this.cacheSize = cacheSize;
        this.onRemoveEldestEntry = onRemoveEldestEntry;
    }

    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
        boolean remove = this.size() > this.cacheSize;
        if (remove && this.onRemoveEldestEntry != null) {
            try {
                onRemoveEldestEntry.accept(eldest.getKey(), eldest.getValue());
            } catch (Exception exception) {
                LOGGER.error("CachedLinkedHashMap onRemoveEldestEntry error", exception);
            }
        }
        return remove;
    }

}
