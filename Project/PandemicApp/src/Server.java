import java.io.*; 
import java.util.*; 
import java.net.*;

public class Server {

    static HashMap<String, Integer> registered_clients = new HashMap<>();
    static HashMap<Integer, ClientHandler> logged_clients;
    
    public static void main(String[] args) throws IOException{
        
        //Opening server socket on port 12345
        ServerSocket ss = new ServerSocket(12345);
        
        //Socket listening for client requests
        Socket cs;
 
        try{
            Accounts accs = new Accounts();
            while(true)
            {
                //Server socket accepts incoming request
                System.out.println("Waiting for client connection...");
                cs = ss.accept();
                System.out.println("New connection: " + cs.getInetAddress().getHostAddress());               
                ClientHandler handler = new ClientHandler(cs, accs);               
                Thread t_handler = new Thread(handler);                
                t_handler.start();

            }
        }catch(IOException e)
        {
            System.out.println("fodasse");
            e.printStackTrace();
        }
    }
} 
