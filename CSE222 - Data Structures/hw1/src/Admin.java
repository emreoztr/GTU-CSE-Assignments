import java.io.Serializable;
/**
 * Admin class, implements AdminAccess and extends from User.
 * @author Yunus_Emre_Ozturk
 */
public class Admin extends User implements AdminAccess, Serializable{
    private Company comp;

    /**
     * Admin constructor.
     * @param name name of the admin.
     * @param surname surname of the admin.
     * @param comp company of the administrator.
     */
    public Admin(String name, String surname, Company comp) throws IllegalArgumentException{
        super(name, surname);
        if(comp == null){
            throw(new IllegalArgumentException());
        }
        this.comp = comp; 
    }

    /**
     * Adds new branch to the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and adding is successfull.
     */
    public boolean addBranch(String name){
        return comp.addBranch(name);
    }

    /**
     * Removes new branch from the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and removing is successfull.
     */
    public boolean removeBranch(String name){
        Branch branch = getBranch(name);
        if(branch != null)
            comp.getOnlineDepot().removeDepot(branch.getDepot());
        return comp.removeBranch(name);
    }

    /**
     * Get branch object based on it's name.
     * @param name unique name of the branch.
     * @return returns branch object if successfull, if it is not it will return null.
     */
    public Branch getBranch(String name){
        return comp.getBranch(name);
    }

    /**
     * Adds branch employee to a branch.
     * @param branch branch object of the branch that will add employee.
     * @param name name of the employee.
     * @param surname surname of the employee.
     * @return returns Employee object.
     * @throws IllegalArgumentException if branch or name or surname is null.
     */
    public Employee addBranchEmployee(Branch branch, String name, String surname) throws IllegalArgumentException{
        if(branch == null || name == null || surname == null){
            throw(new IllegalArgumentException());
        }
        BranchEmployee emp = new BranchEmployee(name, surname, branch);
        branch.addEmployee(emp);
        comp.increaseEmployeeCount();
        return emp;
    }

    /**
     * Removes employee from a branch.
     * @param branch branch object of the branch that will remove employee.
     * @param ID unique ID number of the employee.
     * @return returns true if ID and branch iformation is true.
     * @throws IllegalArgumentException if branch or ID is null.
     */
    public boolean removeBranchEmployee(Branch branch, String ID) throws IllegalArgumentException{
        if(branch == null || ID == null){
            throw(new IllegalArgumentException());
        }
        return(branch.removeEmployee(ID));
    }
    
    /**
     * It gives texts in information box, for inform admin.
     * @param branch branch object of the branch.
     * @return returns String of texts in information box.
     * @throws IllegalArgumentException if branch is null.
     */
    public String lookInfoBoxes(Branch branch) throws IllegalArgumentException{
        if(branch == null){
            throw(new IllegalArgumentException());
        }
        return branch.getInfoBox().toString();
    }

    /**
     * It removex specific text in information box.
     * @param branch branch object of the branch.
     * @param ind index number of message.
     * @return returns true if operation is successful.
     * @throws IllegalArgumentException if branch is null.
     */
    public boolean removeInfo(Branch branch, int ind) throws IllegalArgumentException{
        if(branch == null){
            throw(new IllegalArgumentException());
        }
        return branch.getInfoBox().removeInfo(ind);
    }
    
    /**
     * It gives Online Depot.
     * @return returns Online Depot to inform admin.
     */
    public OnlineDepot getOnlineDepot(){
        return comp.getOnlineDepot();
    }
}
