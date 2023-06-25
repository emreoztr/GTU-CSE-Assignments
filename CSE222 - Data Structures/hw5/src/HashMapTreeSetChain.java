import java.util.TreeSet;

public class HashMapTreeSetChain<K extends Comparable<K>, V> implements KWHashMap<K, V>{

    private TreeSet<Entry<K, V>>[] arr;
    private static final int INITIAL_CAPACITY = 11;
    private static final double LOAD_THRESHOLD = 5.0;
    private int size = 0;
    private int cap = INITIAL_CAPACITY;

    public HashMapTreeSetChain(){
        @SuppressWarnings("unchecked")
        TreeSet<Entry<K, V>>[] temp = new TreeSet[cap];

        arr = temp;
    }

    @SuppressWarnings("unchecked")
    private void rehash(){
        TreeSet<Entry<K, V>>[] temp = arr;

        arr = new TreeSet[2*cap + 1];
        cap = 2*cap + 1;
        size = 0;
        
        for(int i = 0; i < temp.length; ++i){
            if(temp[i] != null)
                for(Entry<K,V> e : temp[i])
                    put(e.getKey(), e.getValue());
        }
    }

    private static class Entry<K extends Comparable<K>, V> implements Comparable<Entry<K, V>>{
        private K key;
        private V val;

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

        public void setValue(V v){
            val = v;
        }

        @Override
        public int compareTo(Entry<K, V> o) throws NullPointerException{
            return getKey().compareTo(o.getKey());
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof Entry))
                return false;

            @SuppressWarnings("unchecked")
            Entry<K,V> ent = (Entry<K,V>)o;
            return this.getKey().compareTo(ent.getKey()) == 0;
        }
    }

    private Entry<K,V> findEntry(K key){
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;
        if(arr[ind] == null)
            return null;
        
        Entry<K,V> checkTool = new Entry<>(key, null);
        Entry<K,V> check = arr[ind].ceiling(checkTool);

        if(check != null && check.equals(checkTool))
            return check;
            
        return null;
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
    public V put(K key, V value) {
        if(key == null || value == null)
            throw new NullPointerException();
        
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;

        Entry<K,V> ent = null;

        if(arr[ind] == null)
            arr[ind] = new TreeSet<>();
        else
            ent = findEntry(key);

        if(ent != null){
            V returnVal = ent.getValue();
            ent.setValue(value);
            return returnVal;
        }

        size++;
        arr[ind].add(new Entry<>(key, value));
        if((size() / (double)cap) > LOAD_THRESHOLD)
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
        
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;
        
        if(arr[ind] == null)
            return null;
        
        @SuppressWarnings("unchecked")
        Entry<K,V> ent = findEntry((K)key);
        
        if(ent != null){
            size--;
            V returnVal = ent.getValue();
            arr[ind].remove(ent);
            if(arr[ind].isEmpty())
                arr[ind] = null;
            return returnVal;    
        }

        return null;
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
            strBuild.append(i).append("  ");
            if(arr[i] == null)
                strBuild.append("    null     null\n");
                else{
                    for(Entry<K,V> e : arr[i])
                        strBuild.append(e.getKey() + ",");
                    strBuild.append("\n");
                }
        }
        return strBuild.toString();
    }
}
