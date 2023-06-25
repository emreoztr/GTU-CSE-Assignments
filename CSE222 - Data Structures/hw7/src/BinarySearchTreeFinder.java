public abstract class BinarySearchTreeFinder {
    private static boolean avl = true;
    private static boolean rbt = true;

    private BinarySearchTreeFinder() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * To check binary search tree
     * @param tree tree that will be checked
     * @return return 0 if it is avl, returns 1 if it is red black tree, returns 2 if it can be both, returns 3 if neither of them
     * @throws NullPointerException
     */
    public static <E extends Comparable<E>> int whichTree(BinarySearchTree<E> tree) throws NullPointerException{
        if(tree == null)
            throw new NullPointerException();
        isAVL(tree);
        isRedBlack(tree);

        if(avl && !rbt) return 0;
        else if(!avl && rbt) return 1;
        else if(avl && rbt) return 2;
        else return 3;
    }

    private static <E extends Comparable<E>> boolean isAVL(BinarySearchTree<E> tree){
        avl = true;
        isAVLRec(tree);
        return avl;
    }

    private static <E extends Comparable<E>> boolean isRedBlack(BinarySearchTree<E> tree){
        rbt = true;
        if(tree.isRed())
            rbt = false;
        else
            isRedBlackRec(tree);

        return rbt;
    }

    private static <E extends Comparable<E>> int isAVLRec(BinaryTree<E> root){
        if(root == null)
            return 0;
        
        int left = isAVLRec(root.getLeftSubtree());
        int right = isAVLRec(root.getRightSubtree());

        if(Math.abs(left - right) >= 2)
            avl = false;
        
        if(left > right)
            return left + 1;
        else
            return right + 1;
    }

    private static <E extends Comparable<E>> int isRedBlackRec(BinarySearchTree<E> root){
        if(root == null)
            return 0;

        BinarySearchTree<E> left = (BinarySearchTree<E>) root.getLeftSubtree();
        int leftBlack = 0;
        BinarySearchTree<E> right = (BinarySearchTree<E>) root.getRightSubtree();
        int rightBlack = 0;

        if(root.isRed()){
            if(left != null && right != null){
                if(left.isRed() || right.isRed())
                    rbt = false;
            }else if(left != null){
                if(left.isRed())
                    rbt = false;
            }else if(right != null){
                if(right.isRed())
                    rbt = false;
            }
        }
            leftBlack = isRedBlackRec(left);
            rightBlack = isRedBlackRec(right);
            if(rightBlack != leftBlack)
                rbt = false;
        
        if(!root.isRed())
            return (leftBlack + 1);
        return leftBlack;
    }
}
