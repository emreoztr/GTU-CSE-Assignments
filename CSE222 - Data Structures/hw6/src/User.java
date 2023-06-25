public class User {
    private AppSystem comp;
    private String name;
    private String id;
    private String password;

    public User(AppSystem comp, String id, String name, String pass){
        if(comp == null || id == null || name == null || pass == null)
            throw new NullPointerException();
        this.comp = comp;
        setId(id);
        setName(name);
        setPassword(pass);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AppSystem getAppSystem(){
        return comp;
    }
}
