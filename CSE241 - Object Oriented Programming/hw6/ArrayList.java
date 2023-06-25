public class ArrayList<E> implements List<E>, Cloneable {
    /**
    * This is the default constructor.
    * It creates the collection with 15 standard capacity.
    * @exception CollectionInvalidSize On input error.
    * @see CollectionInvalidSize
    */
    public ArrayList() throws CollectionInvalidSize{
        this(15);
    }

    /**
    * This constructor takes the capacity for this collection.
    * @exception CollectionInvalidSize On input error.
    * @see CollectionInvalidSize
    */
    public ArrayList(int size) throws CollectionInvalidSize{
        if (size <= 0)
            throw (new CollectionInvalidSize());
        _cap = size;
        _size = 0;
        _container = new Object[_cap];
        _containerHolder = new Object[1][];
        _containerHolder[0] = _container;
    }
    
    /**
   * This method creates a new iterator for user.
   * @return Iterator<E> returns a generic iterator for this object.
   */
    public Iterator<E> iterator() {
        _iter = new Iterator<E>(this, _containerHolder);
        return _iter;
    }

    /**
    * This method adds an element to the collection.
    * @param e This is the element which will be added
    * @return boolean returns if it is successfull or not.
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    */
    public boolean add(E e) throws CollectionNullElement{
        if (e != null) {
            ++_size;
            resize();
            _container[size() - 1] = e;
            return true;
        } else
            throw (new CollectionNullElement());
    }
    
    /**
    * This method checks if the element is in the collection or not.
    * @param e This is the element which will be checked
    * @return boolean returns if element is in the collection or not.
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    */
    public boolean contains(E e) throws CollectionNullElement{
        if (e == null) {
            throw (new CollectionNullElement());
        }
        Iterator<E> iter = iterator();

        while (iter.hasNext())
            if (iter.next().equals(e))
                return true;
        return false;
        //else throw exception
    }
    
    /**
    * This method checks if the collection is empty or not.
    * @return boolean returns true if collection is empty.
    */
    public boolean isEmpty() {
        if (size() == 0)
            return true;
        else
            return false;
    }

    /**
    * This method removes the element from collection.
    * @param e This is the element which will be removed
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    */
    public void remove(E e) throws CollectionNullElement{
        boolean found = false;
        if (e == null) {
            for (int i = 0; i < size() && found == false; ++i) {
                if (_container[i] == null) {
                    shiftLeft(i);
                    found = true;
                }
            }
            if (found == false) {
                throw (new CollectionNullElement());
            }
        } else if (e != null) {
            for (int i = 0; i < size() && found == false; ++i) {
                if (_container[i].equals(e)) {
                    shiftLeft(i);
                    found = true;
                }
            }
        }

        if (found == true)
            --_size;
    }
    
    /**
    * This method sends the size information to user.
    * @return int returns the used size of the collection.
    */
    public int size() {
        return _size;
    }

    /**
    * This method add all elements to this collection.
    * @param c This is the collection which will be added
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    * @exception CollectionSameCollection If user sends the collection's itself.
    * @see CollectionSameCollection
    */
    public void addAll(Collection<E> c) throws CollectionNullElement, CollectionSameCollection{
        if (c == null)
            throw (new CollectionNullElement());
        else if(c == this)
            throw (new CollectionSameCollection());

        Iterator<E> iter = c.iterator();
        while (iter.hasNext())
            add(iter.next());
    }

    /**
    * This method clears all the used data.
    */
    public void clear() {
        _size = 0;
    }

    /**
    * This method checks if  this collection has all the elements from the collection passed by the user.
    * @param c This is the collection which will be checked
    * @return boolean returns true if this collection has all the elements from passed collection.
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    */
    public boolean containsAll(Collection<E> c) throws CollectionNullElement{
        if (c == null)
            throw (new CollectionNullElement());
        boolean returnVal = true;
        Iterator<E> iter_other = c.iterator();

        while (iter_other.hasNext() && returnVal == true) {
            if (!contains(iter_other.next()))
                returnVal = false;
        }

        return returnVal;
    }

    /**
    * This method removes all the elements that are same with passed collection.
    * @param c This is the collection which will be checked
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    * @exception CollectionSameCollection If user sends the collection's itself.
    * @see CollectionSameCollection
    */
    public void removeAll(Collection<E> c) throws CollectionNullElement, CollectionSameCollection{
        if (c == null)
            throw (new CollectionNullElement());
        else if(c == this)
            throw (new CollectionSameCollection());
        Iterator<E> iter = iterator();

        while (iter.hasNext()) {
            if (c.contains(iter.next())) {
                try{
                    iter.remove();
                }
                catch(CollectionNotRandomAccess a){}
            }
        }
    }
    
    /**
    * This method retains all the elements that are same with passed collection.
    * @param c This is the collection which will be checked
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    * @exception CollectionSameCollection If user sends the collection's itself.
    * @see CollectionSameCollection
    */
    public void retainAll(Collection<E> c) throws CollectionNullElement, CollectionSameCollection {
        if (c == null)
            throw (new CollectionNullElement());
        else if (c == this)
            throw (new CollectionSameCollection());
        Iterator<E> iter = iterator();

        while (iter.hasNext()) {
            if (!c.contains(iter.next())) {
                try {
                    iter.remove();
                } catch (CollectionNotRandomAccess a) {
                }
            }
        }
    }

    /**
    * This method using for getting the data from collection.
    * @param index This is the index of the collection that will be returned
    * @return E returns the value of that index.
    * @exception IllegalArgumentException If user sends non valid index.
    * @see IllegalArgumentException
    */
    public E get(int index) throws IllegalArgumentException{
        if(index >= size() || index < 0)
            throw (new IllegalArgumentException());
        @SuppressWarnings("unchecked")
        E returnVal = (E) _container[index];
        return returnVal;
    }


    /**
    * This method using for setting the data in collection.
    * @param index This is the index of the collection that will be set
    * @param val This is the value which will be put to the index passed by user
    * @exception IllegalArgumentException If user sends non valid index.
    * @see IllegalArgumentException
    */
    public void set(int index, E val) throws IllegalArgumentException{
        if(index >= size() || index < 0)
            throw (new IllegalArgumentException());
        _container[index] = (Object) val;
    }
    
    /**
    * This method using while printing this class' objects.
    * @return String returns the string version of this collection.
    * @exception CollectionNullElement If user sends null element.
    * @see CollectionNullElement
    * @exception CollectionSameCollection If user sends the collection's itself.
    * @see CollectionSameCollection
    */
    public String toString() {
        String returnVal = new String();

        if (size() > 0)
            returnVal += _container[0];
        for (int i = 1; i < size(); ++i) {
            returnVal += (", " + _container[i]);
        }
        return returnVal;
    }


    /**
    * This method checks the equality with another object.
    * @param obj This is the object that will be checked
    * @return boolean returns true if data inside of two objects are same.
    */
    public boolean equals(Object obj) {
        if (obj instanceof Collection<?>) {
            @SuppressWarnings("unchecked")
            Collection<E> temp = (Collection<E>) obj;
            if (temp.containsAll(this) && this.containsAll(temp))
                return true;
            else
                return false;
        } else
            return false;
    }
    
    /**
    * This method creates shallow copy of this collection.
    * @return Object returns the copy of this collection.
    * @exception CloneNotSupportedException In case of clone not succesful.
    * @see CloneNotSupportedException
    */
    public Object clone() throws CloneNotSupportedException{
        try{
            @SuppressWarnings("unchecked")
            ArrayList<E> cloneArrList = (ArrayList<E>) super.clone();
            cloneArrList._containerHolder = new Object[1][];
            cloneArrList._container = new Object[_cap];
            cloneArrList._containerHolder[0] = cloneArrList._container;
            for (int i = 0; i < _cap; ++i) {
                cloneArrList._container[i] = _container[i];
            }
            return ((Object) cloneArrList);
        } catch (CloneNotSupportedException ex) {
            throw ex;
        } 
    }

    private void shiftLeft(int index) {
        if (index >= 0 && index < size()) {
            for (int i = index; i < size() - 1; ++i) {
                _container[i] = _container[i + 1];
            }
            _container[size() - 1] = null;
        }
    }

    private boolean resize() {
        boolean returnVal = false;
        if (size() > getCap()) {
            Object[] temp = new Object[_cap+15];
            for (int i = 0; i<getCap(); ++i)
                temp[i] = _container[i];
            _cap += 15;
            _container = temp;
            returnVal = true;
        }
        _containerHolder[0] = _container;

        return returnVal;
    }

    private int getCap() {
        return _cap;
    }

    private int _cap, _size;
    private Iterator<E> _iter;
    private Object[] _container;
    private Object[][] _containerHolder;
}
