import java.util.Iterator;
import java.util.LinkedList;

public class Product implements Comparable<Product>{
    private String id;
    private String name;
    private Category category;
    private LinkedList<Category> categTree;
    private double price;
    private double disc;
    private String desc;
    private String trader;
    private double discPercen;

    /**
     * To hold the product information
     * @param id
     * @param name
     * @param category
     * @param categTree
     * @param price
     * @param disc
     * @param desc
     * @param trader
     */
    public Product(String id, String name, Category category, LinkedList<Category> categTree, double price, double disc, String desc, String trader){
        if(id == null || name == null || category == null || categTree == null || desc == null || trader == null)
            throw new NullPointerException();
        this.id = id;
        this.name = name;
        this.category = category;
        this.categTree = categTree;
        this.price = price;
        this.disc = disc;
        this.desc = desc;
        this.trader = trader;

        discPercen = (100.0) * (price - disc) / price;
    }

    public LinkedList<Category> getCategTree() {
        return this.categTree;
    }

    public void setCategTree(LinkedList<Category> categTree) {
        this.categTree = categTree;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getPrice() {
        return this.price;
    }

    public double getDiscPercen() {
        return this.discPercen;
    }

    public void setDiscPercen(double discPercen) {
        this.discPercen = discPercen;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDisc() {
        return this.disc;
    }

    public void setDisc(double disc) {
        this.disc = disc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTrader() {
        return this.trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }
    

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(getId()).append(";").
            append(getName()).append(";").
            append(AppSystem.categTreeToString(getCategTree())).append(";").
            append(getPrice()).append(";").
            append(getDisc()).append(";").
            append(getDesc()).append(";").
            append(getTrader());
        return strBuild.toString();
    }

    @Override
    public int compareTo(Product o) {
        // TODO Auto-generated method stub
        return 0;
    }

    

}
