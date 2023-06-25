import java.io.Serializable;
/**
 * CustomerList class to easier access customers in array container, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public class CustomerList implements Serializable{
    private ArrayContainer<Customer> customers;

    /**
     * CustomerList constructor.
     */
    public CustomerList(){
        customers = new ArrayContainer<>();
    }

    /**
     * Adds customer to the array container.
     * @param customer Customer will be added to the container.
     * @return return true if there is no problem bu it returns false if there is a customer with same ID (impossible) or mail.
     */
    public boolean addCustomer(Customer customer){
        if(customer == null){
            throw(new IllegalArgumentException());
        }
        if(!customers.contains(customer)){
            customers.add(customer);
            return true;
        }
        return false;
    }

    /**
     * To get customer with using it's ID.
     * @param ID id of the customer in String type.
     * @return returns customer if ID is correct if not it will return null.
     */
    public Customer getCustomerWithID(String ID){
        for(int i = 0; i < customers.getSize(); ++i){
            if(customers.get(i).getCustomerID().equals(ID))
                return customers.get(i);
        }
        return null;
    }

    /**
     * To get customer with using it's mail.
     * @param mail mail of the customer in String type.
     * @return returns customer if ID is correct if not it will return null.
     */
    public Customer getCustomerWithMail(String mail){
        for(int i = 0; i < customers.getSize(); ++i){
            if(customers.get(i).getEmail().equals(mail))
                return customers.get(i);
        }
        return null;
    }

    /**
     * To get customer count in the list.
     * @return returns customer count in iteger type.
     */
    public int getCustomerCount(){
        return customers.getSize();
    }
}
