import java.io.Serializable;
/**
     * It hold item information such as name, model, color and quantity of the item.
     * @author Yunus_Emre_Ozturk
    */
public class Item implements Cloneable, Serializable{
    private String name;
    private String model;
    private String col;
    private int quantity = 1;

    /**
     * Item constructor.
     * @param name name of the item.
     * @param model model of the item.
     * @param col color of the item.
     * @param quantity quantity of the item, shouldn't be negative.
    */
    public Item(String name, String model, String col, int quantity){
        setName(name);
        setModel(model);
        setColor(col);
        setQuantity(quantity);
    }

    /**
     * To get name of the item.
     * @return returns name of the item as String type.
    */
    public String getName(){
        return name;
    }

    /**
     * To get model of the item.
     * @return returns model of the item as String type.
    */
    public String getModel(){
        return model;
    }

    /**
     * To get color of the item.
     * @return returns color of the item as String type.
    */
    public String getColor(){
        return col;
    }

    protected void setName(String name){
        if(name != null && name.length() > 0)
            this.name = name;
        else{
            throw(new IllegalArgumentException()); 
        }
    }

    protected void setModel(String model){
        if(model != null && model.length() > 0)
            this.model = model;
        else{
            throw(new IllegalArgumentException()); 
        }
    }

    protected void setColor(String col){
        if(col != null)
            this.col = col;
        else{
            throw(new IllegalArgumentException());
        }
    }

    /**
     * Sets quantity of the item.
     * @param quantity new quantity, cannot be lower than 0.
     * @return void.
    */
    public void setQuantity(int quantity) {
        if(quantity >= 0)
            this.quantity = quantity;
        else{
            throw(new IllegalArgumentException());
        }
    }

    /**
     * To get quantity of the item.
     * @return returns quantity of the item as int type.
    */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Checks if this and given object has same name, model and color, doesn't check quantity because it is independent aspect of the item.
     * @param obj object that will be checked
     * @return returns true if name, model and color of the item is same.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Item){
            Item item = (Item) obj;
            if(item.name.equals(this.name)&&
               item.model.equals(this.model)&&
               item.col.equals(this.col))
               return true;
        }
        return false;
    }

    /**
     * Turning item information to a String.
     * @return returns item informations as string type.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();

        strBuild.append("Name: " + getName() + ";")
                .append("Model: " + getModel() + ";")
                .append("Color: " + getColor() + ";")
                .append("Quantity: " + getQuantity() + ";");
                
        return strBuild.toString();
    }

    /**
     * Clones the item and sets the quantity of the clone to 0.
     * @return returns upcasted Item as Object. 
     */
    @Override
    public Object clone(){
        Item returnVal = new Item(this.name, this.model, this.col, 0);
        return ((Object) returnVal);
    }
}
