import java.io.Serializable;

/**
     * Order holds the information about items in the customer orders, last order or shipped orders. Implements Serializable for local copy.
     * @author Yunus_Emre_Ozturk
     */
public class Order implements Serializable{
    private ArrayContainer<Item> lastOrder;
    private ArrayContainer<Item> shipped;
    private Customer user;

    /**
     * Order constructor.
     * @param cust Customer who will use this as Order List.
     * @param item First order.
     */
    public Order(Customer cust, Item item){
        this(cust);
        addOrder(item);
    }

    /**
     * Order constructor.
     * @param cust Customer who will use this as Order List.
     */
    public Order(Customer cust){
        if(cust == null){
            throw(new IllegalArgumentException());
        }
        user = cust;
        lastOrder = new ArrayContainer<>();
        shipped = new ArrayContainer<>();
    }


    /**
     * Adds order to the last order list.
     * @param item Item that will be added to the last order section.
     * @return returns true if item quantity is bigger than 0.
     */
    public boolean addOrder(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        if(item.getQuantity() > 0){
            lastOrder.add(item);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Adds finished order in the shipper order section.
     * @param item item that will be added to the shipped order section.
     * @return returns true if item quantity is bigger than 0.
     */
    public boolean addFinishedOrder(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        if(item.getQuantity() > 0){
            shipped.add(item);
            return true;
        }
        else
            return false;
    }

    /**
     * Removes item from last order section based on the index, index starts with the end of the array so last element is the first index.
     * @param index index of the item.
     * @return returns true if index is in the innerbounds of the last order section.
     */
    public boolean removeOrderfromLatest(int index){
        if(index >= 0 && index < lastOrder.getSize()){
            shipped.add(lastOrder.get(lastOrder.getSize() - 1 - index));
            lastOrder.remove(lastOrder.getSize() - 1 - index);
            return true;
        }
        else
            return false;
    }

    /**
     * Removes all last orders.
     * @return void.
     */
    public void removeAllOrders(){
        while(getOrderCount() > 0)
            removeOrderfromLatest(0);
        
    }

    /**
     * To get item from last order section based on the index, index starts with the end of the array so last element is the first index.
     * @param index index of the item.
     * @return returns item from the index position.
     */
    public Item getOrderfromLatest(int index){
        if(index >= 0 && index < lastOrder.getSize()){
            return lastOrder.get(lastOrder.getSize() - 1 - index);
        }
        return null;
    }

    /**
     * To get item from shipped order section based on the index, index starts with the end of the array so last element is the first index.
     * @param index index of the item.
     * @return returns item from the index position.
     */
    public Item getShippedOrderfromLatest(int index){
        if(index >= 0 && index < shipped.getSize()){
            return shipped.get(shipped.getSize() - 1 - index);
        }
        return null;
    }

    /**
     * To get item count in the last order section.
     * @return returns the count in int type.
     */
    public int getOrderCount(){
        return lastOrder.getSize();
    }

    /**
     * To get customer who uses this order object.
     * @return returns the customer.
     */
    public Customer getCustomer(){
        return user;
    }

    /**
     * Turning order informations to a string.
     * @return returns order informations as string type.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        
        strBuild.append("Last order(s):\n");
        for(int i = 0; i < lastOrder.getSize(); ++i)
            strBuild.append(String.valueOf(i + 1) + ") ").append(getOrderfromLatest(i)).append("\n");
        
        strBuild.append("Shipped order(s):\n");
        for(int i = 0; i < shipped.getSize(); ++i)
            strBuild.append(String.valueOf(i + 1) + ") ").append(getShippedOrderfromLatest(i)).append("\n");

        return strBuild.toString();
    }  
}
