/**
 * Admin interface which gives admin access to the classes.
 * @author Yunus_Emre_Ozturk
 */
public interface AdminAccess {
    /**
     * Adds new branch to the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and adding is successfull.
     */
    public boolean addBranch(String name);

    /**
     * Get branch object based on it's name.
     * @param name unique name of the branch.
     * @return returns branch object if successfull, if it is not it will return null.
     */
    public Branch getBranch(String name);

     /**
     * Removes new branch to the company.
     * @param name unique name of the branch.
     * @return returns true if branch name is unique and removing is successfull.
     */
    public boolean removeBranch(String name);

    /**
     * Adds branch employee to a branch.
     * @param branch branch object of the branch that will add employee.
     * @param name name of the employee.
     * @param surname surname of the employee.
     * @return returns Employee object.
     */
    public Employee addBranchEmployee(Branch branch, String name, String surname);

    /**
     * Removes employee from a branch.
     * @param branch branch object of the branch that will remove employee.
     * @param ID unique ID number of the employee.
     * @return returns true if ID and branch iformation is true.
     */
    public boolean removeBranchEmployee(Branch branch, String ID);

    /**
     * It gives texts in information box, for inform admin.
     * @param branch branch object of the branch.
     * @return returns String of texts in information box.
     */
    public String lookInfoBoxes(Branch branch);

    /**
     * It removex specific text in information box.
     * @param branch branch object of the branch.
     * @param ind index number of message.
     * @return returns true if operation is successful.
     */
    public boolean removeInfo(Branch branch, int ind);
}
