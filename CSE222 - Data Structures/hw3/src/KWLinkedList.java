import java.io.Serializable;
import java.util.AbstractList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class KWLinkedList<E> extends AbstractList<E> implements Serializable {
    // Data Fields
    /** A reference to the head of the list. */
    private Node<E> head = null;
    /** A reference to the end of the list. */
    private Node<E> tail = null;
    /** The size of the list. */
    private int size = 0;

    private static class Node<E> {
        /** The data value. */
        private E data;
        /** The link to the next node. */
        private Node<E> next = null;
        /** The link to the previous node. */
        private Node<E> prev = null;
        /** Construct a node with the given data value.
        @param dataItem The data value
        */
        private Node(E dataItem) {
            data = dataItem;
        }
    }

    /** Inner class to implement the ListIterator interface. */
    private class KWListIter implements ListIterator<E> {
        /** A reference to the next item. */
        private Node<E> nextItem;
        /** A reference to the last item returned. */
        private Node<E> lastItemReturned;
        /** The index of the current item. */
        private int index = 0;
        /** Construct a KWListIter that will reference the ith item.
         @param i The index of the item to be referenced
         */
        public KWListIter(int i) {
            // Validate i parameter.
            if (i < 0 || i > size) {
                throw new IndexOutOfBoundsException("Invalid index " + i);
            }
            lastItemReturned = null; // No item returned yet.
            // Special case of last item.
            if (i == size) {
                index = size;
                nextItem = null;
            } else { // Start at the beginning
                nextItem = head;
                for (index = 0; index < i; index++) {
                    nextItem = nextItem.next;
                }
            }
        }
        /** Indicate whether movement forward is defined.
         @return true if call to next will not throw an exception
         */
        public boolean hasNext() {
            return nextItem != null;
        }
        /** Move the iterator forward and return the next item.
         @return The next item in the list
         @throws NoSuchElementException if there is no such object
         */
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastItemReturned = nextItem;
            nextItem = nextItem.next;
            index++;
            return lastItemReturned.data;
        }
        /** Indicate whether movement backward is defined.
         @return true if call to previous will not throw an exception
         */
        public boolean hasPrevious() {

            return (nextItem == null && size != 0)
                    || (nextItem != null && nextItem.prev != null);
        }
        /** Move the iterator backward and return the previous item.
         @return The previous item in the list
         @throws NoSuchElementException if there is no such object
         */
        public E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            if (nextItem == null) { // Iterator is past the last element
                nextItem = tail;
            } else {
                nextItem = nextItem.prev;
            }
            lastItemReturned = nextItem;
            index--;
            return lastItemReturned.data;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public int previousIndex() {
            return index-1;
        }

        @Override
        public void remove() {
            if(lastItemReturned == head){
                if(lastItemReturned == tail){
                    tail = null;
                    head = null;
                }
                else{
                    head = lastItemReturned.next;
                    head.prev = null;
                }
            }else if(lastItemReturned == tail){
                tail = lastItemReturned.prev;
                tail.next = null;
            }else{
                lastItemReturned.next.prev = lastItemReturned.prev;
                lastItemReturned.prev.next = lastItemReturned.next;
            }
            lastItemReturned = null;
            size--;
        }

        @Override
        public void set(E e) {

        }

        /** Add a new item between the item that will be returned
         by next and the item that will be returned by previous.
         If previous is called after add, the element added is
         returned.
         @param obj The item to be inserted
         */
        public void add(E obj) {
            if (head == null) { // Add to an empty list.
                head = new Node<>(obj);
                tail = head;
            } else if (nextItem == head) { // Insert at head.
                // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link it to the nextItem.
                newNode.next = nextItem;  // Step 1
                // Link nextItem to the new node.
                nextItem.prev = newNode;  // Step 2
                // The new node is now the head.
                head = newNode; // Step 3
            } else if (nextItem == null) { // Insert at tail.
                // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link the tail to the new node.
                tail.next = newNode; // Step 1
                // Link the new node to the tail.
                newNode.prev = tail; // Step 2
                // The new node is the new tail.
                tail = newNode; // Step 3
            } else { // Insert into the middle.
                // Create a new node.
                Node<E> newNode = new Node<>(obj);
                // Link it to nextItem.prev.
                newNode.prev = nextItem.prev; // Step 1
                nextItem.prev.next = newNode; // Step 2
                // Link it to the nextItem.
                newNode.next = nextItem; // Step 3
                nextItem.prev = newNode; // Step 4
            }
            // Increase size and index and set lastItemReturned.
            size++;
            index++;
            lastItemReturned = null;
        } // End of method add.
    }

    public ListIterator<E> listIterator(int index){
        return new KWListIter(index);
    }

    public ListIterator<E> listIterator(){
        return listIterator(0);
    }

    public void addFirst(E obj){
        Node<E> newNode = new Node<>(obj);
        newNode.next = head;
        if(head != null)
            head.prev = newNode;
        else
            tail = newNode;
        head = newNode;
    }

    public void addLast(E obj){
        Node<E> newNode = new Node<>(obj);
        newNode.prev = tail;
        if(tail != null)
            tail.next = newNode;
        else
            head = newNode;
        tail = newNode;
    }

    public E getFirst(){
        return head.data;
    }

    public E getLast(){
        return tail.data;
    }

    /** Add an item at position index.
    @param index The position at which the object is to beinserted
    @param obj The object to be inserted
    @throws IndexOutOfBoundsException if the index is out
    of range (i < 0 || i > size())
    */
    public void add(int index, E obj) {
        listIterator(index).add(obj);
    }

    /** Get the element at position index.
    @param index Position of item to be retrieved
    @return The item at index
    */
    public E get(int index) {
        return listIterator(index).next();
    }

    public int size(){
        return size;
    }
}
