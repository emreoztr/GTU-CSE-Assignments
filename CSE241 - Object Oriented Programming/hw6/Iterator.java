
public class Iterator<T> {  
    /**
    * This is the default constructor.
    * @param c This is the itself of caller collection
    * @param e This is the array of caller collection
    */
    public Iterator(Collection<T> c, Object[][] e) {
        _container = c;
        _containerHolder = e;
    }

    /**
    * Return if it is at the end or not.
    * @return boolean returns true if it has elements in front of it
    */
    public boolean hasNext() {
        if (_index + 1 < _container.size())
            return true;
        else
            return false;
    }
    
    /**
    * Returns the next element if it is not at the end.
    * @return T returns next element
    */
    public T next() {
        if (hasNext()) {
            while(((_index + _jumpCount + 1) < _containerHolder[0].length)  && _containerHolder[0][_index+_jumpCount+1] == null)
                _jumpCount++;
            if (_index + _jumpCount + 1 < _containerHolder[0].length) {
                @SuppressWarnings("unchecked")
                T temp = (T) _containerHolder[0][(++_index) + _jumpCount];
                return temp;
            }
            else
                return null;
        }
        return null;
    }

    /**
    * Removes the last returned element from collection
    * @exception CollectionNotRandomAccess If user try to delete a random location of not random access iterator.
    * @see CollectionNotRandomAccess
    */
    public void remove() throws CollectionNotRandomAccess{
        if (_container instanceof Queue && _index + _jumpCount != 0) {
            throw (new CollectionNotRandomAccess());
        } else if (isUsed()) {
            _containerHolder[0][_index + _jumpCount] = null;
            try{
                _container.remove(null);
            }catch(CollectionEmptyCollection a){}
            catch(CollectionNullElement a){}
            _index--;
            if(_containerHolder[0][_index + _jumpCount + 1] == null)
                _index++;
        }
    }

    private boolean isUsed() {
        if (_index == -1)
            return false;
        else
            return true;
    }
    
    private Collection<T> _container;
    private Object[][] _containerHolder;
    private int _index = -1;
    private int _jumpCount = 0;
}
