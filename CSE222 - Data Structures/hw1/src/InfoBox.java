import java.io.Serializable;

/**
 * InfoBox class, it using for communication between branchemployees and managers or admins, extends Serializable to make copy of it.
 * @author Yunus_Emre_Ozturk
 */
public class InfoBox implements Serializable{
    private ArrayContainer<String> box;

    /**
    * InfoBox constructor.
    */
    public InfoBox(){
        box = new ArrayContainer<>();
    }

    /**
     * adds restock information to the box.
     * @param branch takes which branch needs restock
     * @param item takes what item needs to restock in the branch
     * @return void
    */
    public void addRestockInfo(Branch branch, Item item){
        if(item == null || branch == null){
            throw(new IllegalArgumentException());
        }
        box.add(branch.getName() + item.toString());
    }

    /**
     * To get box String from last.
     * @param ind index of the element but it starts with end of the array.
     * @return returns message at the index as string type.
    */
    public String getInfoFromLast(int ind){
        return box.get(box.getSize() - 1 - ind);
    }

    /**
     * To remove box String from last.
     * @param ind index of the array but it starts with end of the array.
     * @return returns true if index in inner bounds.
    */
    public boolean removeInfo(int ind){
        if(ind >= 0 && ind < box.getSize()){
            box.remove(box.getSize() - 1 - ind);
            return true;
        }
        return false;
    }   

    /**
     * To get message box information as String.
     * @return returns the message box information in string type.
    */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        for(int i = 0; i < box.getSize(); ++i){
            strBuild.append("RESTOCK: ");
            strBuild.append(String.valueOf(i+1)).append(") ").append(getInfoFromLast(i)).append('\n');
        }
        return strBuild.toString();
    }
}
