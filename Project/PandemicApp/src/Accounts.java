import Exceptions.ClientExistsException;
import java.util.*;
import java.io.*; 
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Accounts {
    
    private HashMap<String, User> users;
    private Lock lock;
    
    public Accounts()
    {
        users = new HashMap<>();
        this.lock = new ReentrantLock();
    }
    
    public void register(String username, String password, PrintWriter writer) throws ClientExistsException {
        this.lock.lock();
        try {
            if (this.users.containsKey(username)) {
                throw new ClientExistsException("Utilizador " + username + " já registado!");
            }
            User user = new User(username, password, writer);
            this.users.put(username, user);
        } finally {
            this.lock.unlock();
        }
    }
}
