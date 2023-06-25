import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Queue;


public class Customer extends User{
    private ProductResult searchResult = null;
    private Queue<Order> orders = new ArrayDeque<>();

    public Customer(AppSystem comp, String id, String name, String pass){
        super(comp, id, name, pass);
    }

    /**
     * To display search results
     * @return returns the string for display
     */
    public String displayResult(){
        StringBuilder strBuild = new StringBuilder();
        ArrayList<Product> list = searchResult.getProducts();
        for(int i = 0; i < list.size(); ++i){
            strBuild.append((i + 1) + ") ").append(list.get(i).getId() + " | ").
                append(list.get(i).getName() + " | ").append(list.get(i).getPrice() + " -> ").
                append(list.get(i).getDisc() + " | ").append("Discount: %" + list.get(i).getDiscPercen()).append("\n"); 
        }
        return strBuild.toString();
    }

    /**
     * Searches the product in database
     * @param name keyword that will be used in search
     * @return returns the results with holder object
     */
    public ProductResult searchProduct(String name){
        if(name == null)
            throw new NullPointerException();

        searchResult = getAppSystem().searchDataBase(name);
        return searchResult;
    }

    /**
     * Sorts the results that searched before, by giving the part name and increasing or decreasing order
     * @param part part name will be looked
     * @param order to determine increasing or decreasing order
     */
    public void sortResults(DataEnum part, DataEnum order){
        if(part == DataEnum.PRICE){
            Comparator<Product> compar = null;
            if(order == DataEnum.INCREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        return (int)o1.getPrice() - (int)o2.getPrice();
                    }
                };
            else if(order == DataEnum.DECREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        return (int)o2.getPrice() - (int)o1.getPrice();
                    }
                };
            
            SortAlgorithm.quick(searchResult.getProducts(), compar);
        }
        else if(part == DataEnum.NAME){
            Comparator<Product> compar = null;
            if(order == DataEnum.INCREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        return o2.getName().toLowerCase().compareTo(o1.getName().toLowerCase());
                    }
                };
            else if(order == DataEnum.DECREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                };
            
            SortAlgorithm.mergeSort(searchResult.getProducts(), compar);
        }
        else if(part == DataEnum.DISCOUNT_PERCEN){
            Comparator<Product> compar = null;
            if(order == DataEnum.INCREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        if(o1.getDiscPercen() > o2.getDiscPercen())
                            return -1;
                        else if(o1.getDiscPercen() < o2.getDiscPercen())
                            return 1;
                        else
                            return 0;
                    }
                };
            else if(order == DataEnum.DECREASING)
                compar = new Comparator<Product>(){
                    @Override
                    public int compare(Product o1, Product o2){
                        if(o1.getDiscPercen() > o2.getDiscPercen())
                            return 1;
                        else if(o1.getDiscPercen() < o2.getDiscPercen())
                            return -1;
                        else
                            return 0;
                    }
                };
            
            SortAlgorithm.heapSort(searchResult.getProducts(), compar);
        }
    }

    /**
     * To add order to order list
     * @param ind index of the item that will be added
     */
    public void makeOrder(int ind){
        Product product = searchResult.getProducts().get(ind);
        Order order = new Order(getId(), product.getTrader(), product.getId(), "WAIT");
        orders.offer(order);
    }

    /**
     * To finish the order and save it to the database
     * @throws IOException if there is a file error
     */
    public void finishOrder() throws IOException{
        while(!orders.isEmpty()){
            getAppSystem().addOrderToDataBase(orders.poll());
        }
    }

    /**
     * To clear order list before saving it to the database
     */
    public void clearOrder(){
        orders.clear();
    }

    /**
     * Filters the categories by given category, after filter results should only have given category and it's sub categories.
     * @param categ category that will be filtered
     * @return returns the new product results
     */
    public ProductResult filterCategory(Category categ){
        ArrayList<Product> arr = new ArrayList<>();
        if(searchResult != null){
            Category mainCateg = searchResult.getCategTree().floor(categ);
            if(mainCateg.equals(categ)){
                searchResult = new ProductResult(searchResult.getCategTree(), mainCateg.getProducts());
                return searchResult;
            }
        }
        return null;
    }
    
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(getId()).append(";").append(getName()).append(";").append(getPassword());
        return strBuild.toString();
    }
}
