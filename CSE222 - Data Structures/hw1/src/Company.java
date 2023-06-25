import java.io.Serializable;

/**
 * Company class, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public class Company implements Serializable{
    private ArrayContainer<Branch> branches;
    private CustomerList customers;
    private OnlineDepot onlineDepot;
    private String name;
    private int custCount = 0, empCount = 0;

    /**
     * Company constructor.
     * @param name name of the Company.
     */
    public Company(String name){
        setName(name);
        branches = new ArrayContainer<>();
        customers = new CustomerList();
        onlineDepot = new OnlineDepot();
    }

    /**
     * To set name of the company.
     * @param name new name of company.
     * @return void
     */
    public void setName(String name){
        if(name == null){
            throw(new IllegalArgumentException());
        }

        this.name = name;
    }

    /**
     * To get Customer List of company.
     * @return return Customer List.
     */
    public CustomerList getCustomerList(){
        return customers;
    }

    /**
     * Creates new customer.
     * @param name name of the customer.
     * @param surname surname of the customer.
     * @param email email of the customer.
     * @param password password of the customer.
     * @return returns Customer object.
     */
    public Customer createCustomerAccount(String name, String surname, String email, String password){
        Customer returnVal = new Customer(name, surname, email, password, onlineDepot);
        if(!customers.addCustomer(returnVal)){
            return null;
        }
        return returnVal;
    }

    /**
     * To get name of the company.
     * @return returns company name in String type.
     */
    public String getName(){
        return name;
    }

    /**
     * Adds new branch to the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and adding is successfull.
     */
    public boolean addBranch(String name){
        Branch branch = new Branch(name, customers, onlineDepot);
        if(!branches.contains(branch)){
            branches.add(branch);
            onlineDepot.addDepot(branch.getDepot());
            return true;
        }
        else
            return false;
    }

    /**
     * Get branch object based on it's name.
     * @param name unique name of the branch.
     * @return returns branch object if successfull, if it is not it will return null.
     */
    public Branch getBranch(String name){
        for(int i = 0; i < branches.getSize(); ++i){
            if(branches.get(i).getName().equals(name))
                return branches.get(i);
        }
        return null; 
    }

    /**
     * Removes new branch from the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and removing is successfull.
     */
    public boolean removeBranch(String name){
        for(int i = 0; i < branches.getSize(); ++i){
            if(branches.get(i).getName().equals(name)){
                onlineDepot.removeDepot(branches.get(i).getDepot());
                branches.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * To get employee count of company.
     * @return returns employee count in integer type.
     */
    public int getEmployeeCount(){
        return empCount;
    }
    /**
     * To get customer count of company.
     * @return returns customer count in integer type.
     */
    public int getCustomerCount(){
        custCount = customers.getCustomerCount();
        return custCount;
    }

    /**
     * To increase employee count by one.
     * @return void.
     */
    public void increaseEmployeeCount(){
        empCount++;
    }

    /**
     * To get Online Depot of company.
     * @return returns OnlineDepot object of the company.
     */
    public OnlineDepot getOnlineDepot(){
        return onlineDepot;
    }
}
