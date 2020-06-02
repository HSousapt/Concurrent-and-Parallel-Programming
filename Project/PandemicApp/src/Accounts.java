import Exceptions.ClientExistsException;
import Exceptions.InexistentUserException;
import java.util.*;
import java.io.*; 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Accounts {
    
    private HashMap<String, User> users;
    private HashMap<Integer, User> logged_users;
    private Lock lock;
    private int userIds;
    
    public Accounts()
    {
        this.users = new HashMap<>();
        this.lock = new ReentrantLock();
        this.userIds = 0;
        this.logged_users = new HashMap<>();
    }
    
    public void register(String username, String password, PrintWriter writer) throws ClientExistsException
    {
        this.lock.lock();
        try {
            if (this.users.containsKey(username)) {
                throw new ClientExistsException("User " + username + " already registered!");
            }
            this.userIds += 1;
            User user = new User(username, password, writer, this.userIds);
            this.users.put(username, user);
        } finally {
            this.lock.unlock();
        }
    }
    
    public boolean log_user(String username, String password, PrintWriter writer)
    {
        this.lock.lock();
        try{
            if(this.users.containsKey(username) == false)
            {
                return false;
            }
            else
                return this.users.get(username).getPassword().equals(password);
        }finally {
            this.lock.unlock();
        }
    }
    
    public void login(String username, String password, PrintWriter writer) throws InexistentUserException
    {
        this.lock.lock();
        try{
            if(log_user(username, password, writer) == true)
                this.logged_users.put(this.userIds, this.users.get(username));
            else
                throw new InexistentUserException("Incorrect Password! or Not Registered User!");
        }finally {
            this.lock.unlock();
        }
    }
    
    public void logoff(String username)
    {
        this.lock.lock();
        try{
            this.logged_users.remove(this.users.get(username).getId());
        }finally{
            this.lock.unlock();
        }
    }
    
    public int known_cases(String username)
    {
        int cases;
        this.lock.lock();
        try{
            cases = (this.logged_users.get(this.users.get(username).getId()).getCases());
        }finally{
            this.lock.unlock();
        }
        
        return cases;
    }
}
