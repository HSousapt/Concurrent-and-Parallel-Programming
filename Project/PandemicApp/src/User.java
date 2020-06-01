import java.io.*;

public class User {
    private String username;
    private String password;
    private int cases;
    private boolean logged;
    private PrintWriter out;
    
    public User(String user, String pass, PrintWriter out)
    {
        this.username = user;
        this.password = pass;
        this.out = out;
        this.cases = 0;
        this.logged = false;
    }
    
    public int getCases()
    {
        return this.cases;
    }
    
    public void setCases(int newcases)
    {
        this.cases=newcases;
    }
    
    public String getUsername()
    {
        return this.username;
    }
    
    public void setUsername(String name)
    {
        this.username = name;
    }
    
    public String getPassword()
    {
        return this.password;
    }
    
    public boolean isLogged()
    {
        return this.logged;
    }
    
    public void setLog(boolean islogged)
    {
        this.logged = islogged;
    }
    
    public PrintWriter getWritter()
    {
        return this.out;
    }
    
    public String getPass()
    {
        return this.password;
    }
    
    public void setPass(String pswd)
    {
        this.password = pswd;
    }
}
