import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map.Entry;

public class TraderResult implements Result{
    private TreeSet<Category> categTree;
    private HashMap<String, Product> traderProducts;


    public TraderResult(HashMap<String,Product> traderProducts, TreeSet<Category> categTree) {
        this.categTree = categTree;
        this.traderProducts = traderProducts;
    }

    @Override
    public String display(){
        StringBuilder strBuild = new StringBuilder();

        Iterator<Entry<String, Product>> iter = traderProducts.entrySet().iterator();
        int i = 0;
        while(iter.hasNext()){
            Product prod = iter.next().getValue();
            strBuild.append((i + 1) + ") ").append(prod.getId() + " | ").
                append(prod.getName() + " | ").append(prod.getPrice() + " -> ").
                append(prod.getDisc() + " | ").append("Discount: %" + prod.getDiscPercen()).append("\n"); 
            i++;
        }

        return strBuild.toString();
    }

    public TreeSet<Category> getCategTree() {
        return this.categTree;
    }

    public HashMap<String, Product> getProducts() {
        return this.traderProducts;
    }
}
