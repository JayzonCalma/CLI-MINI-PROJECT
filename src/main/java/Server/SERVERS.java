package Server;



import java.io.IOException; // for errrors
import java.net.ServerSocket; // for incoming connection
import java.net.Socket; //for one Client

public class SERVERS {

    public static final int PORT = 12345; // pwede palitan ung port number

    public static void main(String[] args) {
        try( ServerSocket serverSocket = new ServerSocket(PORT) ){
                
                System.out.println("Server started on port " + PORT + " ..."); // print the current PORT

                while (true) {
                    
                    Socket clientSocket = serverSocket.accept(); // pag may client na
                    System.out.println(" New client connected: " + clientSocket.getInetAddress() + " ......"); // ip ni new client

                

                     ClientHandler clientHandler = new ClientHandler(clientSocket);
                     Thread t = new Thread(clientHandler); // handel multiple cllient 
                     t.start();
                }
        } catch (IOException e){
           e.printStackTrace(); // pag may error
        }


    }

}