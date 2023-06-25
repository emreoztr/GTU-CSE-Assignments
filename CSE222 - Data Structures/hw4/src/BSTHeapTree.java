import java.io.Serializable;
import java.util.Collections;
import java.util.NoSuchElementException;

public class BSTHeapTree<E extends Comparable<E> > implements Serializable {
    private BSTNode<HeapPartOne<Node<E> > > root = null;
    private final int MAX_HEAP_HEIGHT = 3;
    private final int MAX_HEAP_SIZE = ((int) Math.pow(2, MAX_HEAP_HEIGHT)) - 1;
    private int size = 0;


    private static class Node<E extends Comparable<E> > implements Comparable<Node<E> >, Serializable{
        private E data;
        private int occur = 0;

        public Node(E e){
            if(e == null)
                throw new NullPointerException();
            data = e;
            occur = 1;
        }

        public Node(){
            data = null;
        }

        @Override
        public int compareTo(Node<E> o) {
            return this.data.compareTo(o.data);
        }

        @Override
        public boolean equals(Object o){
            if(o == null)
                return false;

            if(o.getClass() != this.getClass())
                return false;

            @SuppressWarnings("unchecked")
            Node<E> other = (Node<E>) o;
            if(this.data.equals(other.data))
                return true;
            else
                return false;
            
        }

        @Override
        public String toString(){
            return data.toString();
        }

    }

    private static class BSTNode<T extends Comparable<T> > implements Comparable<BSTNode<T> >, Serializable{
        private T data;
        private BSTNode<T> left=null, right=null;

        public BSTNode(T e){
            if(e == null)
                throw new NullPointerException();
            data = e;
        }

        @Override
        public int compareTo(BSTNode<T> o) {
            return this.data.compareTo(o.data);
        }

        @Override
        public boolean equals(Object o){
            if(o == null)
                return false;
            
            if(this.getClass() != o.getClass())
                return false;

            @SuppressWarnings("unchecked")
            BSTNode<T> other = (BSTNode<T>) o;
            if(this.data.equals(other.data))
                return true;
            else
                return false;
        }

        @Override
        public String toString(){
            return data.toString();
        }
    }

    /**
     * To retrieve size of the BSTHeapTree
     * @return return size as int
     */
    public int size(){
        return size;
    }

    /**
     * Adds given element to the BSTHeapTree
     * @param item item that will be added
     * @return returns number of occurences of the item after addition
     * @throws NullPointerException
     */
    public int add(E item) throws NullPointerException{
        if(item == null)
            throw new NullPointerException();
        size++;

        Node<E> node = findNode(root, item);
        if(node != null){
            return ++(node.occur);
        }

        if(!add_to_node(root, item)){
            HeapPartOne<Node<E> > newHeap = new HeapPartOne<>(Collections.reverseOrder());
            newHeap.add(new Node<E>(item));
            root = addBSTNode(root, newHeap);
        }
        
        return 1;
    }


    private boolean add_to_node(BSTNode<HeapPartOne<Node<E> > > root, E item){
        if(root == null){
            return false;
        }
        if(root.data.size() < MAX_HEAP_SIZE){
            root.data.add(new Node<E>(item));
            return true;
        }
        int comp_sol = root.data.peek().data.compareTo(item);

        if(comp_sol > 0){
            return add_to_node(root.left, item);
        }else{
            return add_to_node(root.right, item);
        }
    }

    /**
     * Removes given element from BSTHeapTree
     * @param item item that will be removed
     * @return returns the number of occurences of the item after deletion
     * @throws NoSuchElementException
     * @throws NullPointerException
     */
    public int remove(E item) throws NoSuchElementException, NullPointerException{
        if(item == null)
            throw new NullPointerException();
        return remove_recursive(root, item);
    }

    private int remove_recursive(BSTNode<HeapPartOne<Node<E> > > root, E item){

        if(root == null)
            throw new NoSuchElementException();

        int comp_sol = root.data.peek().data.compareTo(item);

        if(comp_sol >= 0){
            for(Node<E> node : root.data){
                if(node.data.equals(item)){
                    int returnVal = --(node.occur);
                    if(node.occur == 0){
                        root.data.remove(node);
                        if(root.data.size() == 0){
                            root.data.add(node);
                            this.root = removeBSTNode(this.root, root.data);
                        }else{
                            HeapPartOne<Node<E>> newNode = new HeapPartOne<>(root.data);
                            root.data.add(node);
                            this.root = removeBSTNode(this.root, root.data);
                            for(Node<E> heapNode : newNode){
                                for(int i = 0; i < heapNode.occur; ++i)
                                    add(heapNode.data);
                            }
                        }
                    }
                    --size;
                    return returnVal;
                }
            }
            return remove_recursive(root.left, item);
        }else{
            return remove_recursive(root.right, item);
        }
    }

    /**
     * Finds the given element's number of occurence
     * @param item item that will be find
     * @return returns the number of occurence of given element in the BSTHeapTree
     * @throws NoSuchElementException
     * @throws NullPointerException
     */
    public int find(E item) throws NoSuchElementException, NullPointerException{
        if(item == null)
            throw new NullPointerException();
        Node<E> e = findNode(root, item);
        if(e == null)
            throw new NoSuchElementException();
        return e.occur;
    }
    
    /**
     * Give mode(element which has biggest number of occurence) from list
     * @return returns the mode element
     * @throws NullPointerException
     */
    public E find_mode() throws NullPointerException{
        if(root == null)
            throw new NullPointerException();
        return find_mode_recursive(root, new Node<E>()).data;
    }


    private Node<E> find_mode_recursive(BSTNode<HeapPartOne<Node<E> > > root, Node<E> max){
        if(root == null)
            return max;
        for(Node<E> node : root.data){
            if(node.occur > max.occur)
                max = node;
        }

        Node<E> check = find_mode_recursive(root.left, max);
        if(check.occur > max.occur){
            max = check;
        }

        check = find_mode_recursive(root.right, max);
        if(check.occur > max.occur){
            max = check;
        }

        return max;
    }


    private Node<E> findNode(BSTNode<HeapPartOne<Node<E> > > root, E item){

        if(root == null)
            return null;

        int comp_sol = root.data.peek().data.compareTo(item);

        if(comp_sol >= 0){
            for(Node<E> node : root.data){
                if(node.data.equals(item))
                    return node;
            }
            return findNode(root.left, item);
        }else{
            return findNode(root.right, item);
        }
    }


    


    private <T extends Comparable<T> > BSTNode<T> addBSTNode(BSTNode<T> root, T item){
        
        if(item == null)
            throw new NullPointerException();
        if(root == null){
            root = new BSTNode<T>(item);
            return root;
        }

        int comp_sol = root.data.compareTo(item);

        if(comp_sol > 0){
            root.left = addBSTNode(root.left, item);
        }
        else if(comp_sol < 0){
            root.right = addBSTNode(root.right, item);
        }

        return root;
    }

    private <T extends Comparable<T> > BSTNode<T> removeBSTNode(BSTNode<T> root, T item){
        if(root == null || item == null)
            throw new NullPointerException();
        
        int comp_sol = root.data.compareTo(item);

        if(comp_sol > 0){
            root.left = removeBSTNode(root.left, item);
        }else if(comp_sol < 0){
            root.right = removeBSTNode(root.right, item);
        }else{
            if(root.right == null && root.left == null){
                root = null;
            }else if(root.left == null){
                root = root.right;
            }else{
                if(root.right == null){
                    root = root.left;
                }else{
                    if(root.left.right == null){
                        root.left.right = root.right;
                        root = root.left;
                    }else{
                        BSTNode<T> largest = findLargestBSTNode(root.left);
                        root.data = largest.right.data;
                        largest.right = largest.right.left;
                    }
                }
            }
        }

        return root;
    }

    private <T extends Comparable<T> > BSTNode<T> findLargestBSTNode(BSTNode<T> root){
        if(root.right.right == null)
            return root;
        else{
            return findLargestBSTNode(root.right);
        }
    }

    /**
     * String represantation of BSTHeapTree
     * @return returns string represantation of BSTHeapTree
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        preOrderTraverse(root, 1, sb);
        return sb.toString();
    }

    private <T extends Comparable<T> > void preOrderTraverse(BSTNode<T> node, int depth,
                                                            StringBuilder sb) {
        for (int i = 1; i < depth; i++) {
            sb.append("  ");
        }
        if (node == null) {
            sb.append("null\n");
        }
        else {
            sb.append(node.toString());
            sb.append("\n");
            preOrderTraverse(node.left, depth + 1, sb);
            preOrderTraverse(node.right, depth + 1, sb);
        }
    }
}