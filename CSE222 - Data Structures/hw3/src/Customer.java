import java.io.Serializable;

/**
 * Customer class, implements Serializable to take a copy and extends from User.
 * @author Yunus_Emre_Ozturk
 */
public class Customer extends User implements CustomerAccess, Serializable{
    private String phone=null, address=null, email, password;
    private Order customerOrder;
    private String customer_id;
    private OnlineDepot onlineDep;

    /**
     * Customer constructor.
     * @param name name of the customer.
     * @param surname surname of the customer.
     * @param email email of the customer.
     * @param password password of the customer.
     * @param onlineDep online depot which will give service to customer.
     */
    Customer(String name, String surname, String email, String password, OnlineDepot onlineDep){
        super(name, surname);
        this.email = email;
        this.password = password;
        this.onlineDep = onlineDep;
        customerOrder = new Order(this);
        try {
            customer_id = IDBuilder.generateCustomerID();
        } catch (IDLimitException e) {
            System.out.println("ID limit is exceeded, please contact with programmer!");
            e.printStackTrace();
        }
    }

    /**
     * To set address of customer.
     * @param address Address of the customer.
     * @return void
     */
    public void setAddress(String address){
        if(address == null){
            throw(new IllegalArgumentException());
        }
        this.address = address;
    }

    /**
     * To set password of customer.
     * @param pass Password of the customer.
     * @return void
     */
    public void setPassword(String pass){
        password = pass;
    }

    /**
     * To set phone of customer.
     * @param phone Phone of the customer.
     * @return void
     * @throws IllegalArgumentException if phone is not correct length
     */
    public void setPhone(String phone) throws IllegalArgumentException{
        if(phone != null){
            String correctPhone = phone;
            correctPhone = correctPhone.replaceAll("\\s+","");
            if(correctPhone.length() >= 10){
                this.phone = correctPhone;
            }
            else{
                throw(new IllegalArgumentException());
            }
        }
        else{
            throw(new IllegalArgumentException());
        }
    }

    /**
     * To get Address of this customer.
     * @return returns address in string type.
     */
    public String getAddress(){
        return address;
    }

    /**
     * To get Phone of this customer.
     * @return returns phone in string type.
     */
    public String getPhone(){
        return phone;
    }

    /**
     * To get ID of this customer.
     * @return returns ID number in string type.
     */
    public String getCustomerID(){
        return customer_id;
    }

    /**
     * To get Mail of this customer.
     * @return returns Mail in string type.
     */
    public String getEmail(){
        return email;
    }

    /**
     * To get Order List of this customer.
     * @return returns Order object.
     */
    public Order getOrder(){
        return customerOrder;
    }

    /**
     * To show product list of the online depot to customer.
     * @return returns String with information about products.
     */
    public String getProductList(){
        return onlineDep.toString();
    }

    /**
     * To show product list based of given parameter.
     * @param name name of the product that will be searched.
     * @return returns String with information about products.
     */
    public String searchProduct(String name){
        return onlineDep.searchProduct(name);
    }

    /**
     * Removes item from the order list.
     * @param ind index of the item which will be removed from Order List.
     * @return returns true if Order successfully removed.
     */
    public boolean removeItemFromOrder(int ind){
        return getOrder().removeOrderfromLatest(ind);
    }

    /**
     * Adds item to the order list.
     * @param item Item information which will be added to the order list.
     * @return returns true if Order successfully added to the order list.
     */
    public boolean addToOrder(Item item){
        return(getOrder().addOrder(item));
    }

    /**
     * Finishes online order.
     * @return returns true if a branch depot has the items with correct quantity then depot will decrease those items, else it will return false.
     */
    public boolean makeOnlineOrder(){
        if(getAddress() == null || getPhone() == null){
            //exception
        }
        return onlineDep.removeItemFromDepots(this.getOrder());
    }

    /**
     * Checks if this and given is equal by checking their customer id's and email's, if one of them is same it will be true.
     * @param o object that will be checked.
     * @return returns true if email or customer id is same.
     */
    @Override
    public boolean equals(Object o){
        if(o != null && o instanceof Customer){
            Customer temp = (Customer) o;
            if(getCustomerID().equals(temp.getCustomerID()) ||getEmail().equals(temp.getEmail()))
                return true;
            else{
                return false;
            }
        }
        else
            return false;
    }

    /**
     * To get password of the customer.
     * @return returns String which will have password.
     */
    public String getPassword(){
        return password;
    }
}
