import java.io.*; 
import java.util.*; 
import java.net.*; 

class ClientWriter implements Runnable{
    
    private final Socket cs;
    private PrintWriter out;
    private BufferedReader keyboard;
    
    public ClientWriter(Socket cs) throws IOException {
        this.cs = cs;
        this.out = new PrintWriter( cs.getOutputStream(), true);
        this.keyboard = new BufferedReader( new InputStreamReader( System.in ));
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
                //System.exit(0);
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
                case UPDATE:
                    menu = main_menu();
                    break;
                case SHOW:
                    menu = main_menu();
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
        boolean login = false;
        String user = null, pass = null;
        while(!login)
        {
            System.out.print("Type Your Name: ");  
            System.out.print("> ");
            user = keyboard.readLine();
            System.out.print("Type Your Password: ");  
            System.out.print("> ");
            pass = keyboard.readLine();
            out.println("Login " + user + " " + pass);    
            login = true;
        }
        int escolha = read_choice();
        switch (escolha)
        {
            case 0:
                return Menu.QUIT;
            case 1:
                return Menu.MAIN;
            default:
                return Menu.LOGIN;
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