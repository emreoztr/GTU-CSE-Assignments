import java.util.Collection;
import java.util.TreeSet;

public interface Result {
    /**
     * To display results
     * @return returns String to display
     */
    public String display();

    /**
     * To get category tree
     * @return returns category tree
     */
    public TreeSet<Category> getCategTree();
}
