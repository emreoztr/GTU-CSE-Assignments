import java.util.Arrays;
import java.util.Random;

public class SkipList<E extends Comparable<E>> {
    static final double LOG2 = Math.log(2.0);
    private SLNode<E> head = null;
    private Random rand = new Random();
    private int maxCap = 2;
    private int maxLevel = (int) (Math.log(maxCap) / LOG2);
    private int size = 0;

    

    /** Static class to contain the data and the links */
    static class SLNode<E> {
        SLNode<E>[] links; 
        E data;
        /** Create a node of level m */
        SLNode (int m, E data) {
            links = (SLNode<E>[]) new SLNode[m]; // create links
            this.data = data; // store item
        }
    }

    private int logRandom() {
        int r = rand.nextInt(maxCap);
        int k = (int) (Math.log(r + 1) / LOG2);
        if (k > maxLevel - 1) {
            k = maxLevel - 1;
        }
        return maxLevel - k;
    }

    public boolean insert(E item){
        if(head == null){
            head = new SLNode<>(maxLevel, item);
            size++;
            return true;
        }
        
        int newNodeLevel = logRandom();
        SLNode<E> newNode = new SLNode<>(newNodeLevel, item);
        int currLevelIndex = maxLevel - 1;
        SLNode<E>[] pred = (SLNode<E>[]) new SLNode[maxLevel];
        SLNode<E> curr = head;
        
        if(item.compareTo(head.data) <= 0){
            for(int i = newNodeLevel - 1; i >= 0; --i){
                newNode.links[i] = head.links[i];
                head.links[i] = newNode;
            }
            E temp = head.data;
            head.data = newNode.data;
            newNode.data = temp;
            size++;
            if(size > maxCap)
                increaseLevel();
            return true;
        }

        while(currLevelIndex >= 0){
            if(item.compareTo(curr.data) >= 0){
                pred[currLevelIndex] = curr;
                curr = curr.links[currLevelIndex];
            }
            if(curr == null || item.compareTo(curr.data) < 0 ){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
        }
        size++;
        for(int i = newNodeLevel - 1; i >= 0; --i){
            newNode.links[i] = pred[i].links[i];
            pred[i].links[i] = newNode;
        }

        if(size > maxCap)
            increaseLevel();

        return true;
    }

    private SLNode<E> findNode(E item, SLNode<E>[] pred){
        SLNode<E> returnVal = null;
        if(item.compareTo(head.data) == 0)
            return head;
        else if(item.compareTo(head.data) < 0)
            return null;

        SLNode<E> curr = head;
        int currLevelIndex = maxLevel - 1;
        
        while(currLevelIndex >= 0){
            if(item.compareTo(curr.data) > 0){
                pred[currLevelIndex] = curr;
                curr = curr.links[currLevelIndex];
            }
            if(curr == null || item.compareTo(curr.data) < 0 ){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
            if(item.compareTo(curr.data) == 0 && currLevelIndex == 0)
                return curr;
            else if(item.compareTo(curr.data) == 0){
                curr = pred[currLevelIndex--];
                if(currLevelIndex >= 0)
                    pred[currLevelIndex] = curr;
            }
        }
        return null; 
    }

    public boolean remove(Object o) {
        try{
            @SuppressWarnings("unchecked")
            E item = (E) o;
            SLNode<E>[] pred = (SLNode<E>[]) new SLNode[maxLevel];
            SLNode<E> found = findNode(item, pred);
            if(found == null)
                return false;
            else if(found == head){
                System.out.println("ehe");
                return false;
            }
            for(int i = pred.length - 1; i >= 0; --i){
                if(pred[i].links[i] == found)
                    pred[i].links[i] = found.links[i];
            }
            return true;
        }catch(ClassCastException e){
            System.out.println("err");
            return false;
        }
    }

    private int calcMaxCap(int level){
        return (int) Math.pow(2, level) - 1;
    }

    private void increaseLevel(){
        maxLevel++;
        maxCap = calcMaxCap(maxLevel);
        head.links = Arrays.copyOf(head.links, maxLevel);
    }
}
