import java.io.*;

public class User {
    private String username;
    private String password;
    private int cases;
    private boolean flag;
    private PrintWriter out;
    private int id;
    
    public User(String user, String pass, PrintWriter out, int user_id)
    {
        this.username = user;
        this.password = pass;
        this.out = out;
        this.cases = 0;
        this.flag = false;
        this.id = user_id;
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
    
    public boolean getFlag()
    {
        return this.flag;
    }
    
    public void setFlag(boolean f)
    {
        this.flag = f;
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
    
    public int getId()
    {
        return this.id;
    }
}
