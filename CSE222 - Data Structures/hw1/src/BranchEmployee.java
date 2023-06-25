import java.io.Serializable;

/**
 * BranchEmployee class which holds information of a branch employee.
 * @author Yunus_Emre_Ozturk
 */
public class BranchEmployee extends Employee implements BranchEmployeeAccess, Serializable{
    private Branch branch;

    /**
     * BranchEmployee constructor.
     * @param name name of the employee.
     * @param surname surname of the employee.
     * @param branch branch of the employee which will be added.
     */
    public BranchEmployee(String name, String surname, Branch branch){
        super(name, surname);
        if(branch == null){
            throw(new IllegalArgumentException());
        }
        
        this.branch = branch;
    }

    /**
     * Creates new customer.
     * @param name name of the customer.
     * @param surname surname of the customer.
     * @param email email of the customer.
     * @param password password of the customer.
     * @return returns Customer object to get information.
     */
    public Customer createNewCustomer(String name, String surname, String email, String password){
        Customer cust = new Customer(name, surname, email, password, branch.getOnlineDep());
        branch.getCustList().addCustomer(cust);
        return cust;
    }

    /**
     * To get Order list of the customer.
     * @param ID id of the customer.
     * @return returns Order object to get information, if ID is not correct it will return null.
     */
    public Order getOrder(String ID){
        Customer cust = branch.getCustList().getCustomerWithID(ID);
        if(cust != null){
            return cust.getOrder();
        }
        else
            return null;
    }

    /**
     * To add Order to the Order list of customer.
     * @param Order Order list of customer.
     * @param Item Item information that will be added to customer's Order list.
     * @return returns true if operation is successful and removes the item from depot, if item is not available it will return false.
     * @throws IllegalArgumentException if order or item is null.
     */
    public boolean addOrder(Order order, Item item) throws IllegalArgumentException{
        if(item == null || order == null){
            throw(new IllegalArgumentException());
        }
        if(removeItem(item) && item.getQuantity() > 0){   //if it is succesfully removed in other say if there is stock
            order.addFinishedOrder(item);
            return true;
        }
        else
            return false;
    }

    /**
     * To remove Order from the Order list of customer.
     * @param Order Order list of customer.
     * @param index index of the Order that will be removed.
     * @return returns true if operation is successful, else false.
     * @throws IllegalArgumentException if order is null.
     */
    public boolean removeOrder(Order order, int index) throws IllegalArgumentException{
        if(order == null){
            throw(new IllegalArgumentException());
        }
        if(order.removeOrderfromLatest(index))
            return true;
        else
            return false;
    }

    /**
     * To get branch information of employee.
     * @return returns Branch of the employee.
     */
    public Branch getBranch(){
        return branch;
    }

    /**
     * Adds item to the branch depot.
     * @param Item item that will be added to the depot.
     * @return returns true if operation is successful, else false.
     */
    public boolean addItem(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        Depot depot = getBranch().getDepot();
        if(depot.addToDepot(item))
            return true;
        else
            return false;
    }
    
    /**
     * Removes item from the branch depot.
     * @param Item item that will be added to the depot.
     * @return returns true if operation is successful, if item or item quantity is not available it will return false.
     */
    public boolean removeItem(Item item){
        Depot depot = getBranch().getDepot();

        if(depot.removeFromDepot(item))
            return true;
        else
            return false;
    }

    /**
     * To get branch depot information.
     * @return returns Depot of the branch.
     */
    public Depot getBranchDepot(){
        return getBranch().getDepot();
    }

    /**
     * Informs manager or admin about the restock by writing the item information to InfoBox of branch.
     * @param item Item information about restock request.
     * @return void.
     */
    public void restockInform(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        getBranch().getInfoBox().addRestockInfo(getBranch(), item);
    }

    /**
     * To get information about branch employee with string, it will have name, surname, ID and branch information, Overrides toString method of Employee class.
     * @return returns employee information with string.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder(super.toString());
        strBuild.append("Branch: ").append(branch.getName()).append(";");

        return strBuild.toString();
    }
}
