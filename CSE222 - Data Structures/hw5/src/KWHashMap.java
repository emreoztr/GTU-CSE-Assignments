public interface KWHashMap<K, V> {
    /**
     * To get the value with specified key, if there is not it will return null
     * @param key key that will be searched
     * @return returns value if there is, if not it returns null
     */
    public V get(Object key);

    /**
     * To get the hashmap is empty or not
     * @return returns true if map is empty if not it returns false
     */
    public boolean isEmpty();
    
    /**
     * Puts the value with specified key in the map, if map has same key it swaps the value part only
     * @param key key that will be used in map
     * @param value value that will be saved to the map
     * @return returns old value if swap happens if not it returns null
     */
    public V put(K key, V value);

    /**
     * Removes value with given key
     * @param key key that will be searched
     * @return it returns removed value if program finds key in the map, if not it returns null
     */
    public V remove(Object key);

    /**
     * To get size in integer type
     * @return returns the size of the map
     */
    public int size();
}
