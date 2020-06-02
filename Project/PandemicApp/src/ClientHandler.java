import Exceptions.ClientExistsException;
import Exceptions.InexistentUserException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    
    private final Socket clientSocket;
    private Accounts accs;
    
    public ClientHandler(Socket clientSocket, Accounts accs)
    {
        this.clientSocket = clientSocket;
        this.accs = accs;
    }
    
    @Override
    public void run()
    {
        try {
            handleClientSocket(clientSocket);
        } catch (IOException e) {
            e.getStackTrace();
        } catch (InterruptedException |  ClientExistsException e) {
            e.getStackTrace();
        }
    }
    
    void regist(String username, String password, PrintWriter out) throws ClientExistsException
    {
        try{
            accs.register(username, password, out);
            out.println("R OK " + username);
        }catch(ClientExistsException e)
        {
                out.println("R NOT " + e.getMessage());
        }
    }
    
    void log(String username, String password, PrintWriter out) throws InexistentUserException
    {
        try{
            accs.login(username, password, out);
            out.println("L OK " + username);
        }catch(InexistentUserException e)
        {
            out.println("L NOT " + e.getMessage());
        }
    }
    
    void log_off(String username, PrintWriter out)
    {
       accs.logoff(username);
       out.println("O OK " + username);
    }
    
    void show(String username, PrintWriter out)
    {
        int cases = accs.known_cases(username);
        out.println("S OK " + username + " " + cases);
    }
    
    void update_cases(String username, String cases, PrintWriter out)
    {
        int new_cases;
        try 
        {
            new_cases = Integer.parseInt(cases);
        }
        catch (NumberFormatException e)
        {
            new_cases = 0;
        }
        accs.update(username, new_cases);
        out.println("U OK");
    }

    private int handle_cmds(String[] tokens, PrintWriter out) throws ClientExistsException
    {
            if(tokens != null && tokens.length > 0)
            {
                String cmd = tokens[0];
                if("quit".equalsIgnoreCase(cmd))
                    return -1;
                
                else if("register".equalsIgnoreCase(cmd))
                {
                    String username =  tokens[1];
                    String passwrd =  tokens[2];
                    regist(username, passwrd, out);
                }
                else if("login".equalsIgnoreCase(cmd))
                {
                    String username =  tokens[1];
                    String passwrd =  tokens[2];
                    log(username, passwrd, out);                    
                }
                else if("logoff".equalsIgnoreCase(cmd))
                {
                    log_off(tokens[1], out);
                }
                else if("show".equalsIgnoreCase(cmd))
                {
                    show(tokens[1], out);
                }
                else if("update".equalsIgnoreCase(cmd))
                {
                    update_cases(tokens[1], tokens[2], out);
                }
                else if("broadcast".equalsIgnoreCase(cmd))
                {
                    accs.multicast();
                }
                else
                {
                    String unkown = "ERROR! Unknown command: " + cmd;
                    out.println(unkown);
                }
            }
            return 0;
    }

    
    private void handleClientSocket(Socket cs) throws IOException, InterruptedException, ClientExistsException
    {
        
        BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));    
        PrintWriter out = new PrintWriter(cs.getOutputStream(), true);
        String msg;
        
        while((msg = in.readLine()) != null)
        {
            String[] tokens = msg.split(" ");
            int r = handle_cmds(tokens, out);
            if(r == -1) break;
            
        }
        
    }
    
}
