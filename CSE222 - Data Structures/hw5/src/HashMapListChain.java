import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class HashMapListChain<K, V> implements KWHashMap<K, V> {

    private LinkedList<Entry<K, V>>[] arr;
    private static final int INITIAL_CAPACITY = 11;
    private static final double LOAD_THRESHOLD = 3.0;
    private int size = 0;
    private int cap = INITIAL_CAPACITY;

    public HashMapListChain(){
        @SuppressWarnings("unchecked")
        LinkedList<Entry<K, V>>[] temp = new LinkedList[cap];

        arr = temp;
    }

    @SuppressWarnings("unchecked")
    private void rehash(){
        LinkedList<Entry<K, V>>[] temp = arr;

        arr = new LinkedList[2*cap + 1];
        cap = 2*cap + 1;
        size = 0;
        
        for(int i = 0; i < temp.length; ++i){
            if(temp[i] != null)
                for(Entry<K,V> e : temp[i])
                    put(e.getKey(), e.getValue());
        }
    }

    private static class Entry<K, V>{
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
    }

    /**
     * To get the value with specified key, if there is not it will return null
     * @param key key that will be searched
     * @return returns value if there is, if not it returns null
     */
    @Override
    public V get(Object key) {
        if(key == null)
            return null;
        
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;

        if(arr[ind] == null)
            return null;

        for(Entry<K, V> ent : arr[ind]){
            if(ent.getKey().equals(key))
                return ent.getValue();
        }

        return null;
    }

    /**
     * To get the hashmap is empty or not
     * @return returns true if map is empty if not it returns false
     */
    @Override
    public boolean isEmpty() {
        boolean returnVal = false;
        if(size() == 0)
            returnVal = true;
        return returnVal;
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
        
        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;

        if(arr[ind] == null)
            arr[ind] = new LinkedList<>();
        
        V returnVal = null;
        Iterator<Entry<K, V>> iter = arr[ind].iterator();

        while(iter.hasNext()){
            Entry<K, V> e = iter.next();
            if(e.getKey().equals(key)){
                returnVal = e.getValue();
                e.setValue(value);
                return returnVal;
            }
        }
        
        size++;
        arr[ind].add(new Entry<>(key, value));

        if(((double)size / (double)cap) > LOAD_THRESHOLD)
            rehash();

        return null;
    }

    /**
     * Removes value with given key
     * @param key key that will be searched
     * @return it returns removed value if program finds key in the map, if not it returns null
     */
    @Override
    public V remove(Object key) throws NoSuchElementException{
        if(key == null)
            throw new NoSuchElementException();

        int ind = key.hashCode() % cap;
        if(ind < 0)
            ind += cap;

        if(arr[ind] == null)
            return null;
        
        V returnVal = null;
        Iterator<Entry<K, V>> iter = arr[ind].iterator();

        while(iter.hasNext()){
            Entry<K, V> e = iter.next();
            if(e.getKey().equals(key)){
                returnVal = e.getValue();
                iter.remove();
                size--;
            }
        }

        if(arr[ind].isEmpty())
            arr[ind] = null;

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
