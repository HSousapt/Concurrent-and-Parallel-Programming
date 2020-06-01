import java.io.*; 
import java.util.*; 
import java.net.*;

class ServerWriter implements Runnable{
    
}







public class Server
{
    
}





/*

public class Server {

    static int n_clients = 0;
    private ArrayList<ClientHandler> active_clients  = new ArrayList<>();
    static HashMap<String, Integer> registered_clients = new HashMap<>();
    static HashMap<Integer, ClientHandler> logged_clients;
    
    public List<ClientHandler> getActiveClients()
    {
        return active_clients;
    }
    
    public static void main(String[] args) throws IOException{
        
        //Opening server socket on port 12345
        ServerSocket ss = new ServerSocket(12345);
        
        //Socket listening for client requests
        Socket cs;
 
        try{
        while(true)
            {
                String msg;
                //Server socket accepts incoming request
                System.out.println("Waiting for client connection...");
                cs = ss.accept();
                System.out.println("New connection: " + cs.getInetAddress().getHostAddress());
                
                ClientHandler handler = new ClientHandler(cs);
                //active_clients.add(handler);
                
                Thread t_handler = new Thread(handler);
                
                t_handler.start();

            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
} */
