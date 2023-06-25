import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;

public class AppSystem {
    private String productFile;
    private String countFile;
    private String userFile;
    private String orderFile;
    private int customerCount;
    private int traderCount;
    private int productCount;

    private enum DataPart{
        ID(0),
        NAME(1),
        CATEGORY(2),
        PRICE(3),
        DISC(4),
        DESC(5),
        TRADER(6);

        private final int val;
        private DataPart(int val){
            this.val = val;
        }
        public int getValue(){
            return val;
        }
    }

    /**
     * To create and connect the automation system
     * @param productFile Product file address
     * @param countFile Count file address
     * @param userFile User file adress
     * @param orderFile order file adress
     * @throws NumberFormatException
     * @throws IOException
     */
    public AppSystem(String productFile, String countFile, String userFile, String orderFile) throws NumberFormatException, IOException{
        if(productFile == null || countFile == null || userFile == null || orderFile == null)
            throw new NullPointerException();
        
        this.productFile = productFile;
        this.countFile = countFile;
        this.userFile = userFile;
        this.orderFile = orderFile;

        BufferedReader fread = new BufferedReader(new FileReader(countFile));
        customerCount = Integer.parseInt(fread.readLine());
        productCount = Integer.parseInt(fread.readLine());
        traderCount = Integer.parseInt(fread.readLine());
        IDBuilder.setProductCount(productCount);
        IDBuilder.setCustomerCount(customerCount);
        IDBuilder.setTraderCount(traderCount);
        fread.close();
    }

    /**
     * To save a customer in the system
     * @param name name of the customer
     * @param pass password of the customer
     * @return returns id of the new customer
     * @throws IDLimitException if id limit not enough
     * @throws IOException if there is a file error
     */
    public String registerCustomer(String name, String pass) throws IDLimitException, IOException{
        if(name == null || pass == null)
            throw new NullPointerException();

        if(searchUser(name, DataEnum.NAME) == null){
            Customer c = new Customer(this, IDBuilder.generateCustomerID(), name, pass);
            addLine(userFile, c.toString());
            customerCount++;
            updateCountFile();
            return c.getId();
        }
        return null;
    }

    /**
     * To save a trader in the system
     * @param name name of the trader
     * @param pass password of the trader
     * @return returns id of the new trader
     * @throws IDLimitException if id limit not enough
     * @throws IOException if there is a file error
     */
    public String registerTrader(String name, String pass) throws IDLimitException, IOException{
        if(name == null || pass == null)
            throw new NullPointerException();
        if(searchUser(name, DataEnum.NAME) == null){
            Trader c = new Trader(this, IDBuilder.generateTraderID(), name, pass);
            addLine(userFile, c.toString());
            traderCount++;
            updateCountFile();
            return c.getId();
        }
        return null;
    }

    /**
     * To login the user to the system
     * @param id ID of the user
     * @param pass password of the user
     * @return return the user
     */
    public User login(String id, String pass){
        User u = searchUser(id, DataEnum.ID);
        if(u == null)
            return null;
        if(u.getPassword().equals(pass))
            return u;
        else
            return null;
    }

    private void updateCountFile() throws IOException{
        File file = new File(countFile);
        FileWriter fw = new FileWriter(file);
        fw.append(String.valueOf(customerCount));
        fw.append("\n");
        fw.append(String.valueOf(productCount));
        fw.append("\n");
        fw.append(String.valueOf(traderCount));
        fw.close();
    }
    
    /**
     * Searches the product file for keyword.
     * @param keyWord keyword that will be searched
     * @return returns the results within a holder object
     */
    public ProductResult searchDataBase(String keyWord){
        ProductResult returnVal = new ProductResult(new TreeSet<>(), new ArrayList<>());
        FileSplitter fsplit;
        try {
        fsplit = new FileSplitter(productFile, ";");
        List<String> line;
        line = fsplit.nextLine();
        while(!line.isEmpty()){
            if(line.get(DataPart.NAME.getValue()).toLowerCase().contains(keyWord.toLowerCase()) || 
            line.get(DataPart.DESC.getValue()).toLowerCase().contains(" " + keyWord.toLowerCase() + " ")){
                Product prod = stringListToProduct(line, returnVal);
                prod.getCategory().addProduct(prod);
                returnVal.getProducts().add(prod);
            }
            line =  fsplit.nextLine();
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException r){
            r.printStackTrace();
        }
        return returnVal;
    }

    /**
     * To search the products of a trader
     * @param trader trader name that will be searched
     * @return returns the products within a holder object
     */
    public TraderResult  searchTraderGoods(String trader){
       TraderResult  returnVal = new TraderResult (new HashMap<>(), new TreeSet<>());

        FileSplitter fsplit;
        try {
        fsplit = new FileSplitter(productFile, ";");
        List<String> line;
        line = fsplit.nextLine();
        while(!line.isEmpty()){
            if(line.get(DataPart.TRADER.getValue()).equals(trader)){
                Product prod = stringListToProduct(line, returnVal);
                returnVal.getProducts().put(prod.getId(), prod);
            }
            line =  fsplit.nextLine();
        }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException r){
            r.printStackTrace();
        }
        return returnVal;
    }

    /**
     * Search the product with id.
     * @param id id that will be searched
     * @return returns empty data if it couldn't find, if it finds it returns the product
     */
    public ProductResult searchProductID(String id){
        ProductResult returnVal = new ProductResult(new TreeSet<>(), new ArrayList<>());
        FileSplitter fsplit;
        try {
            fsplit = new FileSplitter(productFile, ";");
            List<String> line;
            line = fsplit.nextLine();
            while(!line.isEmpty()){
                if(line.get(DataPart.ID.getValue()).equals(id)){
                    Product prod = stringListToProduct(line, returnVal);
                    returnVal.getProducts().add(prod);
                }
                line =  fsplit.nextLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException r){
            r.printStackTrace();
        }
        return returnVal;
    }

    /**
     * To add product to text data.
     * @param prod product that will be added to data
     * @throws IOException if there is a file error
     */
    public void addToDataBase(Product prod) throws IOException{
        File file = new File("temp.txt");
        File file_2 = new File(productFile);
        BufferedWriter fwrite = new BufferedWriter(new FileWriter(file));
        BufferedReader fread = new BufferedReader(new FileReader(file_2));
        String lineTemp = fread.readLine();
        boolean found = false;
        productCount++;
        updateCountFile();
        while(lineTemp != null){
            if(!found && prod.getName().compareTo(splitString(lineTemp, ";").get(DataPart.NAME.getValue())) < 0){
                fwrite.write(prod.toString());
                fwrite.newLine();
                found = true;
            }
            fwrite.write(lineTemp);
            fwrite.newLine();
            lineTemp = fread.readLine();
        }
        if(lineTemp != null)
            fwrite.write(lineTemp);
        if(!found)
            fwrite.write(prod.toString());
        fwrite.close();
        fread.close();
        file_2.delete();
        file.renameTo(file_2);
    }

    /**
     * Search user by name or id depending on given parameters
     * @param str string that will be searched for name or id
     * @param searchBy to choose searching by id or name
     * @return returns the user if it finds, if not it returns null
     */
    public User searchUser(String str, DataEnum searchBy){
        try {
            FileSplitter fsplit = new FileSplitter(userFile, ";");
            int searchInd;

            switch(searchBy){
                case ID:
                    searchInd = 0;
                    break;
                case NAME:
                    searchInd = 1;
                    break;
                default:
                    throw new IllegalArgumentException();
            }

            List<String> line = fsplit.nextLine();
            while(!line.isEmpty()){
                if(line.get(searchInd).equals(str)){
                    if(line.get(0).charAt(0) == 'T')
                        return new Trader(this, line.get(0), line.get(1), line.get(2));
                    else if(line.get(0).charAt(0) == 'C')
                        return new Customer(this, line.get(0), line.get(1), line.get(2));
                }

                line = fsplit.nextLine();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        
    }

    private LinkedList<Category> addCategory(String categTree, Result result){
        LinkedList<Category> returnVal = new LinkedList<>();
        categTree = categTree.replace("\"[\"\"", "");
        categTree = categTree.replace("\"\"]\"", "");

        List<String> splittedCateg = Arrays.asList(categTree.split(" >> "));
        Category categ = null;
        for(String categStr : splittedCateg){
            categ = new Category(categStr);
            Category temp = result.getCategTree().ceiling(categ);
            if(temp != null && temp.equals(categ))
                categ = temp;
            else
                result.getCategTree().add(categ);

            if(!returnVal.isEmpty())
                returnVal.getLast().addSubCategory(categ);
            returnVal.add(categ);
        }
        return returnVal;
    }

    public static String categTreeToString(LinkedList<Category> categTree){
        StringBuilder strBuild = new StringBuilder();

        strBuild.append("\"[\"\"");
        Iterator<Category> iter = categTree.iterator();
        while(iter.hasNext()){
            strBuild.append(iter.next().toString());
            if(iter.hasNext())
                strBuild.append(" >> ");
        }
        strBuild.append("\"\"]\"");

        return strBuild.toString();
    }

    private Product stringListToProduct(List<String> line, Result result){
        LinkedList<Category> categTree = addCategory(line.get(DataPart.CATEGORY.getValue()), result);
        return new Product(
            line.get(DataPart.ID.getValue()), 
            line.get(DataPart.NAME.getValue()),
            categTree.getLast(),
            categTree,  
            Double.parseDouble(line.get(DataPart.PRICE.getValue())), 
            Double.parseDouble(line.get(DataPart.DISC.getValue())), 
            line.get(DataPart.DESC.getValue()), 
            line.get(DataPart.TRADER.getValue()));
    }

    /**
     * Adds new order to order file
     * @param order order that will be added
     * @throws IOException if there is an error while opening file
     */
    public void addOrderToDataBase(Order order) throws IOException{
        addLine(orderFile, order.toString());
    }

    private void addLine(String filename, String line) throws IOException{
        File file = new File(filename);
        BufferedWriter fwrite = new BufferedWriter(new FileWriter(file, true));

        
        fwrite.append(line);
        fwrite.append("\n");
        fwrite.close();
    }

    /**
     * To change order state from "wait" to "accepted" or "cancelled"
     * @param order order that will
     * @param state new state for order
     * @throws IOException if there is an error while opening file
     */
    public void changeOrderState(Order order, String state) throws IOException{
        File file = new File("temp.txt");
        File file_2 = new File(orderFile);
        BufferedWriter fwrite = new BufferedWriter(new FileWriter(file));
        BufferedReader fread = new BufferedReader(new FileReader(file_2));
        String lineTemp = fread.readLine();
        boolean changed = false;
        while(lineTemp != null){
            if(!changed && order.toString().equals(lineTemp)){
                order.changeState(state);
                fwrite.write(order.toString());
                fwrite.newLine();
                changed = true;
            }else{
                fwrite.write(lineTemp);
                fwrite.newLine();
            }
            lineTemp = fread.readLine();
        }
        if(!changed && order.toString().equals(lineTemp)){
            order.changeState(state);
            fwrite.write(order.toString());
            fwrite.newLine();
        }
        else if(lineTemp != null)
            fwrite.write(lineTemp);
        
        fwrite.close();
        fread.close();
        file_2.delete();
        file.renameTo(file_2);
    }

    /**
     * To remove a product from database
     * @param id id of the product that will be removed
     * @throws IOException if there is an error while opening file
     */
    public void removeProduct(String id) throws IOException{
        File file = new File("temp.txt");
        File file_2 = new File(productFile);
        BufferedWriter fwrite = new BufferedWriter(new FileWriter(file));
        BufferedReader fread = new BufferedReader(new FileReader(file_2));
        String lineTemp = fread.readLine();
        while(lineTemp != null){
            if(!splitString(lineTemp, ";").get(DataPart.ID.getValue()).equals(id)){
                fwrite.write(lineTemp);
                fwrite.newLine();
            }
            lineTemp = fread.readLine();
        }
        if(lineTemp != null && !splitString(lineTemp, ";").get(DataPart.ID.getValue()).equals(id))
            fwrite.write(lineTemp);
        
        fwrite.close();
        fread.close();
        file_2.delete();
        file.renameTo(file_2);
    }

    private List<String> splitString(String str, String regex){
        return Arrays.asList(str.split(regex));
    }

    /**
     * To find the orders of a customer by looking customer id
     * @param custID customer id that will be looked
     * @return returns the queue of the order list, returns empty queue if it doesnt find
     * @throws IOException if there is an error while opening file
     */
    public Queue<Order> findCustomerOrders(String custID) throws IOException{
        Queue<Order> orders = new ArrayDeque<>();

        File file = new File(orderFile);
        BufferedReader fread = new BufferedReader(new FileReader(file));
        String line = fread.readLine();
        while(line != null){
            List<String> list = splitString(line, "%%");
            if(list.get(0).equals(custID)){
                orders.offer(new Order(list.get(0), list.get(1), list.get(2), list.get(3)));
            }
            line = fread.readLine();
        }

        fread.close();
        return orders;
    }

    /**
     * To find the orders of a trader by looking trader name
     * @param traderName trader name that will be looked
     * @return returns the queue of the order list, returns empty queue if it doesnt find
     * @throws IOException if there is an error while opening file
     */
    public Queue<Order> findTraderOrders(String traderName) throws IOException{
        Queue<Order> orders = new ArrayDeque<>();

        File file = new File(orderFile);
        BufferedReader fread = new BufferedReader(new FileReader(file));
        String line = fread.readLine();
        while(line != null){
            List<String> list = splitString(line, "%%");
            if(list.get(1).equals(traderName) && list.get(3).equals("WAIT")){
                orders.offer(new Order(list.get(0), list.get(1), list.get(2), list.get(3)));
            }
            line = fread.readLine();
        }

        fread.close();
        return orders;
    }
}
