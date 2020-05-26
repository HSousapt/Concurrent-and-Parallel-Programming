import java.io.*; 
import java.util.*; 
import java.net.*; 

public class Client {

    
    public static void help()
    {
        System.out.println("To Sign-In type: register");
        System.out.println("To Log-In write: login");
        System.out.println("To leave write: quit");
    }
    
    public static void main(String[] args) throws IOException{
        
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName("localhost"); 
        
        Socket socket = new Socket(ip, 12345);
        PrintWriter cout = new PrintWriter( socket.getOutputStream(), true);
        BufferedReader cin = new BufferedReader( new InputStreamReader( socket.getInputStream() ));
        BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ));
        System.out.println("Connection Established!");
        System.out.println("type 'help' for help");
        
            
        while(true)
            {
                System.out.print("> ");
                String command = keyboard.readLine();
               
                cout.println(command);
                
                if(command.equalsIgnoreCase(("quit"))) 
                    break;
                else if(command.equalsIgnoreCase(("help")))
                    help();
                
                String server_msg = cin.readLine();
                
                
                System.out.println(server_msg);
            }
            
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
        System.out.println("Connection closed!");
        System.exit(0);
    }
        
 }