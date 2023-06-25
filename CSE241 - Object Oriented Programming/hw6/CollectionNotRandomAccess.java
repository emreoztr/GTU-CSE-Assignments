
@SuppressWarnings("serial")
public class CollectionNotRandomAccess extends RuntimeException {
    public CollectionNotRandomAccess() {
        System.out.println("This collection has not random access capability!");
    }
}

