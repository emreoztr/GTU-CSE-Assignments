import java.io.Serializable;

/**
     * Abstract class which holds user informations such as name and surname, implements Serializable to create local copy.
     * @author Yunus_Emre_Ozturk
     */
public abstract class User implements Serializable{
    private String name;
    private String surname;
    
    /**
     * User constructor.
     * @param name name of the user.
     * @param surname surname of the user.
     */
    public User(String name, String surname){
        setName(name);
        setSurname(surname);
    }

    /**
     * To get name of the user.
     * @return returns the name of the user as String type.
     */
    public String getName(){
        return name;
    }
    
    /**
     * To get surname of the user.
     * @return returns the surname of the user as String type.
     */
    public String getSurname(){
        return surname;
    }

    /**
     * Changes the name of the user.
     * @return void.
     * @throws IllegalArgumentException if name is problematic.
     */
    public void setName(String name) throws IllegalArgumentException{
        if(name != null && name.length() > 0)
            this.name = name;
        else{
            throw(new IllegalArgumentException());
        }
    }

    /**
     * Changes the surname of the user.
     * @return void.
     * @throws IllegalArgumentException if surname is problematic.
     */
    public void setSurname(String surname) throws IllegalArgumentException{
        if(surname != null && surname.length() > 0)
            this.surname = surname;
        else{
            throw(new IllegalArgumentException());
        }
    }
}
