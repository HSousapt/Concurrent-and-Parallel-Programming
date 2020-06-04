import Exceptions.ClientExistsException;
import Exceptions.InexistentUserException;
import java.util.*;
import java.io.*; 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Accounts {
    
    private final Hashtable<String, User> users;
    private final Hashtable<Integer, User> logged_users;
    private Lock lock;
    private int userIds;
    private double average;
    
    public Accounts()
    {
        this.users = new Hashtable<>();
        this.lock = new ReentrantLock();
        this.userIds = 0;
        this.logged_users = new Hashtable<>();
        this.average = 0.0;
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
            {
                int id = this.users.get(username).getId();
                User u = this.users.get(username);
                this.logged_users.put(id, u);
            }
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
            int id = this.users.get(username).getId();
            this.logged_users.remove(id);
        }finally{
            this.lock.unlock();
        }
    }
    
    public int known_cases(String username)
    {
        this.lock.lock();
        try{
            return (this.logged_users.get(this.users.get(username).getId()).getCases());
        }finally{
            this.lock.unlock();
        }   
    }
    
     public double get_average(String username)
    {
        this.lock.lock();
        try{
            return this.average;
        }finally{
            this.lock.unlock();
        }   
    }
    
    public void update(String username, int cases)
    {
        this.lock.lock();
        try{
            if(cases < 0)
                cases = 0;
            this.logged_users.get(this.users.get(username).getId()).setCases(cases);
            multicast(username);
        }finally{
            this.lock.unlock();
        }       
    }
    
    public void multicast(String name)
    {
            List<String> user_names = new ArrayList<>(this.users.keySet());
            int n_users = 0;
            double total = 0;
            this.users.get(name).setFlag(true);
            for (Map.Entry<String, User> pair : this.users.entrySet()) {
                User u = pair.getValue();
                total += u.getCases();
                if(u.getFlag()==true)
                {
                    n_users += 1;
                }
            }
            
            double av = total/(n_users * 150);
            this.average = av;
            String message = "M OK Current average of infected people -> " + av;
            for(User user : this.logged_users.values())
            {
                PrintWriter out = user.getWritter();
                out.println(message);
                out.flush();
            }   
    }
}
