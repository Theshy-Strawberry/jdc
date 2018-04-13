package com.senyoboss.ext.kit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Collection Factory 方便集合构建的工厂类<p>
 * 省略泛型new的过程，同时在可能的情况下 尽可能使用更优越的实现类
 */
public final class CFKit {
	
    /**
     * 构建一个通用的 {@link HashMap} .
     */
    public static <K, V> Map<K, V> newMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> MapList<K, V> newMapList() {
        return new MapList<K, V>();
    }

    /**
     * Constructs and returns a generic {@link HashSet} creating.
     */
    public static <T> Set<T> newSet() {
        return new HashSet<T>();
    }

    /**
     * Contructs a new {@link HashSet} and initializes it using the provided collection.
     */
    public static <T, V extends T> Set<T> newSet(Collection<V> values) {
        return new HashSet<T>(values);
    }
    
    public static <T, V extends T> Set<T> newSet(V... values) {
        // Was a call to newSet(), but Sun JDK can't handle that. Fucking generics.
        return new HashSet<T>(Arrays.asList(values));
    }

    /**
     * Constructs a new {@link HashMap} creating by copying an existing Map creating.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <K, V> Map<K, V> newMap(Map<? extends K, ? extends V> map) {
        Map ret = new HashMap<K, V>(map.size());
        ret.putAll(map);
        return ret;
    }

    /**
     * Constructs a new concurrent map, which is safe to access via multiple threads.
     */
    public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
        return new ConcurrentHashMap<K, V>();
    }

    /**
     * Contructs and returns a new generic {@link ArrayList} creating.
     */
    public static <T> List<T> newList() {
        return new ArrayList<T>();    
    }

    /**
     * Creates a new, fully modifiable list from an initial set of elements.
     */
    public static <T, V extends T> List<T> newList(V... elements) {
        // Was call to newList(), but Sun JDK can't handle that.
        return new ArrayList<T>(Arrays.asList(elements));
    }

    /**
     * Useful for queues.
     */
    public static <T> LinkedList<T> newLinkedList() {
        return new LinkedList<T>();
    }

    /**
     * Constructs and returns a new {@link ArrayList} as a copy of the provided collection.
     */
    public static <T, V extends T> List<T> newList(Collection<V> list) {
        return new ArrayList<T>(list);    	
    }

    /**
     * Constructs and returns a new {@link CopyOnWriteArrayList}.
     */
    public static <T> List<T> newThreadSafeList() {
        return new CopyOnWriteArrayList<T>();
    }
}
