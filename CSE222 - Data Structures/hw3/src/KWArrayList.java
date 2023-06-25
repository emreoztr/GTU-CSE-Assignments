import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class KWArrayList<E> extends AbstractList<E> implements Serializable {
    // Data Fields
    /** The default initial capacity */
    private static final int INITIAL_CAPACITY = 10;
    /** The underlying data array */
    private E[] theData;
    /** The current size */
    private int size = 0;
    /** The current capacity */
    private int capacity = 0;

    private void reallocate() {
        capacity = 2 * capacity;
        theData = Arrays.copyOf(theData, capacity);
    }

    public KWArrayList() {
        this(INITIAL_CAPACITY);
    }

    public KWArrayList(E obj) {
        this();
        theData[0] = obj;
        ++size;
    }

    public KWArrayList(int cap) {
        if(cap <= 0)
            throw new IllegalArgumentException();
        else{
            capacity = cap;
            @SuppressWarnings("unchecked")
            E[] tempData = (E[]) new Object[capacity];
            theData = tempData;
        }
    }

    public boolean add(E anEntry) {
        if (size == capacity) 
            reallocate();
        
        theData[size] = anEntry;
        size++;
        return true;
    }

    public void add(int index, E anEntry) {
        if (index < 0 || index > size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        if (size == capacity)
            reallocate();
        
        // Shift data in elements from index to size ‐ 1
        for (int i = size; i > index; i--)
            theData[i] = theData[i - 1];
        
        // Insert the new item.
        theData[index] = anEntry;
        size++;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return theData[index];
    }

    public E set(int index, E newValue) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        E oldValue = theData[index];
        theData[index] = newValue;
        return oldValue;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new ArrayIndexOutOfBoundsException(index);
        
        E returnValue = theData[index];

        for (int i = index + 1; i < size; i++)
            theData[i - 1] = theData[i];
        
        size--;
        return returnValue;
    }

    public int size(){
        return size;
    }
}

