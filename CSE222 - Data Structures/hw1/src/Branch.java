import java.io.Serializable;

/**
 * Branch class which holds information of a branch.
 * @author Yunus_Emre_Ozturk
 */
public class Branch implements Serializable{
    private Depot branchDepot;
    private ArrayContainer<Employee> employees;
    private String branchName;
    private CustomerList custList;
    private OnlineDepot onlineDep;
    private InfoBox infobox;

    /**
     * Branch constructor.
     * @param name name of the branch.
     * @param custList customer list of the company.
     * @param onlineDep online depot of the company.
     */
    public Branch(String name, CustomerList custList, OnlineDepot onlineDep){
        branchName = name;
        employees = new ArrayContainer<>();
        branchDepot = new Depot(this);
        if(custList == null ||onlineDep == null){
            throw(new IllegalArgumentException());
        }
        infobox = new InfoBox();
        this.custList = custList;
        this.onlineDep = onlineDep;
    }

    /**
     * To get information box of branch.
     * @return returns information box of branch.
     */
    public InfoBox getInfoBox(){
        return infobox;
    }

    /**
     * To find employee in brannch by ID.
     * @param ID id of employee.
     * @return returns Employee object.
     */
    public Employee findEmployee(String ID){
        for(int i = 0; i < employees.getSize(); ++i){
            if(employees.get(i).getID().equals(ID))
                return employees.get(i);
        }

        return null;
    }

    /**
     * To get Depot of the branch.
     * @return returns Depot object of the branch.
     */
    public Depot getDepot(){
        return branchDepot;
    }

    /**
     * To get Online Depot.
     * @return returns Online Depot object.
     */
    public OnlineDepot getOnlineDep(){
        return onlineDep;
    } 

    /**
     * To get Customer List.
     * @return returns CustomerList object.
     */
    public CustomerList getCustList(){
        return custList;
    }

    /**
     * Adds employee to current branch.
     * @param emp Employee object which will be added to employees of branch.
     * @return void.
     */
    public void addEmployee(Employee emp){
        employees.add(emp);
    }

    /**
     * Removes employee by ID.
     * @param ID id of the employee will be removed.
     * @return returns true if operation is successful, else false.
     */
    public boolean removeEmployee(String ID){
        for(int i = 0; i < employees.getSize(); ++i){
            if(employees.get(i).getID().equals(ID)){
                employees.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * To get name of te branch.
     * @return Branch name in String type.
     */
    public String getName(){
        return branchName;
    }
    
    /**
     * Checks if this and given branch is equal or not by checking it's names.
     * @return returns true if both has same name.
     */
    @Override
    public boolean equals(Object o){
        if(o != null && o instanceof Branch){
            Branch temp = (Branch) o;
            if(temp.getName().equals(getName()))
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /**
     * If user prints the branch it will give employee, depot and named information in string.
     * @return String which holds information about branch.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();
        strBuild.append(branchDepot).append("\n").append("Employees:");
        for(int i = 0; i < employees.getSize(); ++i){
            strBuild.append("   ").append(employees.get(i).toString()).append("\n");
        }
        return strBuild.toString();
    }
}
