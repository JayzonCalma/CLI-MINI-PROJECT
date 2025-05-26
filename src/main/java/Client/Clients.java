package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter; 
import java.net.Socket;
import java.util.Scanner;

public class Clients {
    private static final String SERVER = "localhost"; // hostname or IP address  pwd palitan
    private static final int PORT = 12345;  // related siya sa server side pwd palitan  dapat match sila ni server

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER, PORT);  //Creates a new socket and connects this client to the server
            BufferedReader in = new BufferedReader(     // para sa  input  (scanner) na mgagamit ni user
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // paras sa output sout na magagamit ni user
            Scanner scanner = new Scanner(System.in);
        ) {
            Thread readerThread = new Thread(() -> {  // reading ung data ng server hanbang ginagamit ni client or user
                try {
                    int ch;
                    StringBuilder lineBuffer = new StringBuilder(); // madetec si new line ito 
                    while ((ch = in.read()) != -1) {  // ito ung user input tapos convert sa char pag pumasok na sa socket to thread
                        char c = (char) ch;             // para continue ung communictaion ni client at server para mabilis rin   
                        System.out.print(c);
                        if (c == '\n' || c == '\r') {
                            lineBuffer.setLength(0);  // reset buffer on newline
                        } else {
                            lineBuffer.append(c);
                        }
                        // When prompt "Enter choice: " appears, do nothing extra because server already prints it without newline
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });
            
            readerThread.start();

            while (scanner.hasNextLine()) {        //In the main thread, continuously reads user input lines from the console. para mag allow ng send i user ng info sa server
                String input = scanner.nextLine();
                out.println(input);
            }

        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
        }
    }


}
