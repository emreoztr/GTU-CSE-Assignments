import java.io.Serializable;

/**
 * Container class made from the array to write more clean.
 * @author Yunus_Emre_Ozturk
 */
public class ArrayContainer<T> implements Serializable{
    private T[] arr;
    private int cap = 0;
    private int size = 0;

    /**
     * ArrayContainer constructor, initial capacity is 15.
     */
    public ArrayContainer(){
        setCap(15);
    }

    /**
     * ArrayContainer constructor, user defined capacity.
     * @param cap capacity information comes from user.
     * @throws IllegalArgumentException if user enters negative capacity.
     */
    public ArrayContainer(int cap) throws IllegalArgumentException{
        if(!setCap(cap)){
            throw(new IllegalArgumentException());
        }
    }

    /**
     * To get capacity information.
     * @return returns integer about capacity.
     */
    public int getCap(){
        return cap;
    }

    /**
     * To get size information.
     * @return returns integer about size.
     */
    public int getSize(){
        return size;
    }

    /**
     * Adds elements to array.
     * @param element it is a generic element.
     * @return void.
     */
    public void add(T element){
        if(size >= cap){
            setCap(2*cap);
        }
        arr[size++] = element;
    }

    /**
     * Check if array contains the given object.
     * @param o object that will be checked.
     * @return return boolean if array return it, else false.
     */
    public boolean contains(Object o){
        @SuppressWarnings("unchecked")
        T element = (T) o;
        for(int i = 0; i < getSize(); ++i){
            if(arr[i].equals(element))
                return true;
        }
        return false;
    }

    /**
     * Removes an element from array.
     * @param index index of the place that will be removed.
     * @return void.
     */
    public void remove(int index){
        if(index >= 0 && index < getSize()){
            for(int i = index; i < getSize() - 1; ++i)
                arr[i] = arr[i+1];

            setSize(size - 1);
        }
    }

    /**
     * To get specific element in array.
     * @param index index of the place that will return.
     * @return returns generic type of the object.
     * @throws IndexOutOfBoundsException if index is not available.
     */
    public T get(int index) throws IndexOutOfBoundsException{
        if(index < getSize() && index >= 0)
            return arr[index];
        else
            throw(new IndexOutOfBoundsException());
    }

    private void setSize(int size){
        if(size >= 0)
            this.size = size;
    }

    private boolean setCap(int cap){
        if(cap > this.cap){
            @SuppressWarnings("unchecked")
            T[] temp_arr = (T[]) new Object[cap];

            for(int i = 0; i < getSize(); ++i)
                temp_arr[i] = arr[i];
            
            arr = temp_arr;
            this.cap = cap;
            return true;
        }
        return false;
    }
}
