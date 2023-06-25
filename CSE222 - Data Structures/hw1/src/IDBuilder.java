import java.io.Serializable;

/**
 * Abstract IDBuilder class, it's job is generating ID of the users, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public abstract class IDBuilder implements Serializable{
    private static int customerCount = 1;
    private static int employeeCount = 1;
    private final static int max_digit = 5;
    public final static int ID_LEN = max_digit + 1;
    
    /**
     * Static customer id generator method, it's job is generate customer id in hexadecimal.
     * @return returns customer id in a string type.
     * @throws IDLimitException custom exception about overflow in ID limit.
     */
    public static String generateCustomerID() throws IDLimitException{
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("C"); //'C' is for represent 'C'ustomer
        String mainID = Integer.toHexString(customerCount);
        if(mainID.length() > max_digit){
            throw(new IDLimitException());
        }
        for(int i = 0; i < max_digit - mainID.length(); ++i)
            strBuild.append("0");
        
        strBuild.append(mainID);
        customerCount++;
        return strBuild.toString();
    }

    /**
     * Static employee id generator method, it's job is generate employee id in hexadecimal.
     * @return returns employee id in a string type.
     * @throws IDLimitException custom exception about overflow in ID limit.
     */
    public static String generateEmployeeID() throws IDLimitException{
        StringBuilder strBuild = new StringBuilder();
        strBuild.append("E"); //'E' is for represent 'E'mployee
        String mainID = Integer.toHexString(employeeCount);
        if(mainID.length() > max_digit){
            throw(new IDLimitException());
        }
        for(int i = 0; i < max_digit - mainID.length(); ++i)
            strBuild.append("0");
        
        strBuild.append(mainID);
        employeeCount++;
        return strBuild.toString();
    }

    public static void setEmployeeCount(int count){
        employeeCount = count + 1;
    }

    public static void setCustomerCount(int count){
        customerCount = count + 1;
    }
}
