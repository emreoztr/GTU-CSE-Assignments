
public interface Queue<E> extends Collection<E>{
    /**
    * This method adds an element to the collection.
    * @param e This is the element which will be added
    * @return boolean returns if it is successfull or not.
    */
    boolean add(E e);

    /**
    * This method returns the head of this collection.
    * @return E returns head of this collection.
    */
    E element();

    /**
    * This method offers an element to the collection.
    * @param e This is the element which will be added
    * @return boolean returns if it is successfull or not.
    */
    boolean offer(E e);

    /**
    * This method returns the head of this collection and removes it.
    * @return E returns head of this collection.
    */
    E pool();
}
