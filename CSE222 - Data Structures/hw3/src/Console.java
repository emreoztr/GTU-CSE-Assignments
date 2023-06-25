import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ListIterator;
import java.util.Scanner;

public class Console {
    public static boolean driver(){
        Company comp = new Company("comp");
        System.out.println("Company Class method tests:");
        System.out.print("Change Company name to test1, Company.setName(String): ");
        comp.setName("test1");
        System.out.println(comp.getName());
        System.out.println("--------------------------------------------");
        System.out.println("Add 300 branches, Company.addBranch(String)");
        for(int i = 0; i < 300; ++i)
            comp.addBranch(String.valueOf(i));
        System.out.println("Alive branch count: " + String.valueOf(comp.getBranchCount()));
        System.out.println("Remove 300 branches, Company.removeBranch(String)");
        for(int i = 0; i < 300; ++i)
            comp.removeBranch(String.valueOf(i));
        System.out.println("Alive branch count: " + String.valueOf(comp.getBranchCount()));
        System.out.println("Successful!");
        System.out.println("--------------------------------------------");
        System.out.print("Remove branch from empty linked list: ");
        if(!comp.removeBranch("0"))
            System.out.println("Returned false, so it is successful!");
        else
            System.out.println("Returned true, not successful!");
        System.out.println("--------------------------------------------");
        System.out.println("********************************************");
        System.out.println("Admin Class method Tests:");
        System.out.println("Create Admin without a valid comp:");
        try{
            Admin a = new Admin("_name", "_surname", null);
        }catch (IllegalArgumentException e){
            System.out.println("Test is successful, constructor threw IllegalArgumentException");
        }
        Admin a = new Admin("_name", "_surname", comp);
        System.out.println("--------------------------------------------");
        System.out.println("Add 300 branches, Admin.addBranch(String)");
        for(int i = 0; i < 300; ++i)
            a.addBranch(String.valueOf(i));
        System.out.println("Alive branch count: " + String.valueOf(comp.getBranchCount()));
        System.out.println("Remove 300 branches, Admin.removeBranch(String)");
        for(int i = 0; i < 300; ++i)
            a.removeBranch(String.valueOf(i));
        System.out.println("Alive branch count: " + String.valueOf(comp.getBranchCount()));
        System.out.println("Successful!");
        System.out.println("--------------------------------------------");
        System.out.print("Remove branch from empty linked list: ");
        if(!comp.removeBranch("0"))
            System.out.println("Returned false, so it is successful!");
        else
            System.out.println("Returned true, not successful!");
        System.out.println("--------------------------------------------");
        System.out.println("Add branch employee without a valid branch, Admin.addBranchEmployee(Branch, String, String): ");
        try{a.addBranchEmployee(null, "a", "b");}catch(IllegalArgumentException e){
            System.out.println("Test is successful, method threw IllegalArgumentException");
        }
        System.out.println("--------------------------------------------");
        a.addBranch("test");
        System.out.println("Add branch employee, Admin.addBranchEmployee(Branch, String, String): ");
        System.out.println("Added employee information: " + a.addBranchEmployee(a.getBranch("test"), "_name", "_surname"));
        System.out.println("--------------------------------------------");
        System.out.println("Add 300 branch employees to a branch, Admin.addBranchEmployee(Branch, String, String): ");
        for(int i = 0; i < 300; ++i)
            a.addBranchEmployee(a.getBranch("test"), "_name" + String.valueOf(i), "_surname");
        System.out.println("Employee count: " + comp.getEmployeeCount());
        System.out.println("There is 301 employee because driver created an employee before this test!");
        System.out.println("--------------------------------------------");
        System.out.println("Remove 300 branch employees to a branch, Admin.removeBranchEmployee(Branch, String): ");
        for(int i = 2; i < 302; ++i) {
            String base_id = Integer.toHexString(i);
            String id = base_id;
            for(int j = 0; j < (IDBuilder.ID_LEN - base_id.length() - 1); ++j)
                id = "0" + id;
            id = "E" + id;
            a.removeBranchEmployee(a.getBranch("test"), id);
        }
        System.out.println("Employee count: " + comp.getEmployeeCount());
        System.out.println("--------------------------------------------");
        System.out.println("Add 50 restock info to infobox of the branch (Actually this is a job for branch employee): ");
        for(int i = 0; i < 50; ++i)
            a.getBranch("test").getInfoBox().addRestockInfo(a.getBranch("test"),
                    new Item("_name" + String.valueOf(i), "_model", "_col", 1));
        System.out.println("Look info boxes, Admin.lookInfoBoxes(Branch branch): ");
        System.out.println(a.lookInfoBoxes(a.getBranch("test")));
        System.out.println("New ones are printing first.");
        System.out.println("--------------------------------------------");
        System.out.println("Remove 50 restock info to infobox of the branch Admin.removeInfo(Branch, int): ");
        for(int i = 0; i < 50; ++i)
            a.removeInfo(a.getBranch("test"), 0);
        System.out.println("Look info boxes, Admin.lookInfoBoxes(Branch branch): ");
        System.out.println(a.lookInfoBoxes(a.getBranch("test")));
        System.out.println("New ones are printing first.");
        System.out.println("--------------------------------------------");
        System.out.println("Trying to remove from empty infobox, Admin.removeInfo(Branch, int): ");
        if(!a.removeInfo(a.getBranch("test"), 0))
            System.out.println("Method returned false, so test is successful!");
        System.out.println("--------------------------------------------");
        System.out.println("Trying to remove from a null branch, Admin.removeInfo(Branch, int): ");
        try{a.removeInfo(null, 0);}catch (IllegalArgumentException e){
            System.out.println("Program catch an IllegalArgumentException, test is successful!");
        }
        System.out.println("********************************************");
        System.out.println("BranchEmployee Class method tests:");
        BranchEmployee testEmp = (BranchEmployee) comp.getBranch("test").findEmployee("E00001");
        System.out.println("Trying to add 50 customers to system, BranchEmployee.createNewCustomer(String, String, String, String): ");
        for(int i = 0; i < 50; ++i)
            testEmp.createNewCustomer("_name" + String.valueOf(i), "_surname", "_email" + String.valueOf(i), "_pass");
        System.out.println("New customer count: " + comp.getCustomerCount());
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add 50 customers to system with same mail, BranchEmployee.createNewCustomer(String, String, String, String): ");
        for(int i = 0; i < 50; ++i)
            testEmp.createNewCustomer("_name" + String.valueOf(i), "_surname", "_email-", "_pass");
        System.out.println("New customer count: " + comp.getCustomerCount());
        System.out.println("There are 51 customers in the system now, increased one because while trying to add new 50 customers to the system there is only one unique email");
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add 200 new Item with quantity of 50 to the system, BranchEmployee.addItem(Item): ");
        for(int i = 0; i < 200; ++i)
            testEmp.addItem( new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        System.out.println(testEmp.getBranchDepot());
        System.out.println("You can see the links that connects arraylists in HybridList with last print!");
        System.out.println("--------------------------------------------");
        System.out.println("Trying to remove middle 50 items from depot, BranchEmployee.removeItem(Item)");
        for(int i = 75; i < 125; ++i)
            testEmp.removeItem(new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        System.out.println(testEmp.getBranchDepot());
        System.out.println("Trying to remove all items from depot, BranchEmployee.removeItem(Item)");
        for(int i = 0; i < 75; ++i)
            testEmp.removeItem(new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        for(int i = 125; i < 200; ++i)
            testEmp.removeItem(new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        System.out.println("New depot information (there should be no information): " + testEmp.getBranchDepot());
        System.out.println("--------------------------------------------");
        System.out.println("Trying to remove from empty depot, BranchEmployee.removeItem(Item)");
        if(!testEmp.removeItem(new Item("_name" + String.valueOf(0), "_model", "_col", 50)))
            System.out.println("Method returned false, so test is successful!");
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add one item with 100 quantity and then remove 25 quantity from it, BranchEmployee.addItem(Item), BranchEmployee.removeItem(Item)");
        testEmp.addItem( new Item("_name", "_model", "_col", 100));
        System.out.println(testEmp.getBranchDepot());
        System.out.println("Now system will remove 25 of it: ");
        testEmp.removeItem(new Item("_name", "_model", "_col", 25));
        System.out.println(testEmp.getBranchDepot());
        testEmp.removeItem(new Item("_name", "_model", "_col", 75));
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add 50 restock information: BranchEmployee.restockInform(Item)");
        for(int i = 0; i < 50; ++i)
            testEmp.restockInform(new Item("_name" + String.valueOf(i), "_model", "_col", 1));
        System.out.println(testEmp.getBranch().getInfoBox());
        for(int i = 0; i < 50; ++i)
            a.removeInfo(a.getBranch("test"), 0);
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add 50 available item from depot to customer order list: BranchEmployee.addOrder(Order, Item)");
        for(int i = 0; i < 20; ++i)
            testEmp.addItem( new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        for(int i = 0; i < 20; ++i)
            testEmp.addOrder(testEmp.getOrder("C00002"), new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        System.out.println(testEmp.getOrder("C00002"));
        System.out.println("After completing order, depot situation (Should be empty): " + testEmp.getBranchDepot());
        System.out.println("********************************************");
        System.out.println("Customer Class method tests:");
        Customer custTest = comp.getCustomerList().getCustomerWithID("C00003");
        System.out.println("Get list of products, Customer.getProductList()");
        for(int i = 0; i < 50; ++i)
            testEmp.addItem( new Item("_name" + String.valueOf(i), "_model", "_col", 50));
        System.out.println(custTest.getProductList());
        System.out.println("--------------------------------------------");
        System.out.println("Search in product list, searchProduct(String)");
        System.out.println(custTest.searchProduct("_name5"));
        System.out.println("--------------------------------------------");
        System.out.println("Add order from product list, Customer.addToOrder(Item)");
        System.out.println("Last situation in list of products:");
        System.out.println(custTest.getProductList());
        System.out.println("After getting the product _name16 and _name33 with quantity of 50, Customer.getOrder():");
        custTest.addToOrder(new Item("_name16", "_model", "_col", 50));
        custTest.addToOrder(new Item("_name33", "_model", "_col", 50));
        System.out.println(custTest.getOrder());
        System.out.println("After finishing order last situation in online depot (16 and 33 should be removed), Customer.makeOnlineOrder():");
        custTest.makeOnlineOrder();
        System.out.println(custTest.getProductList());
        System.out.println("After finishing order last situation in order list (16 and 33 should be moved to shipped order), Customer.makeOnlineOrder():");
        System.out.println(custTest.getOrder());
        System.out.println("--------------------------------------------");
        testEmp.addItem( new Item("_name16", "_model", "_col", 50));
        testEmp.addItem( new Item("_name33", "_model", "_col", 50));
        System.out.println("Add all the items in the branch depot to order: ");

        for(int i = 0; i < 50; ++i){
            custTest.addToOrder(new Item("_name"+String.valueOf(i), "_model", "_col", 50));
        }
        System.out.println("After adding items to order, last situation in orderlist: ");
        System.out.println(custTest.getOrder());
        System.out.println("Depot before completing to order");
        System.out.println(custTest.getProductList());
        custTest.makeOnlineOrder();
        System.out.println("Depot after completing to order");
        System.out.println(custTest.getProductList());
        System.out.println("Order list after completing order: ");
        System.out.println(custTest.getOrder());
        System.out.println("--------------------------------------------");
        System.out.println("Trying to add an item that is not available to order, and complete order: ");
        custTest = comp.getCustomerList().getCustomerWithID("C00004");
        custTest.addToOrder(new Item("_name", "_model", "_col", 50));
        if(!custTest.makeOnlineOrder())
            System.out.println("Method returned false, so test is successful!");
        System.out.println("Last situation in order list of the user: ");
        System.out.println(custTest.getOrder());
        System.out.println("Item that is not available waits in the last order (which means not completed order), to remove it from there:");
        System.out.println("Customer.removeItemFromOrder(int)");
        custTest.removeItemFromOrder(0);
        System.out.println("After removing item from order, last situation in order list:");
        System.out.println(custTest.getOrder());

        return true;
    }

    public static void main(String[] args){
        driver();

        Company comp = new Company("TEST");
        try {
            FileInputStream fileIn = new FileInputStream("company.ser");
            ObjectInputStream inp = new ObjectInputStream(fileIn);
            comp = (Company) inp.readObject();
            inp.close();
            fileIn.close();
            } catch (FileNotFoundException e){
                comp = new Company("Office Furniture");
            } catch (IOException i) {
                comp = new Company("Office Furniture");
            } catch (ClassNotFoundException c) {
                comp = new Company("Office Furniture");
            }
        IDBuilder.setCustomerCount(comp.getCustomerCount());
        IDBuilder.setEmployeeCount(comp.getEmployeeCount());
        int choice;
        Scanner in = new Scanner(System.in);
        do{
        System.out.println("1) Admin login\n2) Employee login\n3) Customer login\n0) Exit");
        
        choice = in.nextInt();
        in.nextLine();
        if(choice==1){
            adminMenu(comp, in);
        }else if(choice == 2){
            employeeMenu(comp, in);
        }
        else if(choice == 3){
            customerMenu(comp, in);
        }

        }while(choice != 0);
        try{
        FileOutputStream fout = new FileOutputStream("company.ser");
        ObjectOutputStream out = new ObjectOutputStream(fout);
        out.writeObject(comp);
        out.close();
        fout.close();
        } catch (IOException i) {
            i.printStackTrace();
     }
    }

    public static void adminMenu(Company comp, Scanner in){
        Admin admin = new Admin("name", "surname", comp);
        int choice;
        
        
        do{
            System.out.println("1) Add branch\n2) Remove Branch\n" + 
            "3) Add Branch Employee to a Branch\n4) Remove Branch Employee from a Branch\n" +
            "5) Show Employee and Depot Information of Branch\n6) Show All Depot Information\n" +
            "7) Read branch messages\n8) Delete branch message\n" +
            "0) Exit");
            System.out.println("Choice: ");
            choice = in.nextInt();
            in.nextLine();
            
            if(choice == 1){
                String input;
                System.out.print("Please write unique name of Branch: ");
                input = in.nextLine();
                if(admin.addBranch(input))
                    System.out.println(admin.getBranch(input));
                else{
                    System.out.println("Please write a unique name for Branch!");
                }
            }else if(choice == 2){
                String input;
                System.out.print("Please write unique name of Branch: ");
                input = in.nextLine();
                if(admin.removeBranch(input)){
                    System.out.println("Branch removed successfully.");
                }
                else{
                    System.out.println("Please write a unique name for Branch!");
                }

            }else if(choice == 3){
                System.out.print("Please write unique name of Branch: ");
                String branchName = in.nextLine();
                System.out.print("Please write the name of the employee: ");
                String name = in.nextLine();
                System.out.print("Please write the surname of the employee: ");
                String surname = in.nextLine();
                try{
                    Employee emp = admin.addBranchEmployee(admin.getBranch(branchName), name, surname);
                    System.out.print("Employee Information: ");
                    System.out.println(emp);
                }catch(IllegalArgumentException e){
                    System.out.println("Branch doesn't exist!");
                }
            }else if(choice == 4){
                String branchName, ID;
                System.out.print("Please write unique name of Branch: ");
                branchName = in.nextLine();
                System.out.print("Please wrrite the ID of employee: ");
                ID = in.nextLine();
                try{
                    if(admin.removeBranchEmployee(admin.getBranch(branchName), ID)){
                        System.out.println("Employee successfully removed from the branch.");
                    }
                    else{
                        System.out.println("Please provide correct name of the branch and ID of the employee!");
                    }
                }catch(IllegalArgumentException e){
                    System.out.println("Branch doesn't exist!");
                }

            }else if(choice == 5){
                System.out.print("Please write unique name of Branch: ");
                String input = in.nextLine();
                Branch branch = admin.getBranch(input);
                if(branch != null){
                    System.out.println(branch);
                }else{
                    System.out.println("Branch doesn't exist!");
                }
            }else if(choice == 6){
                System.out.println(admin.getOnlineDepot());
            }else if(choice == 7){
                System.out.print("Please write unique name of Branch: ");
                String branchName = in.nextLine();
                try{
                    System.out.println(admin.getBranch(branchName).getInfoBox());
                }catch(IllegalArgumentException e){
                    System.out.println("Branch doesn't exist!");
                }
            }else if(choice == 8){
                System.out.print("Please write unique name of Branch: ");
                String branchName = in.nextLine();
                try{
                    System.out.println(admin.getBranch(branchName).getInfoBox());
                    System.out.print("Please write the index of the message you want to delete: ");
                    int ind = in.nextInt();
                    in.nextLine();
                    if(admin.removeInfo(admin.getBranch(branchName), ind - 1))
                        System.out.println("Message deleted successfully");
                    else
                        System.out.println("Wrong index!");
                }catch(IllegalArgumentException e){
                    System.out.println("Branch doesn't exist!");
                }
                
            }
        }while(choice != 0);
        
    }

    public static void employeeMenu(Company comp, Scanner in){
        String branchName, ID;
        Employee emp;
        boolean found = false;
        do{   
        System.out.print("Please write the name of your Branch: ");
        branchName = in.nextLine();
        System.out.print("Please write your ID: ");
        ID = in.nextLine();
        Branch branch = comp.getBranch(branchName);
        if(branch == null){
            System.out.println("Branch name is not correct!");
            return;
        }
        emp = branch.findEmployee(ID);
        if(emp != null)
            found = true;
        else
            System.out.println("System couldn't find employee! Please try again!");
        }while(!found);

        if(emp instanceof BranchEmployee){ //i've done this because in the future there can be more types of employee
            BranchEmployee bemp = (BranchEmployee) emp; //polymorphism
            Customer cust = null;
            int choice = 0;
            do{
                System.out.println("1) Create New Customer Subscription\n2) Check Customer Order\n" + 
                "3) Check Depot\n4) Sell Item\n" +
                "5) Remove Customer Order\n" +
                "6) Add Item to Depot\n" +
                "7) Remove Item from Depot\n" +
                "8) Inform Manager\n" +
                "0) Exit");
                System.out.print("Choice: ");
                choice = in.nextInt();
                in.nextLine();
                if(choice == 1){
                    String name, surname, mail, password;
                    
                    System.out.print("Write customer name: ");
                    name = in.nextLine();
                    System.out.print("Write customer surname: ");
                    surname = in.nextLine();
                    System.out.print("Write customer mail: ");
                    mail = in.nextLine();
                    System.out.print("Write customer password: ");
                    password = in.nextLine();
                    cust = bemp.createNewCustomer(name, surname, mail, password);

                    if(cust != null){
                        System.out.println("Subscription Created, customer ID: " + cust.getCustomerID());
                    }
                    else
                        System.out.println("Subscription cannot be created!");

                }else if(choice == 2){
                    System.out.print("Please write the ID of customer: ");
                    String custID = in.nextLine();
                    Order order = bemp.getOrder(custID);
                    if(order != null)
                        System.out.println(bemp.getOrder(custID));
                    else
                        System.out.println("Customer ID is not correct!");

                }else if(choice == 3){
                    System.out.println(bemp.getBranchDepot());

                }else if(choice == 4){
                    System.out.print("Please write the ID of the customer: ");
                    String custID = in.nextLine();
                    System.out.print("Please write the name of the Item: ");
                    String itemName = in.nextLine();
                    System.out.print("Please write the model of the Item: ");
                    String itemMod = in.nextLine();
                    System.out.print("Please write the color of the Item: ");
                    String itemCol = in.nextLine();
                    System.out.print("Please write the quantity of the Item: ");
                    int itemQuant = in.nextInt();
                    in.nextLine();
                    try{
                        if(bemp.addOrder(bemp.getOrder(custID), new Item(itemName, itemMod, itemCol, itemQuant)))
                            System.out.println("Order completed and decreased from stock.");
                        else
                            System.out.println("Couldn't add the item, check if there is stock!");
                    }catch(IllegalArgumentException e){
                        System.out.println("Customer ID is not correct!");
                    }

                }else if(choice == 5){
                    System.out.print("Please write the ID of the customer: ");
                    String custID = in.nextLine();
                    Order order = bemp.getOrder(custID);
                    if(order != null){
                        System.out.println(order);
                        System.out.print("Please write the index of the item: ");
                        int ind = in.nextInt();
                        in.nextLine();
                        if(bemp.removeOrder(order, ind - 1))
                            System.out.println("Item removed from order successfully.");
                        else
                            System.out.println("Index of the item is wrong!");
                    }else{
                        System.out.println("Customer ID is not correct!");
                    }
                }else if(choice == 6){
                    System.out.print("Please write the name of the Item: ");
                    String itemName = in.nextLine();
                    System.out.print("Please write the model of the Item: ");
                    String itemMod = in.nextLine();
                    System.out.print("Please write the color of the Item: ");
                    String itemCol = in.nextLine();
                    System.out.print("Please write the quantity of the Item: ");
                    int itemQuant = in.nextInt();
                    in.nextLine();
                    if(bemp.addItem(new Item(itemName, itemMod, itemCol, itemQuant)))
                        System.out.println("Item added to depot.");
                    else
                        System.out.println("Item quantity cannot be less than zero!");

                }else if(choice == 7){
                    System.out.print("Please write the name of the Item: ");
                    String itemName = in.nextLine();
                    System.out.print("Please write the model of the Item: ");
                    String itemMod = in.nextLine();
                    System.out.print("Please write the color of the Item: ");
                    String itemCol = in.nextLine();
                    System.out.print("Please write the quantity of the Item: ");
                    int itemQuant = in.nextInt();
                    in.nextLine();
                    if(bemp.removeItem(new Item(itemName, itemMod, itemCol, itemQuant)))
                        System.out.println("Item removed from the depot.");
                    else
                        System.out.println("There is no such item in depot or enough quantity!");
                }else if(choice == 8){
                    System.out.print("Please write the name of the Item: ");
                    String itemName = in.nextLine();
                    System.out.print("Please write the model of the Item: ");
                    String itemMod = in.nextLine();
                    System.out.print("Please write the color of the Item: ");
                    String itemCol = in.nextLine();
                    System.out.print("Please write the needed restock quantity of the Item: ");
                    int itemQuant = in.nextInt();
                    in.nextLine();
                    bemp.restockInform(new Item(itemName, itemMod, itemCol, itemQuant));
                    System.out.println("Manager informed!");
                }
            }while(choice != 0);
        }
        else{
            System.out.println("Cannot access to this employee!");
        }
    }

    public static void customerMenu(Company comp, Scanner in){
        int choice;
        Customer cust = null;
        boolean loggedin = false;
        do{
            System.out.println("1) Login\n2) Create New Account\n0) Exit");

            choice = in.nextInt();
            in.nextLine();
            if(choice == 1){
                System.out.print("Please write your email: ");
                String mail = in.nextLine();
                System.out.print("Please write your password: ");
                String pass = in.nextLine();
                cust = comp.getCustomerList().getCustomerWithMail(mail);
                if(cust != null){
                    if(cust.getPassword().equals(pass))
                        loggedin = true;
                    else{
                        System.out.println("Wrong password or mail!");
                        cust = null;
                    }
                }
                else
                    System.out.println("Wrong password or mail!");
            }else if(choice == 2){
                String name, surname, mail, password;
                    
                    System.out.print("Write your name: ");
                    name = in.nextLine();
                    System.out.print("Write your surname: ");
                    surname = in.nextLine();
                    System.out.print("Write your mail: ");
                    mail = in.nextLine();
                    System.out.print("Write your password: ");
                    password = in.nextLine();
                    cust = comp.createCustomerAccount(name, surname, mail, password);

                    if(cust != null){
                        System.out.println("Subscription Created, customer ID: " + cust.getCustomerID());
                        loggedin = true;
                    }
                    else
                        System.out.println("System couldn't create subscription, check your email it should be unique!");
            }
        }while(choice != 0 && loggedin == false);

        if(loggedin){
            do{
            System.out.println("1) See list of the products\n2) Search product\n" + 
            "3) Check Order List\n4) Add item to your order\n" +
            "5) Remove item from your order list\n6) Make the order\n" +
            "7) Check your ID\n" +
            "0) Exit");
            System.out.println("Choice: ");
            choice = in.nextInt();
            in.nextLine();
            if(choice == 1){
                System.out.println(cust.getProductList());
            }else if(choice == 2){
                System.out.println("Please write the name of the item: ");
                String name = in.nextLine();
                System.out.println(cust.searchProduct(name));
            }else if(choice == 3){
                System.out.println(cust.getOrder());
            }else if(choice == 4){
                System.out.print("Please write the name of the Item: ");
                String itemName = in.nextLine();
                System.out.print("Please write the model of the Item: ");
                String itemMod = in.nextLine();
                System.out.print("Please write the color of the Item: ");
                String itemCol = in.nextLine();
                System.out.print("How many you want: ");
                int itemQuant = in.nextInt();
                in.nextLine();
                if(cust.addToOrder(new Item(itemName, itemMod, itemCol, itemQuant)))
                    System.out.println("Item added to your order list!");
                else{
                    System.out.println("System couldn't add the item to your order list!");
                }
            }else if(choice == 5){
                System.out.println(cust.getOrder());
                System.out.print("Please choose the item number you want to remove:");
                int ind = in.nextInt();
                in.nextLine();
                if(cust.removeItemFromOrder(ind-1))
                    System.out.println("Item removed successfully.");
                else{
                    System.out.println("System couldn't remove item, please try again!");
                }


            }else if(choice == 6){
                System.out.print("Please write your address: ");
                String address = in.nextLine();
                System.out.print("Please write your phone: ");
                String phone = in.nextLine();
                cust.setAddress(address);
                cust.setPhone(phone);
                if(cust.makeOnlineOrder())
                    System.out.println("Order completed successfully!");
                else
                    System.out.println("Couldn't complete the order!");
            }else if(choice == 7){
                System.out.println(cust.getCustomerID());
            }
            }while(choice != 0);
        }
    }
}