public interface MapIter<K> {
    /**
     * To find next key in the map, if there is no keys which is not iterated it will give the first key.
     * @return returns the next key in the map
     */
    public K next();
    
    /**
     * To find previous key in the map, if it is at the beginning this method will return last key.
     * @return returns the previous key in the map
     */
    public K prev();
    
    /**
     * To know there is not iterated keys in the map or not, if there is not it returns false.
     * @return returns there is not iterated keys in the map or not.
     */
    public boolean hasNext();
}
