import java.io.Serializable;
/**
 * Depot class to easier access Depots in branches, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public class Depot implements Serializable{
    private ArrayContainer<Item> items;
    private Branch branch;
    
    /**
     * Depot constructor.
     * @param items it will take item container.
     * @param branch it will take branch information of the depot.
     */
    public Depot(ArrayContainer<Item> items, Branch branch){
        if(items != null && branch != null){
            this.items = items;
            this.branch = branch;
        }
        else{
            throw(new IllegalArgumentException());
        }
    }

    /**
     * Depot constructor.
     * @param item it will take the first item which will be in the List.
     * @param branch it will take branch information of the depot.
     */
    public Depot(Item item, Branch branch){
        items = new ArrayContainer<>();
        this.branch = branch;
        addToDepot(item);
    }

    /**
     * Depot constructor.
     * @param branch it will take branch information of the depot.
     */
    public Depot(Branch branch){
        if(branch == null){
            throw(new IllegalArgumentException());
        }
        this.branch = branch;
        items = new ArrayContainer<>();
    }   

    /**
     * Adds item to the depot, if there is a item with same name, color and model it will add the quantity of this item to already available item.
     * @param item item will be added to the depot.
     * @return it will return false if item quantity is less than 0
     */
    public boolean addToDepot(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        if(item.getQuantity() < 0)
            return false;

        if(!items.contains(item))
            items.add(item);
        else
            for(int i = 0; i < items.getSize(); ++i)
                if(items.get(i).equals(item))
                    items.get(i).setQuantity(items.get(i).getQuantity() + item.getQuantity()); //increases quantity
        return true;
    }

    /**
     * Removes item from the depot based on it's quantity.
     * @param item item will be removed from the depot.
     * @return it will return false if the quantity is bigger or there is no such a item with same name, model and color.
     */
    public boolean removeFromDepot(Item item){
        if(item == null){
            throw(new IllegalArgumentException());
        }
        for(int i = 0; i < items.getSize(); ++i)
            if(items.get(i).equals(item)){
                if(items.get(i).getQuantity() >= item.getQuantity()){
                    items.get(i).setQuantity(items.get(i).getQuantity() - item.getQuantity());
                    return true;
                }
                else{
                    return false;
                }
            }

        return false;
    }

    /**
     * Removes item based on a Customer order.
     * @param order order that will be processed.
     * @return it will return false if the quantity is bigger or there is no such a item with same name, model and color.
     */
    public boolean removeWithOrder(Order order){
        boolean contains = true;
        for(int i = 0; i < order.getOrderCount() && contains; ++i){
            if(!isContain(order.getOrderfromLatest(i)))
                contains = false;
        }
        if(!contains)
            return false;

        for(int i = 0; i < order.getOrderCount(); ++i){
            removeFromDepot(order.getOrderfromLatest(i));
        }
        return true;
    }

    /**
     * Checks if depot contains the item based on the name, color and model and checks the quantity if it is bigger or not.
     * @param item item that will be checked.
     * @return it will return false if there is no item with same name, color and model, if there is a item with same name, color and model it will check the quantity if quantity is less than the parameter it will return false again.
     */
    public boolean isContain(Item item){
        if(item == null)
        {
            throw(new IllegalArgumentException());
        }
        for(int i = 0; i < items.getSize(); ++i){
            if(items.get(i).equals(item) && items.get(i).getQuantity() >= item.getQuantity())
                return true;
        }
        return false;
    }

    /**
     * To get branch information of the depot.
     * @return it will return it's branch.
     */
    public Branch getBranch(){
        return branch;
    }

    /**
     * To get all the products with the given name.
     * @param name it will take name as a string to search.
     * @return it will return container filled with products in same name.
     */
    public ArrayContainer<Item> searchProduct(String name){
        ArrayContainer<Item> founded = new ArrayContainer<>();
        for(int i = 0; i < items.getSize(); ++i){
            if(items.get(i).getName().equals(name)){
                founded.add(items.get(i));
            }
        }
        return founded;
    }

    /**
     * To get String type of the Depot to get Information.
     * @return it will string with the name of depot's branch and the item information in it.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(branch.getName() + ":\n");
        for(int i = 0; i < items.getSize(); ++i){
            strBuild.append("   ").append(items.get(i)).append("\n");
        }
        return strBuild.toString();
    }
}
