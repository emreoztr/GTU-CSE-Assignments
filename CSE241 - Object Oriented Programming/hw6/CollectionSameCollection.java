@SuppressWarnings("serial")
public class CollectionSameCollection extends RuntimeException{
    public CollectionSameCollection() {
        System.out.println("Cant use same container of itself!");
    }
}
