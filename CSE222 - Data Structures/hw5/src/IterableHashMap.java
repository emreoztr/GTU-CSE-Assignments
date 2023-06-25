import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class IterableHashMap<K, V> extends HashMap<K, V>{

    private class MapIterator implements MapIter<K>{
        /*FIELDS*/
        //keyset inside of the map
        private Set<K> keySet = keySet();

        //this stack will save the returned key by the prev() to access them later if user call next()
        private ArrayDeque<K> remainNext = new ArrayDeque<>();

        //this stack will save the returned key by the next() to access them later if user call prev()
        private ArrayDeque<K> remainPrev = new ArrayDeque<>();

        //this iterator's next() method only will be used if user wants to next() and remain_next is empty
        private Iterator<K> keyIter;

        private K first = null;
        private K last = null;


        /*CONSTRUCTORS*/
        public MapIterator(){
            int count = 0;
            K temp = null;
            for(K k : keySet){
                if(count == 0)
                    first = k;
                count++;
                temp = k;
            }
            
            last = temp;
            keyIter = keySet.iterator();
        }

        //makes iterator starts from the given key
        public MapIterator(K key){
            this();
            if(keySet.contains(key)){
                K tempkey = null;
                do{
                    tempkey = keyIter.next();
                    remainNext.push(tempkey);
                    first = tempkey;
                }while(!tempkey.equals(key));
                
            }
        }



        /*METHODS*/
        @Override
        public K next() {
            if(!remainNext.isEmpty()){
                remainPrev.push(remainNext.pop());
                return remainPrev.peek();
            }

            if(keyIter.hasNext()){
                remainPrev.push(keyIter.next());
                return remainPrev.peek();
            }else
                return first;
        }

        @Override
        public K prev() {
            if(!remainPrev.isEmpty()){
                remainNext.push(remainPrev.pop());
                return remainNext.peek();
            }
            return last;
        }

        @Override
        public boolean hasNext() {
            boolean returnVal = false;
            if(!remainNext.isEmpty() || keyIter.hasNext())
                returnVal = true;

            return returnVal;
        }

    }

    public MapIter<K> iterator() {
        return new MapIterator();
    }

    public MapIter<K> iterator(K k) {
        return new MapIterator(k);
    }
}
