import java.io.*; 
import java.util.*; 
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class ClientWriter implements Runnable{
    
    private final Socket cs;
    private PrintWriter out;
    private BufferedReader keyboard;
    private String name;
    BlockingQueue<String> queue;
    
    public ClientWriter(Socket cs, BlockingQueue<String> queue) throws IOException {
        this.cs = cs;
        this.out = new PrintWriter( cs.getOutputStream(), true);
        this.keyboard = new BufferedReader( new InputStreamReader( System.in ));
        this.queue = queue;
    }
    
    @Override
    public void run()
    {
        try{
            while(true)
            {
                menu(Menu.MAIN);
                cs.shutdownInput();
                break;
            }
        }catch(IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void menu(Menu menu) throws IOException, InterruptedException
    {
        while(menu != Menu.QUIT)
        {
            switch(menu)
            {
                case MAIN:
                    menu = main_menu();
                    break;
                case LOGIN:
                    menu = login();
                    break;
                case REGISTER:
                    menu = register();
                    break;
                case LOGGED:
                    menu = logged_menu();
                    break;
                case LOGOFF:
                    menu = logoff();
                    break;
                case UPDATE:
                    menu = update_menu();
                    break;
                case SHOW:
                    menu = show();
                    break;
                case QUIT:
                    menu = main_menu();
                    break;                    
            }
        }
    }
    
    private void clear_console(int n)
    {
        for(int i = 0; i < n; i++)
            System.out.println("");
    }
    
    private void header()
    {
        System.out.println(" ********************************************************* ");
        System.out.println("|                          MENU                           |");
        System.out.println(" ********************************************************* ");   
    }
    
    private int read_choice()
    {
        clear_console(3);     
        boolean flag = false;
        int escolha = 0;
        while (!flag) {
            try {
                System.out.print("> ");  
                Scanner sc = new Scanner(System.in);
                escolha = sc.nextInt();
                flag = true;
            } catch (InputMismatchException e) {
                System.out.println("ERROR: UNKOWN COMMAND!");
            }
        }
        return escolha;        
    }
    
    private Menu main_menu()
    {
        clear_console(25);
        header();
        clear_console(5);
        System.out.println("1 - LOGIN");
        System.out.println("2 - REGISTER");
        System.out.println("0 - QUIT");
        switch(read_choice())
        {
            case 1:
                return Menu.LOGIN;               
            case 2:
                 return Menu.REGISTER;              
            case 0:
               return Menu.QUIT;                
            default:
                return Menu.MAIN;
        }
    }
    
    private Menu login() throws IOException, InterruptedException
    {
        clear_console(25);
        header();
        System.out.println("|                          LOGIN                          |");
        System.out.println(" ********************************************************* ");
        clear_console(5);
        String msg;
        boolean login = false;
        String user = null, pass = null;
        while(!login)
        {
            System.out.print("Type Your Name: ");  
            System.out.print("> ");
            this.name = keyboard.readLine();
            System.out.print("Type Your Password: ");  
            System.out.print("> ");
            pass = keyboard.readLine();
            out.println("Login " + this.name + " " + pass);    
            login = true;
        }
        msg = queue.poll(5, TimeUnit.SECONDS);
        
        if(msg.equals("OK"))
        {    
            return Menu.LOGGED;
        }
        else
        {
            int choice = read_choice();
            switch (choice)
            {
                case 0:
                    return Menu.QUIT;
                case 1:
                    return Menu.MAIN;
                default:
                    return Menu.LOGIN;
            }         
        }
    }
    
    private Menu register() throws IOException, InterruptedException
    {
        clear_console(25);
        header();
        System.out.println("|                        REGISTER                         |");
        System.out.println(" ********************************************************* ");
        clear_console(5);
        String user = null, pass = null;
        boolean accepted = false;
        while(!accepted)
        {
            System.out.print("Type Your Name: ");  
            System.out.print("> ");
            user = keyboard.readLine();
            System.out.print("Type Your Password: ");  
            System.out.print("> ");
            pass = keyboard.readLine();
            out.println("register " + user + " " + pass);
            accepted = true;
        }
        int escolha = read_choice();
        switch (escolha)
        {
            case 0:
                return Menu.QUIT;
            case 1:
                return Menu.MAIN;
            default:
                return Menu.REGISTER;
        }
    }

    private Menu logged_menu()
    {
        header();
        System.out.println("|            1 - Show | 2 - Update | 0 - Logoff           |");
        System.out.println(" ********************************************************* ");
        
        int escolha = read_choice();
        switch (escolha)
        {
            case 0:
                return Menu.LOGOFF;
            case 1:
                return Menu.SHOW;
            default:
                return Menu.UPDATE;
        }        
    }
    
    private Menu logoff() throws InterruptedException
    {
        out.println("logoff " + this.name);
        String msg = queue.poll(5, TimeUnit.SECONDS);
        if(msg.equalsIgnoreCase("OK"))
        {
            int escolha = read_choice();
            switch (escolha)
            {
                case 0:
                    return Menu.QUIT;
                case 1:
                    return Menu.MAIN;
                default:
                    return Menu.UPDATE;
            }    
        }
        else
            return Menu.MAIN;
            
    }
    
    private Menu show() throws InterruptedException
    {
        out.println("show " + this.name);
        String msg = queue.poll(5, TimeUnit.SECONDS);
        if(msg.equalsIgnoreCase("OK"))
        {
            int escolha = read_choice();
            switch (escolha)
            {
                case 0:
                    return Menu.QUIT;
                case 1:
                    return Menu.LOGGED;
                default:
                    return Menu.LOGGED;
            }    
        }
        else
            return Menu.LOGGED;
    }
    
    private Menu update_menu() throws IOException, InterruptedException
    {
        String new_cases = null;
        System.out.print("Type Your Known Cases > "); 
        new_cases = keyboard.readLine();
        out.println("update " + this.name + " " + new_cases);
        String msg = queue.poll(5, TimeUnit.SECONDS);
        if(msg.equalsIgnoreCase("OK"))
        {
           out.println("broadcast");
           String msg2 = queue.poll(5, TimeUnit.SECONDS);
           if(msg2.equalsIgnoreCase("OK"))
           {
                int escolha = read_choice();
                switch (escolha)
                {
                    case 0:
                        return Menu.QUIT;
                    case 1:
                        return Menu.LOGGED;
                    default:
                }        return Menu.LOGGED;
           }   
        }
        return Menu.LOGGED;
    }
}


class ClientReader implements Runnable {
    
    private final Socket cs;
    BlockingQueue<String> queue;
    
    public ClientReader(Socket cs, BlockingQueue<String> queue) throws IOException {
        this.cs = cs;
        this. queue = queue;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));
            String s;
            while ((s = in.readLine()) != null) {
                handle_reply(s);
            }
            in.close();
            this.cs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void handle_reply(String reply)
    {
        try{
        String[] tokens = reply.split(" ");
        switch(tokens[0])
        {
            case "R":
                if(tokens[1].equals("OK"))
                {
                    System.out.println(tokens[2] + " -> you have been registered successfully!");
                    System.out.println(" ********************************************************* ");
                    System.out.println("|              1 - Previous menu | 0 - Quit               |");
                    System.out.println(" ********************************************************* ");   
                }
                else
                {
                    System.out.println(reply.substring(6, reply.length()));
                    System.out.println(" ********************************************************* ");
                    System.out.println("|              1 - Previous menu | 0 - Quit               |");
                    System.out.println(" ********************************************************* ");   
                }
                break;
            case "L":
                if(tokens[1].equals("OK"))
                {
                    System.out.println("WELCOME " + tokens[2] + " -> you are now logged on!");
                    queue.put("OK");
                }
                else
                {
                    System.out.println(reply.substring(6, reply.length()));
                    System.out.println(" ********************************************************* ");
                    System.out.println("|              1 - Previous menu | 0 - Quit               |");
                    System.out.println(" ********************************************************* ");
                    queue.put("NOT");
                }
                break;
            case "O":
                if(tokens[1].equals("OK"))
                {
                    System.out.println(tokens[2] + " you have successfully logged off!");
                    System.out.println(" ********************************************************* ");
                    System.out.println("|                1 - Main menu | 0 - Quit                 |");
                    System.out.println(" ********************************************************* ");
                    queue.put("OK");                
                }
                break;
            case "S":
                if(tokens[1].equals("OK"))
                {
                    System.out.println(tokens[2] + " you know " + tokens[3] + " cases!!!");
                    System.out.println(" ********************************************************* ");
                    System.out.println("|           Any num - Previous menu | 0 - Quit            |");
                    System.out.println(" ********************************************************* ");
                    queue.put("OK");                
                }
                break;
            case "U":
                if(tokens[1].equals("OK"))
                {
                    queue.put("OK");                
                }
                break;
            case "M":
                if(tokens[1].equals("OK"))
                {
                    System.out.println(reply.substring(5, reply.length()));
                    System.out.println(" ********************************************************* ");
                    System.out.println("|           Any num - Previous menu | 0 - Quit            |");
                    System.out.println(" ********************************************************* ");
                    queue.put("OK");                
                }
                else
                {
                   System.out.println("passei aqui 2"); 
                }
                break;
        }
        }catch(InterruptedException e)
        {
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
            BlockingQueue<String> queue = new ArrayBlockingQueue<>(200);
            Thread tWrite = new Thread(new ClientWriter(cs, queue));
            Thread tRead = new Thread(new ClientReader(cs, queue));
            tWrite.start();
            tRead.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}