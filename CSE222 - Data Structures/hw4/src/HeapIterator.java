import java.util.Iterator;

public interface HeapIterator<E> extends Iterator<E>{
    /**
     * Sets the last element returned from the list
     * @param e New element that will replace the returned element
     * @return Returns the old element
     */
    public E set(E e);
}
