import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class HeapPartOne<E extends Comparable<E> > implements Iterable<E>, Serializable, Comparable<HeapPartOne<E> >{
    private PriorityQueue<E> heapData;

    public HeapPartOne(){
        heapData = new PriorityQueue<>();
    }

    public HeapPartOne(int cap){
        heapData = new PriorityQueue<>(cap);
    }

    public HeapPartOne(Comparator<? super E> comparator){
        heapData = new PriorityQueue<>(comparator);
    }

    public HeapPartOne(HeapPartOne<E> other){
        this(other.heapData.comparator());

        HeapIterator<E> iter = other.heapIterator();
        while(iter.hasNext())
            heapData.add(iter.next());
    }

    /**
     * Searching for given object in heap
     * @param o object that will be searched
     * @return  boolean if heap does have the equal object
     */
    public boolean search(Object o){
        HeapIterator<E> iter = heapIterator();

        while(iter.hasNext())
            if(iter.next().equals(o))
                return true;

        return false;
    }

    /**
     * Merges two heaps, it only merges heaps if there is no same element
     * @param other heap that will be merged with caller heap
     * @return  returns true if merge is successful, if there is same element it return false
     */
    public boolean merge(HeapPartOne<E> other){
        HeapIterator<E> iter = other.heapIterator();
        while(iter.hasNext()){
            E item = iter.next();
            try{
                offer(item);
            }catch(Exception e){
                return false;
            }
        }
        return true;
    }

    /**
     * Removes index which represents the index of largest elements
     * @param i index of which largest element
     * @return  returns true if remove is successful
     */
    public boolean removeBiggest(int i){
        try{
        Object[] arr = toArray();
        Arrays.sort(arr);
        remove(arr[size() - 1 - i]);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    private class HeapIter implements HeapIterator<E>{
        private Iterator<E> innerIter;
        private int count = 0;
        private E lastItemReturned = null;

        public HeapIter(){
            innerIter = heapData.iterator();
        }

        @Override
        public boolean hasNext() {
            return innerIter.hasNext();
        }

        @Override
        public E next() throws NoSuchElementException{
            lastItemReturned = innerIter.next();
            count++;
            return lastItemReturned;
        }

        @Override
        public void remove() throws UnsupportedOperationException, IllegalStateException{
            innerIter.remove();
        }

        @Override
        public E set(E element) throws UnsupportedOperationException, IllegalStateException{
            if(lastItemReturned == null)
                return null;
            remove();

            if(heapData.add(element)) {
                innerIter = heapData.iterator();
                for(int i = 0; i < count; ++i){
                    innerIter.next();
                }
                E temp = lastItemReturned;
                lastItemReturned = null;
                return temp;
            }
            else
                return null;
        }
        
    }

    /**
     * Gives the size of the heap.
     * @return returns the size as int
     */
    public int size() {
        return heapData.size();
    }

    /**
     * Checks if heap is empty or not
     * @return return true if heap is empty
     */
    public boolean isEmpty() {
        return heapData.isEmpty();
    }

    /**
     * Checks if heap contains given object
     * @param o object that will be checked
     * @return returns true if heap contains that object
     */
    public boolean contains(Object o) {
        return heapData.contains(o);
    }

    /**
     * To take iterator of the heap.
     * @return returns the iterator
     */
    public Iterator<E> iterator() {
        return heapData.iterator();
    }

    /**
     * To take HeapIterator of the heap
     * @return returns the HeapIterator
     */
    public HeapIterator<E> heapIterator() {
        return new HeapIter();
    }

    /**
     * Creates an array which represents elements in the heap.
     * @return returns the array.
     */
    public Object[] toArray() {
        return heapData.toArray();
    }

    /**
     * Removes the element.
     * @param o Object that will be removed
     * @return returns true if remove is successful.
     */
    public boolean remove(Object o) {
        return heapData.remove(o);
    }

    /**
     * Adds given element.
     * @param e Element that will be added to the heap.
     * @return return true if add is successful
     */
    public boolean add(E e) {
        return heapData.add(e);
    }

    /**
     * Offers element to the heap.
     * @param e Element that will be offered to the heap
     * @return returns true of insertion is successful
     * @throws NullPointerException
     * @throws ClassCastException
     */
    public boolean offer(E e) throws NullPointerException, ClassCastException{
        return heapData.offer(e);
    }

    /**
     * Removes the peek element of the heap
     * @return returns the element removed
     */
    public E remove() {
        return heapData.remove();
    }

    /**
     * Polls the element of the heap
     * @return return the element polled
     */
    public E poll() {
        return heapData.poll();
    }

    /**
     * Retrieves peek element of this heap
     * @return returns the peek element
     */
    public E element() {
        return heapData.element();
    }

    /**
     * Retrieves peek element of this heap
     * @return returns the peek element
     */
    public E peek() {
        return heapData.peek();
    }

    /**
     * Compares this heap with another heap with their peek values
     * @param o Heap that will be compared
     * @return returns positive if peek is bigger, negative if peek is smaller, 0 if their peeks are equal
     */
    @Override
    public int compareTo(HeapPartOne<E> o) {
        if(!(o instanceof HeapPartOne))
            throw new ClassCastException();
        
        @SuppressWarnings("unchecked")
        HeapPartOne<E> other = (HeapPartOne<E>) o;
        if(this == o)
            return 0;

        return this.peek().compareTo(other.peek());
    }

    /**
     * Retrieves String representation of the hepa.
     * @return returns String representation of the heap.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        for(E node: heapData){
            strBuild.append(node).append("-");
        }
        return strBuild.toString();
    }
}