import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        AppSystem ap = new AppSystem("e-commerce-samples.txt", "count_file.txt", "user_file.txt", "order_file.txt");
        driver(ap);

        loginScreen(ap);
        
    }

    private static void driver(AppSystem ap){
        try {
        System.out.println("Login / Register Tests:");
        String custId = ap.registerCustomer("testcust", "123456");
        String traderId;
        
        traderId = ap.registerTrader("testtrad", "123456");
        
        System.out.println("Customer ID after register: " + custId);
        System.out.println("Trader ID after register: " + traderId);
        System.out.println("Trying to create customer and trader with same name: ");
        String custId2 = ap.registerCustomer("testcust", "123456");
        String traderId2 = ap.registerTrader("testtrad", "123456");
        System.out.println("Customer ID after register: " + custId2);
        System.out.println("Trader ID after register: " + traderId2);
        System.out.println("System didnt give them id because there is already a member with same name.");
        try{
            ap.registerCustomer(null, null);
            ap.registerTrader(null, null);
        }catch(NullPointerException e){
            System.out.println("Null parameter test successful!");
        }

        System.out.println("Trying to login with invalid password:");
        Customer c = (Customer) ap.login("C0000002", "123457");
        Trader t = (Trader) ap.login("T0000c71", "123457");
        System.out.println("Should be null: ");
        System.out.println(c);
        System.out.println(t);

        
        c = (Customer) ap.login("C0000003", "123456");
        t = (Trader) ap.login("T0000c72", "123456");
        System.out.println("Prints trader and customer information: ");
        System.out.println(c);
        System.out.println(t);

        System.out.println("\nCustomer methods: ");
        c.searchProduct("hat");
        System.out.println(c.displayResult());
        System.out.println("Filtered Category:");
        c.filterCategory(new Category("Tools"));
        System.out.println(c.displayResult());
        System.out.println("Name descending and ascending: ");
        c.sortResults(DataEnum.NAME, DataEnum.DECREASING);
        System.out.println(c.displayResult() + "\n");
        c.sortResults(DataEnum.NAME, DataEnum.INCREASING);
        System.out.println(c.displayResult() + "\n");
        System.out.println("Price descending and ascending: ");
        c.sortResults(DataEnum.PRICE, DataEnum.DECREASING);
        System.out.println(c.displayResult() + "\n");
        c.sortResults(DataEnum.PRICE, DataEnum.INCREASING);
        System.out.println(c.displayResult() + "\n");
        System.out.println("Discount percentage descending and ascending: ");
        c.sortResults(DataEnum.DISCOUNT_PERCEN, DataEnum.DECREASING);
        System.out.println(c.displayResult() + "\n");
        c.sortResults(DataEnum.DISCOUNT_PERCEN, DataEnum.INCREASING);
        System.out.println(c.displayResult() + "\n");

        System.out.println("\nTrader add / remove products");
        LinkedList<Category> a = new LinkedList<>();
        a.add(new Category("a"));
        a.add(new Category("b"));
        a.add(new Category("c"));
        t.addProduct(new Product(IDBuilder.generateProductID(), "DriverTest", a.getLast(), a, 100.0, 20.0, "test", t.getName()));

        System.out.println("\nSearching the product with customer:");
        c.searchProduct("DriverTest");
        System.out.println(c.displayResult());
        System.out.println("\nOrdering the product with customer:");
        c.makeOrder(1);
        c.finishOrder();
        System.out.println("\nChecking order with trader: ");
        System.out.println(t.checkLastOrder());
        System.out.println("\nAccepting order: :");
        t.checkOrder(true);
        System.out.println("Removing product: ");
        String id = IDBuilder.generateProductID();
        t.addProduct(new Product(id, "DriverTest", a.getLast(), a, 100.0, 20.0, "test", t.getName()));
        t.removeProduct(id);
        System.out.println(t.getProducts().display());


        } catch (IDLimitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static void loginScreen(AppSystem ap){
        int choice = 0;
        Scanner input = new Scanner(System.in);
        
        do{
            System.out.println("*******************WELCOME*****************");
            System.out.println("1) Login");
            System.out.println("2) Customer Register");
            System.out.println("3) Trader Register");
            System.out.println("0) Exit");

            choice = input.nextInt();
            input.nextLine();

            if(choice == 1){
                String id;
                String pass;

                System.out.print("ID: ");
                id = input.nextLine();
                System.out.print("Password: ");
                pass = input.nextLine();

                User user = ap.login(id, pass);
                if(user == null){
                    System.out.println("Wrong id or password!");
                }
                else if(id.charAt(0) == 'C'){
                    Customer cust = (Customer) user;
                    customerScreen(cust, input);

                }else if(id.charAt(0) == 'T'){
                    Trader trad = (Trader) user;
                    traderScreen(trad, input);

                }
            }else if(choice == 2){
                String name, pass;
                System.out.print("Nickname: ");
                name = input.nextLine();
                System.out.print("Password: ");
                pass = input.nextLine();

                String res;
                try {
                    res = ap.registerCustomer(name, pass);
                    if(res == null) System.out.println("Name already using!");

                    System.out.println("Account created, your ID: " + res);
            
                } catch (IDLimitException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }else if(choice == 3){
                String name, pass;
                System.out.print("Nickname: ");
                name = input.nextLine();
                System.out.print("Password: ");
                pass = input.nextLine();

                String res;
                try {
                    res = ap.registerTrader(name, pass);
                    if(res == null) System.out.println("Name already using!");

                    System.out.println("Account created, your ID: " + res);
            
                } catch (IDLimitException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
        }while(choice != 0);
    }
    
    private static void customerScreen(Customer c, Scanner input){
        int choice = 0;
        ProductResult result;
        do{
            System.out.println("1) Search Product");
            System.out.println("2) Sort by name (ascending)");
            System.out.println("3) Sort by name (descending)");
            System.out.println("4) Sort by price (ascending)");
            System.out.println("5) Sort by price (descending)");
            System.out.println("6) Sort by discount (ascending)");
            System.out.println("7) Sort by discount (descending)");
            System.out.println("8) Order");
            System.out.println("9) Filter by Category");
            System.out.println("0) Back");

            choice = input.nextInt();
            input.nextLine();

            if(choice == 1){
                String key;
                System.out.println("Write keyword: ");
                key = input.nextLine();
                result =  c.searchProduct(key);
                System.out.println(c.displayResult());
            }else if(choice == 2){
                c.sortResults(DataEnum.NAME, DataEnum.INCREASING);
                System.out.println(c.displayResult());
            }else if(choice == 3){
                c.sortResults(DataEnum.NAME, DataEnum.DECREASING);
                System.out.println(c.displayResult());
            }else if(choice == 4){
                c.sortResults(DataEnum.PRICE, DataEnum.INCREASING);
                System.out.println(c.displayResult());
            }else if(choice == 5){
                c.sortResults(DataEnum.PRICE, DataEnum.DECREASING);
                System.out.println(c.displayResult());
            }else if(choice == 6){
                c.sortResults(DataEnum.DISCOUNT_PERCEN, DataEnum.INCREASING);
                System.out.println(c.displayResult());
            }else if(choice == 7){
                c.sortResults(DataEnum.DISCOUNT_PERCEN, DataEnum.DECREASING);
                System.out.println(c.displayResult());
            }else if(choice == 8){
                do{
                    System.out.println(c.displayResult());
                    System.out.println("Please write index of your order, to cancel write -1, to finish and and make your order write 0");
                    choice = input.nextInt();
                    input.nextLine();
                    if(choice > 0)
                        c.makeOrder(choice - 1);
                }while(choice != -1 && choice != 0);

                if(choice == -1) c.clearOrder();
                else if(choice == 0)
                    try {
                        c.finishOrder();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

            }else if(choice == 9){
                System.out.println("Please write the category name: ");
                String categ = input.nextLine();

                c.filterCategory(new Category(categ));
                System.out.println(c.displayResult());
            }


        }while(choice != 0);
        
    
    }

    private static void traderScreen(Trader t, Scanner input){
        int choice = 0;
        Result result = t.getProducts();
        do{
            System.out.println("1) Display Products");
            System.out.println("2) Add Product");
            System.out.println("3) Remove Product");
            System.out.println("4) Order Confirmation");
            System.out.println("0) Back");

            choice = input.nextInt();
            input.nextLine();

            if(choice == 1){
                result =  t.getProducts();
                System.out.println(result.display());
            }else if(choice == 2){
                System.out.print("Please write item name: ");
                String name = input.nextLine();
                System.out.print("Please write item description: ");
                String desc = input.nextLine();
                System.out.print("Please write price and discounted price: ");
                double price = input.nextDouble();
                double disc = input.nextDouble();
                LinkedList<Category> categTree = new LinkedList<>();
                System.out.println("Please write category tree from, if you want to stop write 0");
                Category categ = new Category(input.nextLine());
                do{
                    categTree.add(categ);
                    categ = new Category(input.nextLine());
                }while(!categ.getName().equals("0"));

                if(price < disc) System.out.println("Price cannot be smaller the discounted price!");
                else if(categTree.isEmpty()) System.out.println("Category tree cannot be empty");
                else{
                    Product prod;
                    try {
                        prod = new Product(IDBuilder.generateProductID(), name, categTree.getLast(), categTree, price, disc, desc, t.getName());
                        t.addProduct(prod);
                    } catch (IDLimitException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                   
                }
            }else if(choice == 3){
                System.out.print("Please write id of the product: ");
                String id = input.nextLine();
                try {
                    t.removeProduct(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if(choice == 4){
                String line = t.checkLastOrder();
                boolean confirm = false;
                int conf;
                while(line != null){
                    System.out.println(line);
                    System.out.print("Write 1 to confirm and 0 to cancel: ");
                    conf = input.nextInt();
                    input.nextLine();
                    try {
                    if(conf == 1)
                        t.checkOrder(true);
                    else if(conf == 0)
                        t.checkOrder(false);    
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    line = t.checkLastOrder();
                }
            }


        }while(choice != 0);
    }
}
