import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Random;
import java.util.SortedSet;

public class SkipListSet<E extends Comparable<E>> implements NavigableSet<E>{

    static final double LOG2 = Math.log(2.0);
    private SLNode<E> head = null;
    private Random rand = new Random();
    private int maxCap = 2;
    private int maxLevel = (int) (Math.log(maxCap) / LOG2);
    private int size = 0;

    private class Iter implements Iterator<E>{
        private ArrayList<E> list;
        private int ind = 0;
        public Iter(){
            list = new ArrayList<>();
            SLNode<E> current = head;
            while(current != null){
                list.add(current.data);
                current = current.links[0];
                ind++;
            }
        }

        @Override
        public boolean hasNext() {
            return ind > 0;
        }

        @Override
        public E next() {
            return list.get(--ind);
        }

    } 

    /** Static class to contain the data and the links */
    public static class SLNode<E> {
        SLNode<E>[] links; 
        E data;
        /** Create a node of level m */
        SLNode (int m, E data) {
            links = (SLNode<E>[]) new SLNode[m]; // create links
            this.data = data; // store item
        }
        
        public String toString(){
            StringBuilder strBuild = new StringBuilder();
            strBuild.append("{");
            for(int i = 0; i < links.length; ++i){
                if(links[i] != null){
                    strBuild.append(links[i].data);
                    strBuild.append(",");
                }
            }
            strBuild.append("}");
            strBuild.append("(" + data + ")");
            return strBuild.toString();
        }
    }

    private int logRandom() {
        int r = rand.nextInt(maxCap);
        int k = (int) (Math.log(r + 1) / LOG2);
        if (k > maxLevel - 1) {
            k = maxLevel - 1;
        }
        return maxLevel - k;
    }

    /**
     * To insert new item to skiplist
     * @param item item will be inserted
     * @return returns insertion result
     */
    public boolean insert(E item){
        if(head == null){
            head = new SLNode<>(maxLevel, item);
            size++;
            return true;
        }
        int newNodeLevel = logRandom();
        SLNode<E> newNode = new SLNode<>(newNodeLevel + 1, item);
        int currLevelIndex = maxLevel - 1;
        SLNode<E>[] pred = (SLNode<E>[]) new SLNode[maxLevel];
        SLNode<E> curr = head;
        
        if(item.compareTo(head.data) < 0){
            for(int i = newNodeLevel - 1; i >= 0; --i){
                newNode.links[i] = head.links[i];
                head.links[i] = newNode;
            }
            E temp = head.data;
            head.data = newNode.data;
            newNode.data = temp;
            size++;
            if(size > maxCap)
                increaseLevel();
            return true;
        }else if(item.compareTo(head.data) == 0){
            return false;
        }

        while(currLevelIndex >= 0){
            if(item.compareTo(curr.data) > 0){
                pred[currLevelIndex] = curr;
                curr = curr.links[currLevelIndex];
            }
            if(curr == null || item.compareTo(curr.data) < 0 ){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
            if(item.compareTo(curr.data) == 0)
                return false;
        }
        size++;
        for(int i = newNodeLevel - 1; i >= 0; --i){
            newNode.links[i] = pred[i].links[i];
            pred[i].links[i] = newNode;
        }

        if(size > maxCap)
            increaseLevel();

        return true;
    }

    private SLNode<E> findNode(E item, SLNode<E>[] pred){
        SLNode<E> returnVal = null;
        if(item.compareTo(head.data) == 0)
            return head;
        else if(item.compareTo(head.data) < 0)
            return null;

        SLNode<E> curr = head;
        int currLevelIndex = maxLevel - 1;
        
        while(currLevelIndex >= 0){
            if(item.compareTo(curr.data) > 0){
                pred[currLevelIndex] = curr;
                curr = curr.links[currLevelIndex];
            }
            if(curr == null || item.compareTo(curr.data) < 0 ){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
            if(item.compareTo(curr.data) == 0 && currLevelIndex == 0)
                return curr;
            else if(item.compareTo(curr.data) == 0){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
        }
        return null; 
    }

    public SLNode<E>[] search (E target){
        SLNode<E>[] pred = new SLNode[maxLevel];
        SLNode<E> curr = head;
        
        for (int i = curr.links.length-1; i >= 0; i--) {
            while (curr.links[i] != null && curr.links[i].data.compareTo(target) < 0) {
                curr = curr.links[i];
            }

            pred[i] = curr;
        }
        
        return pred;
    }


    /**
     * To delete item from skiplistset
     * @param item item will be deleted
     * @return returns insertion result
     */
    @Override
    public boolean remove(Object o) {
        try{
            @SuppressWarnings("unchecked")
            E item = (E) o;
            SLNode<E>[] pred = (SLNode<E>[]) new SLNode[maxLevel];
            if(head == null) return false;
            SLNode<E> found = findNode(item, pred);
            if(found == null)
                return false;
            size--;

            if(found == head){
                SLNode<E> node = head.links[0];
                if(node == null){
                    head = null;
                    return true;
                }
                int oldLev = node.links.length;
                node.links = Arrays.copyOf(node.links, maxLevel);
                for(int i = oldLev; i < maxLevel; ++i)
                    node.links[i] = head.links[i];
                head = node;
                return true;
            }
            for(int i = pred.length - 1; i >= 0; --i){
                if(pred[i].links[i] == found)
                    pred[i].links[i] = found.links[i];
            }
            return true;
        }catch(ClassCastException e){
            System.out.println("err");
            return false;
        }
    }

    private int calcMaxCap(int level){
        return (int) Math.pow(2, level) - 1;
    }

    private void increaseLevel(){
        maxLevel++;
        maxCap = calcMaxCap(maxLevel);
        head.links = Arrays.copyOf(head.links, maxLevel);
    }

    /**
     * To get descending iterator
     * @return returns iterator
     */
    @Override
    public Iterator<E> descendingIterator() {
        return new Iter();
    }

    @Override
    public Comparator<? super E> comparator() {

        return null;
    }

    @Override
    public E first() {

        return null;
    }

    @Override
    public E last() {

        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {

        return false;
    }

    @Override
    public Object[] toArray() {

        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {

        return null;
    }

    /**
     * To insert new item to AVLTreeSet
     * @param item item will be inserted
     * @return returns insertion result
     */
    @Override
    public boolean add(E e) {
        return insert(e);
    }

    

    @Override
    public boolean containsAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {

        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {

        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {

        return false;
    }

    @Override
    public void clear() {

        
    }

    @Override
    public E lower(E e) {

        return null;
    }

    @Override
    public E floor(E e) {

        return null;
    }

    @Override
    public E ceiling(E e) {

        return null;
    }

    @Override
    public E higher(E e) {

        return null;
    }

    @Override
    public E pollFirst() {

        return null;
    }

    @Override
    public E pollLast() {

        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public NavigableSet<E> descendingSet() {

        return null;
    }


    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {

        return null;
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {

        return null;
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {

        return null;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {

        return null;
    }

    @Override
    public SortedSet<E> headSet(E toElement) {

        return null;
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {

        return null;
    }

    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        SLNode<E> temp = head;
        for(int i = maxLevel - 1; i >= 0; --i){
            while(temp != null){
                strBuild.append(temp.data).append(",");
                temp = temp.links[i];
            }
            temp = head;
            strBuild.append("\n-----------------");
        }
        return strBuild.toString();
    }
}