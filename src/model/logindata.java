package model;

public class logindata {
    private String username;
    private String email;
    private String password;
    private String userType;  
    
    // Default constructor 
    public logindata() {}
    
    // Existing constructor (keep this)
    public logindata(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    
    
    public logindata(String username, String email, String password, String userType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }
    
    // Existing getters/setters 
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    // getter/setter
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}

