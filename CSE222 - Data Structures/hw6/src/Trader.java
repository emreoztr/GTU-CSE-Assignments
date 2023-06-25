import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Trader extends User{
    private TraderResult traderProducts;
    private Queue<Order> orders;

    /**
     * Trader object to control trader operations
     * @param comp
     * @param id
     * @param name
     * @param pass
     * @throws IOException
     */
    public Trader(AppSystem comp, String id, String name, String pass) throws IOException{
        super(comp, id, name, pass);
        traderProducts = getAppSystem().searchTraderGoods(this.getName());
        orders = getAppSystem().findTraderOrders(this.getName());
    }
    
    public Result getProducts(){
        return traderProducts;
    }

    /**
     * To check last given order
     * @return returns the string representation of order
     */
    public String checkLastOrder(){
        StringBuilder strBuild = new StringBuilder();
        Order order = orders.peek();
        if(order == null)
            return null;
        strBuild.append("Customer ID: ").append(order.getCustID()).append("\n").
        append("Trader Name: ").append(order.getTrader()).append("\n").
        append("Product Name: ").append(traderProducts.getProducts().get(order.getProdID()).getName()).append("\n");

        return strBuild.toString();
    }

    /**
     * To decide cancel or met the product
     * @param accept if it is true order will be accepted, if it is false it will be cancelled
     * @throws IOException if file have some errors
     */
    public void checkOrder(boolean accept) throws IOException{
        Order order = orders.poll();
        if(order != null){
            if(accept)
                getAppSystem().changeOrderState(order, "ACCEPTED");
            else
                getAppSystem().changeOrderState(order, "CANCELLED");
        }
    }
    
    /**
     * To add product to the database
     * @param product product that will be added
     * @throws IOException in case of a file error
     */
    public void addProduct(Product product) throws IOException{
        if(getAppSystem().searchProductID(product.getId()).getProducts().isEmpty()){
            getAppSystem().addToDataBase(product);
            traderProducts.getProducts().put(product.getId(), product);
        }
    }

    /**
     * Removes the product with given id
     * @param id product which has this id will be removed
     * @throws IOException in case of a file error
     */
    public void removeProduct(String id) throws IOException{
        if(id == null)
            throw new NullPointerException();
        
        getAppSystem().removeProduct(id);
        traderProducts.getProducts().remove(id);
    }

    /**
     * To edit the string part of the product
     * @param id
     * @param str
     * @param part
     * @throws IOException
     */
    public void editProductInfo(String id, String str, DataEnum part) throws IOException{
        Product product = traderProducts.getProducts().get(id);
        getAppSystem().removeProduct(id);
        if(part.equals(DataEnum.NAME))
            product.setName(str);
        else if(part.equals(DataEnum.DESCRIPTION))
            product.setDesc(str);
        getAppSystem().addToDataBase(product);
    }

    /**
     * To edit the numeric part of the product
     * @param id
     * @param price
     * @param disc
     * @return
     * @throws IOException
     */
    public boolean editProductPrice(String id, double price, double disc) throws IOException{
        Product prod = traderProducts.getProducts().get(id);
        boolean returnVal = false;
        getAppSystem().removeProduct(id);
        if(price >= disc){
            prod.setPrice(price);
            prod.setDisc(disc);
            returnVal = true;
        }
        getAppSystem().addToDataBase(prod);

        return returnVal;
    }

    /**
     * To edit the category tree of a product
     * @param id
     * @param categList
     * @return
     * @throws IOException
     */
    public boolean editCategoryTree(String id, LinkedList<Category> categList) throws IOException{
        if(id == null || categList == null)
            throw new NullPointerException();

        if(categList.isEmpty())
            return false;
        getAppSystem().removeProduct(id);
        Product prod = traderProducts.getProducts().get(id);
        prod.setCategTree(categList);
        getAppSystem().addToDataBase(prod);
        return true;
    }

    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(getId()).append(";").append(getName()).append(";").append(getPassword());
        return strBuild.toString();
    }
}
