import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ProductResult implements Result{
    private TreeSet<Category> categTree;
    private ArrayList<Product> productList;

    /**
     * Holds the product informatino and category tree
     * @param categTree Category tree will be used to store categories
     * @param productList productlist to store product results
     */
    public ProductResult(TreeSet<Category> categTree, ArrayList<Product> productList){
        this.categTree = categTree;
        this.productList = productList;
    }

    @Override
    public String display(){
        StringBuilder strBuild = new StringBuilder();
        ArrayList<Product> list = productList;
        for(int i = 0; i < list.size(); ++i){
            strBuild.append((i + 1) + ") ").append(list.get(i).getId() + " | ").
                append(list.get(i).getName() + " | ").append(list.get(i).getPrice() + " -> ").
                append(list.get(i).getDisc() + " | ").append("Discount: %" + list.get(i).getDiscPercen()).append("\n"); 
        }
        return strBuild.toString();
    }

    public TreeSet<Category> getCategTree() {
        return this.categTree;
    }

    public ArrayList<Product> getProducts() {
        return this.productList;
    }
}
