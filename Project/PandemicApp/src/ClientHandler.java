import Exceptions.ClientExistsException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


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
                    String passwrd =  tokens[1];
                    accs.register(username, passwrd, out);
                    out.println(username + " you have been registered!!");
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
