import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public abstract class ComparisionOfStructures {
    
    /**
     * To calculate time consumption of adding 100 elements to binary search tree
     * @param count to add how many elements
     * @return returns time in nanoseconds
     */
    public static double calcBinarySearchTree(int count){
        long start;
        long end;
        long avr = 0;

        for(int k = 0; k < 10; ++k){
            BinarySearchTree<Integer> bstree = new BinarySearchTree<>();
            List<Integer> items = findElements(count + 100);
            Integer[] hund = new Integer[100];
            for(int i = count; i < count + 100; ++i)
                hund[i - count] = items.get(i);
            for(int i = 0; i < count; ++i)
                bstree.add(items.get(i));
            start = System.nanoTime();
            for(int i = 0; i < 100; ++i)
                bstree.add(hund[i]);
            end = System.nanoTime();
            avr += ((end - start) / 10);
        }
        return avr;
    }

    /**
     * To calculate time consumption of adding 100 elements to red black tree
     * @param count to add how many elements
     * @return returns time in nanoseconds
     */
    public static double calcRedBlack(int count){
        long start;
        long end;
        long avr = 0;

        for(int k = 0; k < 10; ++k){
            RedBlackTree<Integer> rb = new RedBlackTree<>();
            List<Integer> items = findElements(count + 100);
            Integer[] hund = new Integer[100];
            for(int i = count; i < count + 100; ++i)
                hund[i - count] = items.get(i);
            for(int i = 0; i < count; ++i)
                rb.add(items.get(i));
            start = System.nanoTime();
            for(int i = 0; i < 100; ++i)
                rb.add(hund[i]);
            end = System.nanoTime();
            avr += ((end - start) / 10);
        }
        return avr;
    }

    /**
     * To calculate time consumption of adding 100 elements to two three tree
     * @param count to add how many elements
     * @return returns time in nanoseconds
     */
    public static double calcTwoThree(int count){
        long start;
        long end;
        long avr = 0;

        for(int k = 0; k < 10; ++k){
            TwoThree<Integer> tt = new TwoThree<>();
            List<Integer> items = findElements(count + 100);
            Integer[] hund = new Integer[100];
            for(int i = count; i < count + 100; ++i)
                hund[i - count] = items.get(i);
            for(int i = 0; i < count; ++i)
                tt.insert(items.get(i));
            start = System.nanoTime();
            for(int i = 0; i < 100; ++i)
                tt.insert(hund[i]);
            end = System.nanoTime();
            avr += ((end - start) / 10);
        }
        return avr;
    }

    /**
     * To calculate time consumption of adding 100 elements to skiplist
     * @param count to add how many elements
     * @return returns time in nanoseconds
     */
    public static double calcSkipList(int count){
        long start;
        long end;
        long avr = 0;

        for(int k = 0; k < 10; ++k){
            SkipList<Integer> skip = new SkipList<>();
            List<Integer> items = findElements(count + 100);
            Integer[] hund = new Integer[100];
            for(int i = count; i < count + 100; ++i)
                hund[i - count] = items.get(i);
            for(int i = 0; i < count; ++i)
                skip.insert(items.get(i));
            start = System.nanoTime();
            for(int i = 0; i < 100; ++i)
                skip.insert(hund[i]);
            end = System.nanoTime();
            avr += ((end - start) / 10);
        }
        return avr;
    }

    /**
     * To calculate time consumption of adding 100 elements to b-tree
     * @param count to add how many elements
     * @return returns time in nanoseconds
     */
    public static double calcBTree(int count){
        long start;
        long end;
        long avr = 0;

        for(int k = 0; k < 10; ++k){
            BTree<Integer> skip = new BTree<>(50);
            List<Integer> items = findElements(count + 100);
            Integer[] hund = new Integer[100];
            for(int i = count; i < count + 100; ++i)
                hund[i - count] = items.get(i);
            for(int i = 0; i < count; ++i)
                skip.insert(items.get(i));
            start = System.nanoTime();
            for(int i = 0; i < 100; ++i)
                skip.insert(hund[i]);
            end = System.nanoTime();
            avr += ((end - start) / 10);
        }
        return avr;
    }

    private static List<Integer> findElements(int count){
        Random rand = new Random();
        Set<Integer> s = new TreeSet<>();
        while(s.size() < count)
            s.add(rand.nextInt(count*100));
        List<Integer> arr = new ArrayList<>(List.copyOf(s));
        Collections.shuffle(arr);
        return arr;
    }
}
