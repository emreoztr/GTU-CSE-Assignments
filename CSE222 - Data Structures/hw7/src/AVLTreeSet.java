import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AVLTreeSet<E extends Comparable<E>> implements NavigableSet<E> {
  private boolean increase;
  private AVLNode<E> root;

  /** Class to represent an AVL Node. It extends the
      BinaryTree.Node by adding the balance field. */
  private static class AVLNode < E >{
    private E data;

    private AVLNode < E > left;

    private AVLNode < E > right;

    public static final int LEFT_HEAVY = -1;

    public static final int BALANCED = 0;

    public static final int RIGHT_HEAVY = 1;

    private int balance;

    // Methods
    /** Construct a node with the given item as the data field.
        @param item The data field
     */
    public AVLNode(E item) {
      data = item;
      left = null;
      right = null;
      balance = BALANCED;
    }

    /** Return a string representation of this object.
        The balance value is appended to the contents.
        @return String representation of this object
     */
    public String toString() {
      return balance + ": " + data.toString();
    }
  }
    /**
     * To insert new item to AVLTreeSet
     * @param item item will be inserted
     * @return returns insertion result
     */
    public boolean insert(E item){
        AVLNode<E> val = insertRec(root, item);
        if(val == null)
            return false;
        else{
            root = val;
            return true;
        }
    }

    private AVLNode<E> insertRec(AVLNode<E> root, E item){
        if(root == null){
            increase = true;
            return new AVLNode<>(item);
        }
        if(root.data.equals(item)){
            increase = false;
            return null;
        }

        if(item.compareTo(root.data) < 0){
            AVLNode<E> val = insertRec(root.left, item);
            
            if(val == null)
                return null;
            root.left = val;
            if(increase){
              root.balance--;
              if(root.balance == AVLNode.BALANCED) increase = false;
              if(root.balance < AVLNode.LEFT_HEAVY){
                  increase = false;
                  return rebalanceLeft(root);
              }
            }
        }else{
            AVLNode<E> val = insertRec(root.right, item);
            
            if(val == null)
                return null;
            root.right = val;
            if(increase){
              root.balance++;
              if(root.balance == AVLNode.BALANCED) increase = false;
              if(root.balance > AVLNode.RIGHT_HEAVY){
                increase = false;
                return rebalanceRight(root);
              }
            }
        }
        return root;
    }



    private class SetIter implements Iterator<E>{
        private List<E> items = null;
        private Iterator<E> innerIter = null;
        private E lastItem = null;

        public SetIter(){
            items = inOrderTraversal(root);
            innerIter = items.iterator();
        }

        @Override
        public boolean hasNext() {
            return innerIter.hasNext();
        }

        @Override
        public E next() {
            lastItem = innerIter.next();
            return lastItem;
        }
        
        @Override
        public void remove() throws IllegalStateException{
            if(lastItem == null)
                throw new IllegalStateException();
            AVLTreeSet.this.remove(lastItem);
            innerIter.remove();
            lastItem = null;
        }
    }

    private List<E> inOrderTraversal(AVLNode<E> node){
        if(node == null)
            return null;
        List<E> returnVal = new ArrayList<>();
        List<E> subTree = inOrderTraversal(node.left);
        if(subTree != null)
            returnVal.addAll(subTree);
        returnVal.add(node.data);
        subTree = inOrderTraversal(node.right);
        if(subTree != null)
            returnVal.addAll(subTree);
        return returnVal;
    } 
    // Insert nested class AVLNode<E> here.

    
    private NavigableSet<E> subSet(AVLNode<E> node, E head, boolean headInc, E tail, boolean tailInc){
        NavigableSet<E> returnVal = new TreeSet<>();
        if(node == null)
            return returnVal;
        if(head == null && tail != null){
            returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
            if(tailInc){
                if(node.data.compareTo(tail) <= 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }
            }else{
                if(node.data.compareTo(tail) < 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }
            }
        }
        else if(head != null && tail == null){
            returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
            if(headInc){
                if(node.data.compareTo(head) >= 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                }
            }else{
                if(node.data.compareTo(head) > 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                }
            }
        }
        else if(head != null && tail != null){
            if(headInc && tailInc){
                if(node.data.compareTo(head) >= 0 && node.data.compareTo(tail) <= 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }else if(node.data.compareTo(head) >= 0)
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                else if(node.data.compareTo(tail) <= 0)
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
            }
            else if(headInc){
                if(node.data.compareTo(head) >= 0 && node.data.compareTo(tail) < 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }else if(node.data.compareTo(head) >= 0)
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                else if(node.data.compareTo(tail) < 0)
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
            }
            else if(tailInc){
                if(node.data.compareTo(head) > 0 && node.data.compareTo(tail) <= 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }else if(node.data.compareTo(head) > 0)
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                else if(node.data.compareTo(tail) <= 0)
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
            }
            else{
                if(node.data.compareTo(head) > 0 && node.data.compareTo(tail) < 0){
                    returnVal.add(node.data);
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
                }else if(node.data.compareTo(head) > 0)
                    returnVal.addAll(subSet(node.left, head, headInc, tail, tailInc));
                else if(node.data.compareTo(tail) < 0)
                    returnVal.addAll(subSet(node.right, head, headInc, tail, tailInc));
            }
        }
        return returnVal;
    }
    
    /**
     * Subset of given edges
     * @param head head item
     * @param headInc include head or not
     * @param tail tail item
     * @param tailInc include tail or not
     * @return returns set by given parameters
     */
    public NavigableSet<E> subSet(E head, boolean headInc, E tail, boolean tailInc){
        return subSet(root, head, headInc, tail, tailInc);
    }

    /**
     * Headset by given parameter
     * @param tail tail item
     * @param tailInc include tail or not
     * @return returns set by given parameters
     */
    @Override
    public NavigableSet<E> headSet(E tail, boolean tailInc){
        return subSet(root, null, false, tail, tailInc);
    }

    /**
     *Tailset by given parameter
     * @param head head item
     * @param headInc include head or not
     * @return returns set by given parameters
     */
    @Override
    public NavigableSet<E> tailSet(E head, boolean headInc){
        return subSet(root, head, headInc, null, false);
    }
    
    /**
     * Headset by given parameter
     * @param toElement element to check
     * @return returns set by given parameters
     */
    @Override
    public SortedSet<E> headSet(E toElement) {
        return headSet(toElement, false);
    }

    /**
     *Tailset by given parameter
     * @param fromElement element to check
     * @return returns set by given parameters
     */
    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return tailSet(fromElement, true);
    }
    /**
     * To take iterator for travel
     * @return returns the iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new SetIter();
    }
  

  /** rebalanceRight
         @pre localRoot is the root of an AVL subtree that is
         more than one right heavy.
         @post balance is restored and increase is set false
         @param localRoot Root of the AVL subtree that needs rebalancing
         @return a new localRoot
   */
  private AVLNode < E > rebalanceRight(AVLNode < E > localRoot) {
    // Obtain reference to right child
    AVLNode < E > rightChild = localRoot.right;
    // See if right-left heavy
    if (rightChild.balance < AVLNode.BALANCED) {
      // Obtain reference to right-left child
      AVLNode < E > rightLeftChild = rightChild.left;

      if (rightLeftChild.balance > AVLNode.BALANCED) {
        rightChild.balance = AVLNode.BALANCED;
        rightLeftChild.balance = AVLNode.BALANCED;
        localRoot.balance = AVLNode.BALANCED;
      }
      else if (rightLeftChild.balance < AVLNode.BALANCED) {
        rightChild.balance = AVLNode.BALANCED;
        rightLeftChild.balance = AVLNode.BALANCED;
        localRoot.balance = AVLNode.BALANCED;
      }
      else {
        rightChild.balance = AVLNode.BALANCED;
        rightLeftChild.balance = AVLNode.BALANCED;
        localRoot.balance = AVLNode.BALANCED;
      }
      // Perform double rotation
      localRoot.right = rotateRight(rightChild);
      return rotateLeft(localRoot);
    }
    else {
      rightChild.balance = AVLNode.BALANCED;
      localRoot.balance = AVLNode.BALANCED;
      // Now rotate the
      return rotateLeft(localRoot);
    }
  }

 

  /** Method to rebalance left.
    pre: localRoot is the root of an AVL subtree that is
         critically left-heavy.
    post: Balance is restored.
    @param localRoot Root of the AVL subtree
           that needs rebalancing
    @return a new localRoot
   */
  private AVLNode < E > rebalanceLeft(AVLNode < E > localRoot) {
    // Obtain reference to left child.
    AVLNode < E > leftChild = localRoot.left;
    // See whether left-right heavy.
    if (leftChild.balance > AVLNode.BALANCED) {
      // Obtain reference to left-right child.
      AVLNode < E > leftRightChild = leftChild.right;
      /** Adjust the balances to be their new values after
          the rotations are performed.
       */
      if (leftRightChild.balance < AVLNode.BALANCED) {
        leftChild.balance = AVLNode.BALANCED;
        leftRightChild.balance = AVLNode.BALANCED;
        localRoot.balance = AVLNode.BALANCED;
      }
      else {
        leftChild.balance = AVLNode.BALANCED;
        leftRightChild.balance = AVLNode.BALANCED;
        localRoot.balance = AVLNode.BALANCED;
      }
      // Perform left rotation.
      localRoot.left = rotateLeft(leftChild);
    }
    else { //Left-Left case
      /** In this case the leftChild (the new root)
          and the root (new right child) will both be balanced
          after the rotation.
       */
      leftChild.balance = AVLNode.BALANCED;
      localRoot.balance = AVLNode.BALANCED;
    }
    // Now rotate the local root right.
    return rotateRight(localRoot);
  }


    /** Method to perform a right rotation.
        pre:  root is the root of a binary search tree.
        post: root.right is the root of a binary search tree,
              root.right.right is raised one level,
              root.right.left does not change levels,
              root.left is lowered one level,
              the new root is returned.
        @param root The root of the binary tree to be rotated
        @return The new root of the rotated tree
    */
    protected AVLNode < E > rotateRight(AVLNode < E > root) {
      AVLNode < E > temp = root.left;
      root.left = temp.right;
      temp.right = root;
      return temp;
    }


  /** rotateLeft
          pre:  localRoot is the root of a binary search tree
          post: localRoot.right is the root of a binary search tree
                localRoot.right.right is raised one level
                localRoot.right.left does not change levels
                localRoot.left is lowered one level
                the new localRoot is returned.
          @param localRoot The root of the binary tree to be rotated
          @return the new root of the rotated tree
  */
  protected AVLNode < E > rotateLeft(AVLNode < E > localRoot) {
    AVLNode < E > temp = localRoot.right;
    localRoot.right = temp.left;
    temp.left = localRoot;
    return temp;
  }


  public String toString(){
      StringBuilder strBuild = new StringBuilder();
      preOrderTraverse(root, 0, strBuild);
      return strBuild.toString();
  }

  /** Perform a preorder traversal.
      @param node The local root
      @param depth The depth
      @param sb The string buffer to save the output
   */
  private void preOrderTraverse(AVLNode < E > node, int depth,
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
  
    @Override
    public Comparator<? super E> comparator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E first() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E last() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean contains(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean remove(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public E lower(E e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E floor(E e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E ceiling(E e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E higher(E e) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E pollFirst() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public E pollLast() {
        // TODO Auto-generated method stub
        return null;
    }

    

    @Override
    public NavigableSet<E> descendingSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<E> descendingIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        // TODO Auto-generated method stub
        return null;
    }

    

    @Override
    public boolean add(E e) {
      // TODO Auto-generated method stub
      return false;
    }
    
}
