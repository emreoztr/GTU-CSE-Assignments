import java.io.Serializable;

/**
 * Abstract IDBuilder class, it's job is generating ID of the users, implements Serializable to take a copy.
 * @author Yunus_Emre_Ozturk
 */
public abstract class IDBuilder implements Serializable{
    private static int customerCount = 0;
    private static int traderCount = 0;
    private static int productCount = 0;
    public final static int ID_LEN = 7;
    
    /**
     * Static customer id generator method, it's job is generate customer id in hexadecimal.
     * @return returns customer id in a string type.
     * @throws IDLimitException custom exception about overflow in ID limit.
     */
    public static String generateCustomerID() throws IDLimitException{
        String returnVal = generateID(customerCount, 'C');
        customerCount++;
        return returnVal;
    }

    public static String generateTraderID() throws IDLimitException{
        String returnVal = generateID(traderCount, 'T');
        traderCount++;
        return returnVal;
    }

    public static String generateProductID() throws IDLimitException{
        String returnVal = generateID(productCount, 'P');
        productCount++;
        return returnVal;
    }

    private static String generateID(int count, char fistChar) throws IDLimitException{
        StringBuilder strBuild = new StringBuilder();
        String mainID = Integer.toHexString(count);
        
        strBuild.append(fistChar);
        if(mainID.length() > ID_LEN){
            throw(new IDLimitException());
        }
        for(int i = 0; i < ID_LEN - mainID.length(); ++i)
            strBuild.append("0");
        
        strBuild.append(mainID);

        return strBuild.toString();
    }

    public static void setCustomerCount(int c){
        customerCount = c;
    }

    public static void setTraderCount(int c){
        traderCount = c;
    }
    
    public static void setProductCount(int c){
        productCount = c;
    }
}
