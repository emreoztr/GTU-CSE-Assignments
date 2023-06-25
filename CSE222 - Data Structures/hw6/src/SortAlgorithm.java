import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public abstract class SortAlgorithm {
    
    public static <E extends Comparable<E>> void shellSort(ArrayList<E> arr){
        int gap = (int) (arr.size() / 2.2);
        while(gap >= 1){
            for(int i = gap; i < arr.size(); i+=gap)
                insertionWithGap(arr, i, gap);
            
            if(gap == 2)
                gap = 1;
            else
                gap /= 2.2;
        }
    }

    private static <E extends Comparable<E>> void insertionWithGap(ArrayList<E> arr, int pos, int gap){
        int nextPos = pos;
        E valHolder = arr.get(nextPos);

        while((nextPos - gap) >= 0 && (valHolder.compareTo(arr.get(nextPos - gap))) <= 0){
            arr.set(nextPos, arr.get(nextPos - gap));
            nextPos -= gap;
        }
        arr.set(nextPos, valHolder);
    }

    /**
     * Merge sort to sort given list
     * @param list list which will be sorted
     * @param compar comparator for comparision operations
     */
    public static <E extends Comparable<E>> void mergeSort(List<E> list, Comparator<E> compar){
        if(list.size() > 1){
            ArrayList<E> right = new ArrayList<>();
            ArrayList<E> left = new ArrayList<>();

            int mid = list.size() / 2;
            ListIterator<E> list_iter = list.listIterator();

            for(int i = 0; i < mid; ++i)
                left.add(list_iter.next());

            for(int i = mid; list_iter.hasNext(); ++i)
                right.add(list_iter.next());

            mergeSort(left, compar);
            mergeSort(right, compar);
        
            list.clear();
            list.addAll(merge(left, right, compar));
        }
    }

    private static <E extends Comparable<E>> List<E> merge(ArrayList<E> left_arr, ArrayList<E> right_arr, Comparator<E> compar){
        List<E> merged = new ArrayList<>();
        int left_ind = 0;
        int right_ind = 0;
        
        while(left_ind < left_arr.size() && right_ind < right_arr.size()){
            if(compar.compare(left_arr.get(left_ind), right_arr.get(right_ind)) < 0)
                merged.add(left_arr.get(left_ind++));
            else
                merged.add(right_arr.get(right_ind++));
        }
        if(left_ind < left_arr.size())
            for(int i = left_ind; i < left_arr.size(); ++i)
                merged.add(left_arr.get(i));
        
        if(right_ind < right_arr.size())
            for(int i = right_ind; i < right_arr.size(); ++i)
                merged.add(right_arr.get(i));

        return merged;
    }

    /**
     * Quick sort to sort given list
     * @param list list which will be sorted
     * @param compar comparator for comparision operations
     */
    public static <E extends Comparable<E>> void quick(ArrayList<E> list, Comparator<E> compar){
        quicksort(list, 0, list.size()-1, compar);
    }

    private static <E extends Comparable<E>> void quicksort(ArrayList<E> list, int first, int last, Comparator<E> compar){
        if(first < last){
            int piv = partition(list, first, last, compar);

            quicksort(list, first, piv - 1, compar);
            quicksort(list, piv + 1, last, compar);
        }
    }

    private static <E extends Comparable<E>> int partition(ArrayList<E> list, int first, int last, Comparator<E> compar){
        int upInd = first;
        int dnInd = last;
        int mid = (first + last) / 2;
        int pivotInd;

        if((list.get(first).compareTo(list.get(mid)) > 0 && list.get(first).compareTo(list.get(last)) < 0) ||
            (list.get(first).compareTo(list.get(mid)) < 0 && list.get(first).compareTo(list.get(last)) > 0))
            pivotInd = first;

        else if((list.get(mid).compareTo(list.get(first)) < 0 && list.get(mid).compareTo(list.get(last)) > 0) || 
            (list.get(mid).compareTo(list.get(first)) > 0 && list.get(mid).compareTo(list.get(last)) < 0))
            pivotInd = mid;

        else if((list.get(last).compareTo(list.get(first)) < 0 && list.get(last).compareTo(list.get(mid)) > 0) ||
            (list.get(last).compareTo(list.get(first)) > 0 && list.get(last).compareTo(list.get(mid)) < 0))
            pivotInd = last; 
                   
        else
            pivotInd = first;

        swap(first, pivotInd, list);
        pivotInd = first;

        E pivot = list.get(pivotInd);
        
        do{
            while(upInd < last && compar.compare(list.get(upInd), pivot) <= 0)
                upInd++;
            while(compar.compare(list.get(dnInd), pivot) > 0)
                dnInd--;
            
            if(upInd < dnInd)
                swap(dnInd, upInd, list);
        }while(upInd < dnInd);

        swap(first, dnInd, list);
        return dnInd;
    }

    private static <E extends Comparable<E>> void swap(int first, int last, ArrayList<E> list){
        E temp = list.get(last);
        list.set(last, list.get(first));
        list.set(first, temp);
    }

    /**
     * Heap sort algorithm to sort the given list
     * @param list lsit which will be sorted
     * @param compar comparator for comparision operations
     */
    public static <E extends Comparable<E>> void heapSort(ArrayList<E> list, Comparator<E> compar){
        for(int i = 0; i < list.size(); ++i)
            addElementToHeap(list, i, compar);

        for(int i = 0; i < list.size(); ++i)
            removeElementFromHeap(list, list.size() - i, compar);
    }

    private static <E extends Comparable<E>> void addElementToHeap(ArrayList<E> list, int ind, Comparator<E> compar){
        if(ind == 0)
            return;

        int parent = (ind - 1) / 2;
        int curr = ind;
        
        while(curr > 0 && compar.compare(list.get(curr), list.get(parent)) < 0){
            swap(curr, parent, list);
            curr = parent;
            parent = (curr - 1) / 2;
        }
    }

    private static <E extends Comparable<E>> void removeElementFromHeap(ArrayList<E> list, int size, Comparator<E> compar){
        if(size <= 1)
            return;
        
        swap(0, size - 1, list);
        size--;
        int leftChild = 1;
        int rightChild = 2;
        int curr = 0;

        while(leftChild < size){
            int next = curr;

            if(rightChild < size){
                if(compar.compare(list.get(leftChild), list.get(rightChild)) < 0){
                    if(compar.compare(list.get(curr), list.get(leftChild)) > 0)
                        next = leftChild;
                }
                else{
                    if(compar.compare(list.get(curr), list.get(rightChild)) > 0)
                        next = rightChild;
                }
            }else{
                if(compar.compare(list.get(leftChild), list.get(rightChild)) < 0){
                    if(compar.compare(list.get(curr), list.get(leftChild)) > 0)
                        next = leftChild;
                } 
            }

            if(next == curr)
                break;

            swap(curr, next, list);
            curr = next;
            leftChild = 2 * curr + 1;
            rightChild = 2 * curr + 2;
        }
    }
}
