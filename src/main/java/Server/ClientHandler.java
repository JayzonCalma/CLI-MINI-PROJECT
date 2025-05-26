package Server;

import java.io.BufferedReader;  // parang scanner get user input
import java.io.IOException; //error 
import java.io.InputStreamReader; // help to read the sockes and convert sa charcters
import java.io.PrintWriter; // parang sout
import java.net.Socket; // ito ung parsa sa one client si thread ang may handdle pag multiple user na

import features.Login;
import features.Register;

public class ClientHandler implements Runnable {  // Runnable ay thread  feature na tumutulong kay ClientHandler para mag run mag isa 
    private Socket clientSocket;  // onnected client's socket
    private BufferedReader in;  // clients inputs  (scout) 
    private PrintWriter out;   // clients outputs    (scanner) 

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;  // sumasalo sa clients 
    }

    @Override
    public void run() {   // start na pag ng run si thread  handdel rin ng communication sa clients 

        
        try {
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));      // read data from the client via the socket.
            out = new PrintWriter(clientSocket.getOutputStream(), true);  // to send data to the client. true enables auto-flushing (sends data immediately).
            
             Register register = new Register(in, out); // (in out) para mapasa next page in input at output
             Login login = new Login(in, out);

            while (true) {
                
                out.println("\n+--------------------------+");
                out.println("|         MAIN MENU        |");
                out.println("+--------------------------+");
                out.println("| 1. Register              |");
                out.println("| 2. Login                 |");
                out.println("| 3. Reset Password        |");
                out.println("| 0. Exit                  |");
                out.print("+--------------------------+\n ");
                out.print("\nEnter choice: ");
                out.flush();
                out.print("\n");

                String input = in.readLine();
                if (input == null) break; // client disconnect

                int choice;
                try {
                    choice = Integer.parseInt(input.trim()); // trim whitespace just in case
                } catch (NumberFormatException e) {

                    out.println("+-------------------------------+");
                    out.println("|  Invalid input.               |");
                    out.println("|  Please enter a number.       |");
                    out.println("+-------------------------------+");
                    continue;
                }

                switch (choice) {
                    case 1:
                        register.registerUser();
                        break;
                    case 2:
                       login.loginUser();
                        break;
                    case 3:
                       
                        break;
                    case 0:
                        out.println("+-----------+");
                        out.println("|  Goodbye! |");
                        out.println("+-----------+");
                        clientSocket.close();
                        return;
                    default:
                        out.println("+-------------------+");
                        out.println("|  Invalid option.  |");
                        out.println("+-------------------+");
                        break;
                }
            }

        } catch (IOException e) {
            System.out.println("+----------------------+");
            System.out.println("|  Client disconnected. |");
            System.out.println("+----------------------+");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }
}
