import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;


public class App {
    public static void main(String[] args){
        navigableSetDriver();
        driverBinarySearchTreeFinder();
        compareStructures();
    }

    private static void navigableSetDriver(){
        AVLTreeSet<Integer> avl = new AVLTreeSet<>();
        SkipListSet<Integer> skip = new SkipListSet<>();

        ArrayList<Integer> arr = new ArrayList<>();
        for(int i = 0; i < 30; ++i)
            arr.add(i);
        Collections.shuffle(arr);

        System.out.println("Adding numbers from 0 to 30 random to SkipListSet");

        for(int i = 0; i < 30; ++i)
            skip.insert(arr.get(i));
        System.out.println("Showing them with iterator: ");
        Iterator<Integer> iter = skip.descendingIterator();
        while(iter.hasNext())
            System.out.print(iter.next() + ", ");
        System.out.println();
        System.out.println("Deleting all numbers from SkipListSet");
        for(int i = 0; i < 30; ++i)
            skip.remove(arr.get(i));
        System.out.println("Showing them with iterator: ");
        iter = skip.descendingIterator();
        while(iter.hasNext())
            System.out.print(iter.next() + ", ");
        System.out.println("Trying to delete element not in the set: ");
        if(skip.remove(-10)) System.out.println("Tes failed");
        else System.out.println("Test Passed");

        System.out.println("Adding and removing 20K items: ");
        arr.clear();
        Random rand = new Random();
        for(int i = 0; i < 20000; ++i){
            arr.add(rand.nextInt(100000));
            skip.add(arr.get(i));
        }
        for(int i = 0; i < 20000; ++i)
            skip.remove(arr.get(i));

        arr.clear();
        for(int i = 0; i < 30; ++i)
            arr.add(i);
        Collections.shuffle(arr);
        
        System.out.println("--------------------------------------\nAdding numbers from 0 to 30 random to AVLTre");
        for(int i = 0; i < 30; ++i)
            avl.insert(arr.get(i));
        System.out.println("Showing them with iterator: ");
        iter = avl.iterator();
        while(iter.hasNext())
            System.out.print(iter.next() + ", ");
        System.out.println();
        System.out.println("Headset with 20:");
        SortedSet<Integer> s = avl.headSet(20);
        for(Integer i : s)
            System.out.print(i + ", ");

        System.out.println("\nTailset with 12:");
        s = avl.tailSet(12);
        for(Integer i: s)
            System.out.print(i + ", ");
        System.out.println("\nInserting 20K elements: ");
        for(int i = 0; i < 20000; i++)
            avl.insert(rand.nextInt(100000));
        avl = null;
    }

    private static void driverBinarySearchTreeFinder(){
        BinarySearchTree<Integer> avl = new AVLTree<>();
        BinarySearchTree<Integer> rb = new RedBlackTree<>();
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        System.out.println("-----------BSTree Finder Driver-----------");
        System.out.println("Adding numbers from 0 to 30 to all trees");
        for(int i = 0; i < 30; ++i){
            avl.add(i);
            rb.add(i);
            bst.add(i);
        }
        int ans = BinarySearchTreeFinder.whichTree(avl);
        if(ans == 0) System.out.println("Test passed, it is avl tree.");
        else System.out.println("Test failed");

        ans = BinarySearchTreeFinder.whichTree(rb);
        if(ans == 1) System.out.println("Test passed, it is red black tree.");
        else System.out.println("Test failed");

        ans = BinarySearchTreeFinder.whichTree(bst);
        if(ans == 3) System.out.println("Test passed, it is binary search tree.");
        else System.out.println("Test failed");
    }

    private static void compareStructures(){
        ComparisionOfStructures.calcBinarySearchTree(10000);
        ComparisionOfStructures.calcRedBlack(10000);
        ComparisionOfStructures.calcTwoThree(10000);
        ComparisionOfStructures.calcBTree(10000);
        ComparisionOfStructures.calcSkipList(10000);
        System.out.println("------------Comparison of Structures----------");
        System.out.println("Adding 100 elements to 10k elements: ");
        System.out.println("Binary Search Tree: " + ComparisionOfStructures.calcBinarySearchTree(10000));
        System.out.println("Red Black Tree: " + ComparisionOfStructures.calcRedBlack(10000));
        System.out.println("Two Three Tree: " + ComparisionOfStructures.calcTwoThree(10000));
        System.out.println("B-Tree (Order: 50): " + ComparisionOfStructures.calcBTree(10000));
        System.out.println("Skip List: " + ComparisionOfStructures.calcSkipList(10000));
        System.out.println("\nAdding 100 elements to 20k elements: ");
        System.out.println("Binary Search Tree: " + ComparisionOfStructures.calcBinarySearchTree(20000));
        System.out.println("Red Black Tree: " + ComparisionOfStructures.calcRedBlack(20000));
        System.out.println("Two Three Tree: " + ComparisionOfStructures.calcTwoThree(20000));
        System.out.println("B-Tree (Order: 50): " + ComparisionOfStructures.calcBTree(20000));
        System.out.println("Skip List: " + ComparisionOfStructures.calcSkipList(20000));
        System.out.println("\nAdding 100 elements to 40k elements: ");
        System.out.println("Binary Search Tree: " + ComparisionOfStructures.calcBinarySearchTree(40000));
        System.out.println("Red Black Tree: " + ComparisionOfStructures.calcRedBlack(40000));
        System.out.println("Two Three Tree: " + ComparisionOfStructures.calcTwoThree(40000));
        System.out.println("B-Tree (Order: 50): " + ComparisionOfStructures.calcBTree(40000));
        System.out.println("Skip List: " + ComparisionOfStructures.calcSkipList(40000));
        System.out.println("\nAdding 100 elements to 80k elements: ");
        System.out.println("Binary Search Tree: " + ComparisionOfStructures.calcBinarySearchTree(80000));
        System.out.println("Red Black Tree: " + ComparisionOfStructures.calcRedBlack(80000));
        System.out.println("Two Three Tree: " + ComparisionOfStructures.calcTwoThree(80000));
        System.out.println("B-Tree (Order: 50): " + ComparisionOfStructures.calcBTree(80000));
        System.out.println("Skip List: " + ComparisionOfStructures.calcSkipList(80000));
    }
}
