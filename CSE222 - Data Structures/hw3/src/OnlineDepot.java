import java.io.Serializable;
import java.util.ListIterator;

/**
     * It holds all the branch depots and helps to find items in online orders, implements serializable to local copy.
     * @author Yunus_Emre_Ozturk
     */
public class OnlineDepot implements Serializable{
    private KWArrayList<Depot> depots;

    /**
     * OnlineDepot constructor.
     */
    public OnlineDepot(){
        depots = new KWArrayList<>();
    }

    /**
     * Adds depot to the online depot.
     * @param depot depot that will be added.
     * @return void.
     */
    public void addDepot(Depot depot){
        if(depot == null){
            throw(new IllegalArgumentException());
        }
        depots.add(depot);
    }

    /**
     * Removes given depot from the online depot list.
     * @param depot depot that will be removed.
     * @return void.
     */
    public void removeDepot(Depot depot){
        for(int i = 0; i < depots.size(); ++i){
            if(depots.get(i) == depot)
                depots.remove(i);
        }
    }

    /**
     * Removes items from depots based on the customer order and helps to finish the online order.
     * @param order order that will be processed.
     * @return returns false if item or items couldn't find in the depots.
     */
    public boolean removeItemFromDepots(Order order){
        boolean found = false;

        for(int i = 0; i < depots.size() && found == false; ++i){
            for(int j = 0; j < order.getOrderCount(); ++j){
                if(depots.get(i).isContain(order.getOrderfromLatest(j))){
                    found = true;
                }
                else{
                    found = false;
                    break;
                }
            }
            if(found){
                for(int k = 0; k < order.getOrderCount(); ++k)
                    depots.get(i).removeFromDepot(order.getOrderfromLatest(k));
                
                order.removeAllOrders();
                return true;
            }
            
        }
        return false;
    }

    /**
     * Helps product based on it's name, not model or color.
     * @param name name of the product.
     * @return returns item informations as string type.
     */
    public String searchProduct(String name){
        StringBuilder strBuild = new StringBuilder();
        for(int i = 0; i < depots.size(); ++i){
            HybridList<Item> founded = depots.get(i).searchProduct(name);
            if(founded.size() > 0)
                strBuild.append(depots.get(i).getBranch().getName() + ":\n");
            ListIterator listIter = founded.listIterator();
            while(listIter.hasNext()){
                Item item = (Item) listIter.next();
                strBuild.append("   ").append(item).append("\n");
            }
        }
        return strBuild.toString();
    }

    /**
     * Turning online depot informations to a string.
     * @return returns depot informations as string type.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        String[] parts;
        for(int i = 0; i < depots.size(); ++i){
            parts = depots.get(i).toString().split(";");
            for(int j = 0; j < parts.length; ++j){
                if(j % 4 != 3){
                    strBuild.append(parts[j] + ";");
                }
            }
        }
        
        return strBuild.toString();
    }
}
