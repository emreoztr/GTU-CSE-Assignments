import java.io.Serializable;
/**
 * Abstract Employee class to give definition some common functions, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public abstract class Employee extends User implements Serializable{
    private String empID;
    
    /**
     * Employee constructor, it will generate an employee ID too.
     * @param name name of the employee in string type.
     * @param surname surname of the employee in string type.
     */
    public Employee(String name, String surname){
        super(name, surname);
        try {
            empID = IDBuilder.generateEmployeeID();
        } catch (IDLimitException e) {
            System.out.println("ID limit is exceeded, please contact with programmer!");
            e.printStackTrace();
        }
    }

    /**
     * To get ID of the employee.
     * @return returns employee id with string type.
     */
    public String getID(){
        return empID;
    }

    /**
     * To get information about employee with string, it will have name, surname and ID.
     * @return returns employee information with string.
     */
    @Override
    public String toString(){
        StringBuilder strBuild = new StringBuilder();

        strBuild.append("Name: ").append(getName()).append(";Surname: ").append(getSurname())
        .append(";ID: ").append(getID()).append(";");

        return strBuild.toString();
    }
}
