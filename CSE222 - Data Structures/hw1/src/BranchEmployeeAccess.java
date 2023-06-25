/**
 * BranchEmployee interface which gives Branch Employee Access to classes.
 * @author Yunus_Emre_Ozturk
 */
public interface BranchEmployeeAccess {
    /**
     * Creates new customer.
     * @param name name of the customer.
     * @param surname surname of the customer.
     * @param email email of the customer.
     * @param password password of the customer.
     * @return returns Customer object to get information.
     */
    public Customer createNewCustomer(String name, String surname, String email, String password);

    /**
     * To get Order list of the customer.
     * @param ID id of the customer.
     * @return returns Order object to get information, if ID is not correct it will return null.
     */
    public Order getOrder(String ID);

    /**
     * To add Order to the Order list of customer.
     * @param Order Order list of customer.
     * @param Item Item information that will be added to customer's Order list.
     * @return returns true if operation is successful and removes the item from depot, if item is not available it will return false.
     * @throws IllegalArgumentException if order or item is null.
     */
    public boolean addOrder(Order order, Item item) throws IllegalArgumentException;

    /**
     * To remove Order from the Order list of customer.
     * @param Order Order list of customer.
     * @param index index of the Order that will be removed.
     * @return returns true if operation is successful, else false.
     */
    public boolean removeOrder(Order order, int index);

    /**
     * Adds item to the branch depot.
     * @param Item item that will be added to the depot.
     * @return returns true if operation is successful, else false.
     */
    public boolean addItem(Item item);

    /**
     * Removes item from the branch depot.
     * @param Item item that will be added to the depot.
     * @return returns true if operation is successful, if item or item quantity is not available it will return false.
     */
    public boolean removeItem(Item item);

    /**
     * To get branch depot information.
     * @return returns Depot of the branch.
     */
    public Depot getBranchDepot();

    /**
     * Informs manager or admin about the restock by writing the item information to InfoBox of branch.
     * @param item Item information about restock request.
     * @return void.
     */
    public void restockInform(Item item);
}
