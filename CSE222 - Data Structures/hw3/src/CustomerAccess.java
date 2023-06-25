/**
 * Customer interface which will give access of the customer to the classes.
 * @author Yunus_Emre_Ozturk
 */
public interface CustomerAccess {
    /**
     * To get ID of this customer.
     * @return returns ID number in string type.
     */
    public String getCustomerID();

    /**
     * To show product list of the online depot to customer.
     * @return returns String with information about products.
     */
    public String getProductList();

     /**
     * To show product list based of given parameter.
     * @param name name of the product that will be searched.
     * @return returns String with information about products.
     */
    public String searchProduct(String name);

    /**
     * Removes item from the order list.
     * @param ind index of the item which will be removed from Order List.
     * @return returns true if Order successfully removed.
     */
    public boolean removeItemFromOrder(int ind);

    /**
     * Adds item to the order list.
     * @param item Item information which will be added to the order list.
     * @return returns true if Order successfully added to the order list.
     */
    public boolean addToOrder(Item item);

    /**
     * Finishes online order.
     * @return returns true if a branch depot has the items with correct quantity then depot will decrease those items, else it will return false.
     */
    public boolean makeOnlineOrder();

    /**
     * To get Order List of this customer.
     * @return returns Order object.
     */
    public Order getOrder();
}
