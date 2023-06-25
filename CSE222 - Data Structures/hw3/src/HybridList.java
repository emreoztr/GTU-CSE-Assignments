import java.io.Serializable;
import java.util.*;

public class HybridList<E> implements List<E>, Serializable {
    private KWLinkedList<KWArrayList<E>> dataList = null;
    private final int MAX_NUMBER = 15;
    private int total_size = 0;
    private KWArrayList lastArrayList = null;

    private class HybridIter implements ListIterator<E>{
        private int cursor = 0;
        private int arr_cursor = 0;
        private ListIterator<KWArrayList<E>> dataList_iter = null;
        private KWArrayList<E> current_arr = null;
        private KWArrayList<E> last_arr = null;
        private int last_returned_arrcursor = 0;
        private int last_returned_cursor = 0;

        public HybridIter(){
            if(dataList != null){
                dataList_iter = dataList.listIterator();

            }
        }

        public HybridIter(int ind){
            if(ind == 0){
                dataList_iter = dataList.listIterator();
            }
            else if(ind == size()){
                dataList_iter = dataList.listIterator(dataList.size());
                dataList_iter.previous();
                dataList_iter.next();
                current_arr = dataList.getLast();
                cursor = ind;
                arr_cursor = current_arr.size();
            }else{
                dataList_iter = dataList.listIterator();
                for(int i = 0; i < ind; ++i){
                    next();
                }
            }
        }


        @Override
        public boolean hasNext() {
            if(current_arr == null && !dataList_iter.hasNext())
                return false;
            else if(dataList_iter.hasNext())
                return true;

            if(current_arr.size() > arr_cursor || dataList_iter.hasNext())
                return true;
            else
                return false;
        }

        @Override
        public E next() throws NoSuchElementException{
            if(current_arr == null){
                if(dataList_iter.hasNext())
                    current_arr = dataList_iter.next();
                else
                    throw new NoSuchElementException();
            }

            if(current_arr.size() > arr_cursor){
                last_arr = current_arr;
                last_returned_arrcursor = arr_cursor;
                last_returned_cursor = cursor++;
                return current_arr.get(arr_cursor++);
            }else if(dataList_iter.hasNext()){
                current_arr = dataList_iter.next();
                arr_cursor = 0;
                last_arr = current_arr;
                last_returned_arrcursor = arr_cursor;
                last_returned_cursor = cursor++;
                return current_arr.get(arr_cursor++);
            }else{
                throw new NoSuchElementException();
            }
        }

        @Override
        public boolean hasPrevious() {
            if((arr_cursor - 1 >= 0 &&  current_arr.size() > arr_cursor - 1))
                return true;
            else if(dataList_iter.hasPrevious()) {
                if (dataList_iter.previous() == current_arr){
                    if (dataList_iter.hasPrevious()) {
                        dataList_iter.next();
                        return true;
                    } else {
                        return false;
                    }
                }else{
                    dataList_iter.next();
                    return true;
                }
            }else{
                return false;
            }
            /*if((arr_cursor - 1 >= 0 &&  current_arr.size() > arr_cursor - 1)
                    || dataList_iter.hasPrevious())
                return true;
            else
                return false;*/
        }

        @Override
        public E previous() {
            if(arr_cursor - 1 >= 0 &&  current_arr.size() > arr_cursor - 1){
                last_arr = current_arr;
                last_returned_arrcursor = --arr_cursor;
                last_returned_cursor = --cursor;
                return current_arr.get(arr_cursor);
            }else if(dataList_iter.hasPrevious()){
                KWArrayList<E> tempArr = dataList_iter.previous();
                if(tempArr == current_arr){
                    if(dataList_iter.hasPrevious()){
                        current_arr = dataList_iter.previous();
                        dataList_iter.next();
                    }
                    else{
                        throw new NoSuchElementException();
                    }
                }else{
                    current_arr = tempArr;
                }
                arr_cursor = current_arr.size() - 1;
                last_arr = current_arr;
                last_returned_arrcursor = arr_cursor;
                last_returned_cursor = --cursor;
                return current_arr.get(arr_cursor);

            }else{
                throw new NoSuchElementException();
            }
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor-1;
        }

        @Override
        public void remove() throws IllegalStateException{
            if(last_arr == null)
                throw new IllegalStateException();

            last_arr.remove(last_returned_arrcursor);
            total_size--;
            if(last_returned_cursor < cursor){
                cursor--;
                arr_cursor--;
            }
            if(last_arr.isEmpty()){
                dataList_iter.remove();

                if(dataList_iter.hasNext()){
                    current_arr = dataList_iter.next();
                    arr_cursor = 0;
                }else{
                    arr_cursor = 0;
                    current_arr = null;
                }

            }
            last_arr = null;
        }

        @Override
        public void set(E e) throws IllegalStateException{
            if(last_arr == null)
                throw new IllegalStateException();
            last_arr.set(last_returned_arrcursor, e);
        }

        @Override
        public void add(E e){ //not finished
            if(current_arr == null){
                dataList_iter.add(new KWArrayList<E>());
                dataList_iter.previous();
                current_arr = dataList_iter.next();
                arr_cursor = 0;
            }else if(arr_cursor == MAX_NUMBER){
                arr_cursor = 0;
                if(dataList_iter.hasNext())
                    current_arr = dataList_iter.next();
                else{
                    dataList_iter.add(new KWArrayList<>());
                    current_arr = dataList_iter.previous();
                    dataList_iter.next();
                }
            }

            if(current_arr.size() < MAX_NUMBER){
                current_arr.add(arr_cursor,e);
            }else{

                if(dataList_iter.hasNext()){
                    if(MAX_NUMBER - dataList_iter.next().size() < current_arr.size() - arr_cursor){
                        dataList_iter.previous();
                        dataList_iter.add(new KWArrayList<>());
                        dataList_iter.previous();
                    }
                }else{
                    dataList_iter.add(new KWArrayList<E>());
                    dataList_iter.previous();
                }
                dataList_iter.next().add(current_arr.get(MAX_NUMBER-1));
                dataList_iter.previous();
                current_arr.remove(MAX_NUMBER-1);
                current_arr.add(arr_cursor, e);
                dataList_iter.next();
            }
            total_size++;
            arr_cursor++;
            cursor++;
            last_arr = null;
        }
    }

    /** constructor.
     */
    public HybridList(){
        dataList = new KWLinkedList<>();
    }

    /** Sets the element at the index with new value.
     * @param int index
     * @param E object
     @return The old value in index.
     @throws ArrayIndexOutOfBoundsException
     */
    public E set(int ind, E obj) throws ArrayIndexOutOfBoundsException{
        int count = 0;

        if(ind < 0 || ind >= size())
            throw new ArrayIndexOutOfBoundsException(ind);

        for (KWArrayList<E> arr : dataList) {
            if (count + arr.size() >= ind + 1)
                return arr.set(ind - count, obj);
            count += arr.size();
        }

        throw new ArrayIndexOutOfBoundsException(ind);
    }

    /** Checks if the list contains the item or not.
     * @param Object Item will be checked
     @return Contains or not.
     */
    public boolean contains(Object obj){
        for (KWArrayList<E> arr : dataList) {
            for (int i = 0; i < arr.size(); ++i)
                if (arr.contains(obj))
                    return true;
        }
        return false;
    }

    /** Returns the item at the index.
     * @param int index
     @return Item in the index.
     @throws ArrayIndexOutOfBoundsException
     */
    public E get(int ind) throws ArrayIndexOutOfBoundsException{
        int count = 0;

        if(ind < 0 || ind >= size())
            throw new ArrayIndexOutOfBoundsException(ind);

        for (KWArrayList<E> arr : dataList) {
            if (count + arr.size() >= ind + 1)
                return arr.get(ind - count);
            count += arr.size();
        }

        throw new ArrayIndexOutOfBoundsException(ind);
    }

    /** Returns the item at the index.
     * @param int index
     @return Item in the index.
     @throws ArrayIndexOutOfBoundsException
     */
    public boolean add(E obj){
        ListIterator<E> listIter = listIterator(size());
        listIter.add(obj);
        return true;
    }

    public boolean containsAll(Collection<?> c){
        for (Object o : c) {
            if (!contains(o))
                return false;
        }
        return true;
    }

    public int size(){
        return total_size;
    }

    @Override
    public boolean isEmpty() {
        if(size() == 0)
            return true;
        else
            return false;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] returnVal = new Object[size()];
        int ind = 0;
        for(KWArrayList<E> arr : dataList)
            for(int i = 0; i < arr.size(); ++i)
                returnVal[ind++] = arr.get(i);

        return returnVal;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        ListIterator<E> list_iter = listIterator();
        while(list_iter.hasNext()){
            if(list_iter.next().equals(o)){
                list_iter.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for(E obj : c)
            if(!add(obj))
                return false;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        try{
            ListIterator<E> iter = listIterator(index);
            Iterator c_iter = c.iterator();
            while(c_iter.hasNext())
                iter.add((E)c_iter.next());
            return true;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        dataList.clear();
        total_size = 0;
    }

    @Override
    public void add(int ind, E element) throws IndexOutOfBoundsException{
        try {
            ListIterator<E> iter = listIterator(ind);
            iter.add(element);
        }catch(IndexOutOfBoundsException e){
            throw e;
        }
    }

    @Override
    public E remove(int index) throws IndexOutOfBoundsException{
        if(index < 0 || index >= total_size)
            throw new IndexOutOfBoundsException();
        ListIterator<E> iter = listIterator(index);
        E returnVal = iter.next();
        iter.remove();
        return returnVal;
    }

    @Override
    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new HybridIter();
    }

    @Override
    public ListIterator<E> listIterator(int index) throws IndexOutOfBoundsException{
        if(index < 0 || index > total_size)
            throw new IndexOutOfBoundsException();
        return new HybridIter(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public String toString(){
        StringBuilder strBuild = new StringBuilder();

        ListIterator tempList = dataList.listIterator();
        while(tempList.hasNext()){
            KWArrayList arr = (KWArrayList) tempList.next();
            for(int i = 0; i < arr.size(); ++i){
                strBuild.append(arr.get(i)).append(", ");
            }
            strBuild.append("->\n");
        }
        return strBuild.toString();
    }
}
