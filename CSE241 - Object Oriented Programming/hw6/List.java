
public interface List<E> extends Collection<E> {
    /**
    * This method using for getting the data from collection.
    * @param index This is the index of the collection that will be returned
    * @return E returns the value of that index.
    */
    E get(int index);

    /**
    * This method using for setting the data in collection.
    * @param index This is the index of the collection that will be set
    * @param val This is the value which will be put to the index passed by user
    */
    void set(int index, E val);
}
