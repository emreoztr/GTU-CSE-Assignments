
public interface Collection<E> {
    /**
   * This method creates a new iterator for user.
   * @return Iterator<E> returns a generic iterator for this object.
   */
    Iterator<E> iterator();

    /**
    * This method adds an element to the collection.
    * @param e This is the element which will be added
    * @return boolean returns if it is successfull or not.
    */
    boolean add(E e);

    /**
    * This method add all elements to this collection.
    * @param c This is the collection which will be added
    */
    void addAll(Collection<E> c);

    /**
    * This method clears all the used data.
    */
    void clear();

    /**
    * This method checks if the element is in the collection or not.
    * @param e This is the element which will be checked
    * @return boolean returns if element is in the collection or not.
    */
    boolean contains(E e);

    /**
    * This method checks if  this collection has all the elements from the collection passed by the user.
    * @param c This is the collection which will be checked
    * @return boolean returns true if this collection has all the elements from passed collection.
    */
    boolean containsAll(Collection<E> c);

    /**
    * This method checks if the collection is empty or not.
    * @return boolean returns true if collection is empty.
    */
    boolean isEmpty();

    /**
    * This method removes the element from collection.
    * @param e This is the element which will be removed
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    */
    void remove(E e);

    /**
    * This method removes all the elements that are same with passed collection.
    * @param c This is the collection which will be checked
    */
    void removeAll(Collection<E> c);

    /**
    * This method retains all the elements that are same with passed collection.
    * @param c This is the collection which will be checked
    */
    void retainAll(Collection<E> c);

    /**
    * This method sends the size information to user.
    * @return int returns the used size of the collection.
    */
    int size();
}
