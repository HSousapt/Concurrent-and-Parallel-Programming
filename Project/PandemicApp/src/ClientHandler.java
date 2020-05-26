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
    
    public ClientHandler(Socket clientSocket)
    {
        this.clientSocket= clientSocket;
    }
    
    @Override
    public void run()
    {
        try {
            handleClientSocket(clientSocket);
        } catch (IOException e) {
            e.getStackTrace();
        } catch (InterruptedException e) {
            e.getStackTrace();
        }
    }
 

    private int handle_cmds(String[] tokens, PrintWriter out)
    {
            if(tokens != null && tokens.length > 0)
            {
                String cmd = tokens[0];
                if("quit".equalsIgnoreCase(cmd))
                    return -1;
                
                if("register".equalsIgnoreCase(cmd))
                {
                    out.println("chegueeeei!");
                }
                else
                {
                    String unkown = "ERROR! Unknown command: " + cmd;
                    out.println(unkown);
                }
            }
            return 0;
    }

    
    private void handleClientSocket(Socket cs) throws IOException, InterruptedException
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
        
        cs.close();
        cs.shutdownOutput();
        cs.shutdownInput();
    }
    
}
