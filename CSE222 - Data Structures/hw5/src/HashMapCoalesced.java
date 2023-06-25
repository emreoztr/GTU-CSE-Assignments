public class HashMapCoalesced<K,V> implements KWHashMap<K,V> {

    private Entry<K, V>[] arr;
    private static final int INITIAL_CAPACITY = 11;
    private static final double LOAD_THRESHOLD = 0.5;
    private int size = 0;
    private int cap = INITIAL_CAPACITY;

    @SuppressWarnings("unchecked")
    public HashMapCoalesced(){
        this.arr = new Entry[cap];
    }

    private static class Entry<K, V>{
        private K key;
        private V val;
        private Integer next = null;

        public Entry(K k, V v){
            key = k;
            val = v;
        }

        public K getKey(){
            return key;
        }

        public V getValue(){
            return val;
        }

        public V setValue(V v){
            V old = val;
            val = v;
            return old;
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof Entry))
                return false;

            @SuppressWarnings("unchecked")
            Entry<K,V> ent = (Entry<K,V>)o;

            return this.getKey().equals(ent.getKey());
        }
    }

    private Entry<K,V> findEntry(K key){
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;

        Entry<K, V> check = arr[ind];
        Entry<K, V> checkTool = new Entry<>(key, null);
        while(check != null && !(check.equals(checkTool))){
            if(check.next != null)
                check = arr[check.next];
            else
                check = null;
        }
        return check;
    }

    @SuppressWarnings("unchecked")
    private void rehash(){
        Entry<K,V>[] tempArr = arr;
        arr = new Entry[2*cap + 1];
        cap = (2*cap) + 1;
        size = 0;

        for(int i = 0; i < tempArr.length; ++i){
            if(tempArr[i] != null)
                put(tempArr[i].getKey(), tempArr[i].getValue());
        }
    }


    private Entry<K,V> findLastPointingEntry(K key){
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;
        
        Entry<K, V> check = arr[ind];
        if(check == null)
            return null;
        while(check.next != null)
            check=arr[check.next];
        return check;
    }

    /**
     * To get the value with specified key, if there is not it will return null
     * @param key key that will be searched
     * @return returns value if there is, if not it returns null
     */
    @Override
    public V get(Object key) throws NullPointerException{
        if(key == null)
            throw new NullPointerException();

        @SuppressWarnings("unchecked")
        Entry<K,V> ent = findEntry((K)key);

        if(ent != null)
            return ent.getValue();
        return null;
    }

    /**
     * To get the hashmap is empty or not
     * @return returns true if map is empty if not it returns false
     */
    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    /**
     * Puts the value with specified key in the map, if map has same key it swaps the value part only
     * @param key key that will be used in map
     * @param value value that will be saved to the map
     * @return returns old value if swap happens if not it returns null
     */
    @Override
    public V put(K key, V value) throws NullPointerException{
        if(key == null || value == null)
            throw new NullPointerException();
        
        Entry<K,V> check = findEntry(key);
        if(check != null){
            V returnVal = check.getValue();
            check.setValue(value);
            return returnVal;
        }
        
        size++;
        int k = -1;
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;
        while(arr[ind] != null){
            k+=2;
            ind = (ind + k) % cap;
        }
        check = findLastPointingEntry(key);
        arr[ind] = new Entry<>(key, value);
        if(check != null)
            check.next = ind;
        if((size() / (double) cap) > LOAD_THRESHOLD)
            rehash();
        return null;
    }

    /**
     * Removes value with given key
     * @param key key that will be searched
     * @return it returns removed value if program finds key in the map, if not it returns null
     */
    @Override
    public V remove(Object key) throws NullPointerException{
        if(key == null)
            throw new NullPointerException();
        
        @SuppressWarnings("unchecked")
        Entry<K,V> checkTool = new Entry<>((K)key, null);

        V returnVal = null;
        
        
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;
        Entry<K,V> check = arr[ind];

        int prevIndex = -1;
        int currIndex = ind;

        while(check != null && !check.equals(checkTool)){
            if(check.next == null)
                check = null;
            else{
                prevIndex = currIndex;
                currIndex = check.next;
                check = arr[check.next];
            }
        }
        if(check == null)
            return null;

        size--;
        returnVal = arr[currIndex].getValue();
        if(arr[currIndex].next != null){
            int nextItem = arr[currIndex].next;
            arr[currIndex] = arr[nextItem];
            arr[nextItem] = null;
        }else{
            arr[currIndex] = null;
            if(prevIndex != -1)
                arr[prevIndex].next = null;
        }
        return returnVal;
    }

    /**
     * To get size in integer type
     * @return returns the size of the map
     */
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("hash val       key     next\n");
        for(int i = 0; i < cap; ++i){
            
            if(arr[i] != null){
                strBuild.append(i);
                strBuild.append("    " + arr[i].getKey() + "      " + arr[i].getValue() + "      " + arr[i].next + "\n");
            }
        }
        return strBuild.toString();
    }
}
