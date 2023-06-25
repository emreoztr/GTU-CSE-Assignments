import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class Console {
    public static void main(String[] args){
        Company comp;
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
            return;
            } catch (ClassNotFoundException c) {
                comp = new Company("Office Furniture");
            return;
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
