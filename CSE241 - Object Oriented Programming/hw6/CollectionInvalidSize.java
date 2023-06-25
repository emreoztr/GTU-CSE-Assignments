
@SuppressWarnings("serial")
public class CollectionInvalidSize extends RuntimeException {
    public CollectionInvalidSize() {
        System.out.println("Size of the collection cant be smaller or equal to zero!");
    }
}

