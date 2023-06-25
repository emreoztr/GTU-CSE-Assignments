import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class driver {
    

    public static void main(String[] args) {
        System.out.println("************************************Iterable Hash Map*******************************");
        IterableHashMap<Integer,Integer> iterMap = new IterableHashMap<>();
        Set<Integer> set = new HashSet<>();
        Random rand = new Random();
        int first, last, mid = 0;

        for(int i = 0; i < 20; ++i){
           set.add(rand.nextInt(100));
        }
        System.out.println("List of elements in set " + set);

        for(Integer e : set){
            iterMap.put(e, e);
        }
        MapIter<Integer> iter = iterMap.iterator();
        System.out.println("iter.next from start to end: ");
        for(int i = 0; iter.hasNext(); ++i){
            int e = iter.next();
            if(i == 10)
                mid = e;
            else if(i == 0)
                first = e;
            else if(i == set.size() - 1)
                last = e;
            System.out.print(e + ", ");
        }
        System.out.println("\n20 random next and prev: ");
        for(int i = 0; i < 20; ++i){
            if(rand.nextInt(2) % 2 == 1){
                System.out.println("next: " + iter.next());
            }else{
                System.out.println("prev: " + iter.prev());
            }
        }
        System.out.println("iter.next from start to end with iterator starts from middle element: ");
        iter = iterMap.iterator(mid);
        while(iter.hasNext())
            System.out.print(iter.next() + ", ");
        System.out.println("\n20 random next and prev with iterator starts from middle element: ");
        iter = iterMap.iterator(mid);
        for(int i = 0; i < 20; ++i){
            if(rand.nextInt(2) % 2 == 1){
                System.out.println("next: " + iter.next());
            }else{
                System.out.println("prev: " + iter.prev());
            }
        }
        System.out.println("\n***********************************HashMap Implemantations*******************************");

        testhashmap(1000);
        System.out.println("*********************************************************\n");
        testhashmap(10000);
        System.out.println("*********************************************************\n");
        testhashmap(100000);
        System.out.println("*********************************************************\n");


        System.out.println("\nTrying to get non-existing element:");
        HashMapListChain<Integer, Integer> listchain = new HashMapListChain<>();
        HashMapTreeSetChain<Integer, Integer> treechain = new HashMapTreeSetChain<>();
        HashMapCoalesced<Integer, Integer> coalescedchain = new HashMapCoalesced<>();
        for(int i = 0; i < 3; ++i){
            int val = rand.nextInt(10000);
            listchain.put(val, val);
            treechain.put(val, val);
            coalescedchain.put(val, val);
        }
        for(int i = 0; i < 5; ++i){
            if(listchain.get(1001 + i) == null)
                System.out.println((1001 + i) + " not found in HashMapListChain, test passed.");
            if(treechain.get(1001 + i) == null)
                System.out.println((1001 + i) + " not found HashMapTreeSetChain, test passed.");
            if(coalescedchain.get(1001 + i) == null)
                System.out.println((1001 + i) + " not found HashMapCoalesced, test passed.");
        }

        System.out.println("\nTrying to remove non-existing element:");

        for(int i = 0; i < 5; ++i){
            if(listchain.remove(1001 + i) == null)
                System.out.println((1001 + i) + " not found in HashMapListChain, test passed.");
            if(treechain.remove(1001 + i) == null)
                System.out.println((1001 + i) + " not found HashMapTreeSetChain, test passed.");
            if(coalescedchain.remove(1001 + i) == null)
                System.out.println((1001 + i) + " not found HashMapCoalesced, test passed.");
        }

        System.out.println("\nCoalesced map example:");
        System.out.println(coalescedchain);

        System.out.println("\nTrying to add null:");
        try{
            coalescedchain.put(null, null);
        }catch(NullPointerException e){
            System.out.println("NullPointerException throwed, test passed.");
        }
    }


    public static void testhashmap(int size){
        Random rand = new Random();
        HashMapListChain<Integer, Integer> listchain = new HashMapListChain<>();
        HashMapTreeSetChain<Integer, Integer> treechain = new HashMapTreeSetChain<>();
        HashMapCoalesced<Integer, Integer> coalescedchain = new HashMapCoalesced<>();
        System.out.println("Average case test (" + size +  "elements (random)):");
        ArrayList<Integer> arr = new ArrayList<>(); 
        for(int i = 0; i < size; ++i)
            arr.add(rand.nextInt(10000));


        /*---------------------PUT---------------------*/
        long start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            listchain.put(arr.get(i), arr.get(i));
        long end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for putting items in HashMapListChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            treechain.put(arr.get(i), arr.get(i));
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for putting items in HashMapTreeSetChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            coalescedchain.put(arr.get(i), arr.get(i));
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for putting items in HashMapCoalesced: " + (end - start));

        System.out.println("\n");

        Collections.shuffle(arr);

        /*----------------GET------------------------*/
        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            if(listchain.get(arr.get(i)) == null)
                System.out.println("PANNNNNİİİİİK 1");
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for getting items in HashMapListChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            if(treechain.get(arr.get(i)) == null)
                System.out.println("PANNNNNİİİİİK 2");
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for getting items in HashMapTreeSetChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            if(coalescedchain.get(arr.get(i)) == null)
                System.out.println("PANNNNNİİİİİK 3");
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for getting items in HashMapCoalesced: " + (end - start));

        System.out.println("\n");

        /*----------------REMOVE------------------------*/
        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            listchain.remove(arr.get(i));
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for removing items in HashMapListChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i)
            treechain.remove(arr.get(i));
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for removing items in HashMapTreeSetChain: " + (end - start));

        start = System.currentTimeMillis();
        for(int i = 0; i < size; ++i){
            coalescedchain.remove(arr.get(i));
        }
        end = System.currentTimeMillis();
        System.out.println("Time took in miliseconds for removing items in HashMapCoalesced: " + (end - start));
    }
}
