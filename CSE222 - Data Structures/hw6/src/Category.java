import java.util.ArrayList;
import java.util.List;

public class Category implements Comparable<Category>{
    private String name;
    private List<Category> subCateg = new ArrayList<>();
    private List<Product> product = new ArrayList<>();
    
    public Category(String name){
        if(name == null)
            throw new NullPointerException();

        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        if(name == null)
            throw new NullPointerException();
        this.name = name;
    }

    /**
     * To get products of this category
     * @return returns the arraylist of products, if it doesnt find it return empty list
     */
    public ArrayList<Product> getProducts(){
        ArrayList<Product> returnVal = new ArrayList<>();
        getProductsRec(returnVal, this);

        return returnVal;
    }

    private void getProductsRec(ArrayList<Product> arr, Category categ){
        arr.addAll(categ.product);
        for(int i = 0; i < categ.subCateg.size(); ++i){
            getProductsRec(arr, categ.subCateg.get(i));
        }
    }

    /**
     * To add new sub category
     * @param categ new sub category
     * @return return true if the sub category not added before
     */
    public boolean addSubCategory(Category categ){
        if(!subCateg.contains(categ))
            return subCateg.add(categ);
        return false;
    }

    /**
     * Adds product to the category
     * @param prod product that will be added
     */
    public void addProduct(Product prod){
        product.add(prod);
    }

    @Override
    public int compareTo(Category o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Category))
            return false;
        
        Category other = (Category) o;

        return this.name.equals(other.name);
    }


    @Override
    public String toString() {
        return getName();
    }

}
