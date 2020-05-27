import java.io.*; 
import java.util.*; 
import java.net.*; 

class ClientWriter implements Runnable{
    
    private final Socket cs;
    
    public ClientWriter(Socket cs) throws IOException {
        this.cs = cs;
    }
    @Override
    public void run() {
        try{
            PrintWriter cout = new PrintWriter( cs.getOutputStream(), true);
            BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ));
             while(true)
            {       
                System.out.print("> ");
                String command = keyboard.readLine();
               
                cout.println(command);
                
                if(command.equalsIgnoreCase(("quit"))) 
                    break;
            }        
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}


class ClientReader implements Runnable {
    
    private final Socket cs;
    
    public ClientReader(Socket cs) throws IOException {
        this.cs = cs;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String s;
            String[] data;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
            }
            in.close();
            cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Client{
    public static void main(String[] args){
        try {
            // getting localhost ip 
            InetAddress ip = InetAddress.getByName("localhost"); 
            Socket cs = new Socket(ip, 12345);
            Thread tWrite = new Thread(new ClientWriter(cs));
            Thread tRead = new Thread(new ClientReader(cs));
            tWrite.start();
            tRead.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}