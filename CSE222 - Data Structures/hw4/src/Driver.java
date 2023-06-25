import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Random;


public class Driver {
    public static void main(String[] args) {
        heapTest();
        System.out.println("--------------------------------------------------------------------");
        bstHeapTreeTest();
    }

    public static void heapTest(){
        HeapPartOne<Integer> heap = new HeapPartOne<>();
        System.out.println("Search Test:");
        for(int i = 0; i < 20; ++i){
            heap.add(i);
        }
        System.out.println("20 integer added to the heap");
        System.out.println(heap);
        System.out.println("Search for 20 integer:");
        for(int i = 0; i < 20; ++i){
            if(heap.search(Integer.valueOf(i)))
                System.out.println(i + " is found");
        }
        System.out.println("\nSearching for an element that heap doesn't have:");
        if(!heap.search(Integer.valueOf(999)))
            System.out.println("999 is not found");
        System.out.println("*************************************");
        System.out.println("Merge:");
        HeapPartOne<Integer> heap2 = new HeapPartOne<>();
        for(int i = 21; i < 35; ++i)
            heap2.add(i);
        System.out.println("Created new heap with values from 21 to 35");
        heap.merge(heap2);
        System.out.println("First heap after merging with second heap:");
        System.out.println(heap);
        System.out.println("**************************************");
        System.out.println("Removing ith largest element:");
        System.out.println("Removing 6th largest element (which is 28 in this case)");
        heap.removeBiggest(6);
        System.out.println("First heap after remove:");
        System.out.println(heap);
        System.out.println("Trying to remove negative index -1:");
        if(!heap.removeBiggest(-1))
            System.out.println("Method returned false");
        System.out.println("Trying to remove an index bigger than size (60):");
        if(!heap.removeBiggest(60))
            System.out.println("Method returned false");
        System.out.println("Last situation in first heap:");
        System.out.println(heap);
        System.out.println("**************************************");
        System.out.println("Set method in iterator:");
        HeapIterator<Integer> heapIter = heap.heapIterator();
        System.out.println("Seth 5th element with value of 4 to 999:");
        for(int i = 0; i < 5; ++i)
            heapIter.next();
        heapIter.set(999);
        System.out.println(heap);
        System.out.println("Trying to use set again before using next (Return value should be null):");
        System.out.println("Return value: " + heapIter.set(888));
    }

    public static void bstHeapTreeTest(){
        Random rand = new Random();
        BSTHeapTree<Integer> tree = new BSTHeapTree<>();
        ArrayList<Integer> arr = new ArrayList<>();
        boolean found = false;

        for(int i = 0; i < 3000; ++i){
            Integer element = Integer.valueOf(rand.nextInt(5000));
            tree.add(element);
            arr.add(element);
        }
        Collections.sort(arr);
        int ind = 0;
        for(int i = 0; i < 100; ++i){
            System.out.println("Number: " + arr.get(ind));
            try{
                System.out.println("Occurence count in array: " + findOccur(arr, ind));
                System.out.println("Occurence count in BSTHeapTree: " + tree.find(arr.get(ind)) + "\n");
            }catch(NoSuchElementException e){
                System.out.println("TEST IS NOT SUCCESSFUL");
                e.printStackTrace();
                throw e;
            }
            ind = findIndexNextItem(arr, ind);
            if(ind < 0){
                System.out.println("Array size is not sufficient for this test, Array size: " + arr.size());
                break;
            }
        }

        System.out.println("TRYING TO FIND ELEMENTS THAT TREE DOESN'T HAVE");

        for(int i = 5001; i < 5011; ++i){
            System.out.println("Number: " + i);
            try{
                System.out.println("Occurence count in BSTHeapTree: " + tree.find(i) + "\n");
            }catch(NoSuchElementException e){
                System.out.println("NoSuchElementException has been throwed, test is successful");
            }
        }

        Integer mode = null;
        int max = 0;
        ind = 0;
        int old_ind = 0;
        while(ind >= 0){
            old_ind = ind;
            ind = findIndexNextItem(arr, ind);
            if((ind - old_ind) > max){
                max = ind - old_ind;
                mode = arr.get(old_ind);
            }
        }
        if(arr.size() - old_ind > max){
            max = arr.size() - old_ind;
            mode = arr.get(old_ind);
        }
        
        System.out.println("\nMODE TEST");
        System.out.println("Mode in array: " + mode);
        System.out.println("Count of mode in array: " + max);
        System.out.println("Mode in tree: " + tree.find_mode());
        System.out.println("Count of mode in tree: " + tree.find(tree.find_mode()));

        System.out.println("\nREMOVE TEST");
        for(int i = 0; i < 100; ++i){
            int count = findOccur(arr, i);
            System.out.println("Number will be removed: " + arr.get(i));
            System.out.println("Occurence in array before remove: " + (count));
            System.out.println("Occurence in tree before remove: " + tree.find(arr.get(i)));
            System.out.println("Occurence in array after remove: " + (count - 1));
            System.out.println("Occurence in tree after remove: " + tree.remove(arr.get(i)));
            System.out.println();
        }
        System.out.println("\nTRYING TO REMOVE ITEMS THAT NOT IN THE ARRAY");
        for(int i = 5001; i < 5011; ++i){
            try{
                tree.remove(i);
            }catch(NoSuchElementException e){
                System.out.println("Number: " + i);
                System.out.println("NoSuchElementException has been throwed, test is successful\n");
            }
        }
    }

    private static int findIndexNextItem(ArrayList<Integer> arr, int curr_ind){
        Integer e = arr.get(curr_ind);
        for(int i = curr_ind + 1; i < arr.size(); ++i){
            if(!arr.get(i).equals(e))
                return i;
        }
        return -1;
    }

    private static int findOccur(ArrayList<Integer> arr, int curr_ind){
        Integer e = arr.get(curr_ind);
        int count = 0;
        for(int i = curr_ind; i < arr.size(); ++i){
            if(e.equals(arr.get(i)))
                count++;
            else
                return count;
        }
        return count;
    }
}
