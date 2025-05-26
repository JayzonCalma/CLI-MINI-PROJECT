package features;

import java.io.BufferedReader;  // for input
import java.io.IOException;
import java.io.PrintWriter;     // fpr output    
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;

public class Register {
        private BufferedReader in;
        private PrintWriter out;

// BufferedReader  PrintWriter
// for sockets base program para magamit ng maraming users
        public Register(BufferedReader in, PrintWriter out) {
            this.in = in;
            this.out = out;
        }

        public void registerUser() throws IOException {
        String username;
        String password;
        
        out.println("+-----------+");
        out.println("| REGISTER  |");
        out.println("+-----------+");

        // FOR USERNAME
           
        while(true){

            out.print("Enter username (min 4 characters, alphanumeric only, no spaces/special characters): ");
            out.flush(); // use to send data to thee socket
            username = in.readLine(); // acts like scanner
            if (username == null) return; // prevent entering no content
            username = username.trim();   // no white space
            out.print("\n");

            // check if the input is alphanumeric and no special char and space
            if (!username.matches("^[a-zA-Z0-9]{4,}$")) {
                out.println("Invalid username. Try again.");
                continue;
            }

            // check the uniqueness of the username om DB

            try (Connection conn = DBConnection.getConnection()) {
                String checkSql = "SELECT username FROM user WHERE username = ? UNION SELECT username FROM admin WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setString(1, username);
                    checkStmt.setString(2, username);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        out.println("+----------------------------------------------+");
                        out.println("| Username already exists. Try a different one. |");
                        out.println("+----------------------------------------------+");
                    } else {
                        break;
                    }
            
            } catch (Exception e) {
                out.println("+-------------------------+");
                out.println("| Error checking username.|");
                out.println("+-------------------------+");
                e.printStackTrace(out);
                return;
            }
        }

        //Password
        while (true){
            out.print("Enter password (min 8 characters, no spaces): ");
            out.flush();
            password = in.readLine();
            if (password == null) return;
            if (password.matches("^[\\S]{8,}$")) {
                break;
            } else {
                out.println("+------------------------------------+");
                out.println("| Invalid password. Try again.       |");
                out.println("+------------------------------------+");
            }
        }
         out.print("\n");

        //Ask if Admin
        int attempts = 0;
        while (attempts < 3 ){

                out.print("Are you registering as an admin? (y/n): ");
                out.flush(); 
                String response = in.readLine();
                if (response == null) return;
                response = response.trim().toLowerCase();
                out.print("\n");

                if (response.equals("y")) {
                    
                    out.print("Enter Admin PIN: ");
                    out.flush();
                    String enteredPin = in.readLine();
                    if (enteredPin == null) return;

                    //Checking on the DB

                    try (Connection conn = DBConnection.getConnection()) {

                        String pinSql = "SELECT pin FROM pin WHERE pin = ?";
                        PreparedStatement pinStmt = conn.prepareStatement(pinSql);
                        pinStmt.setString(1, enteredPin);
                        ResultSet rs = pinStmt.executeQuery();

                        if ( rs.next()){
                            String insertAdmin = "INSERT INTO admin(username, password) VALUES (?, ?)";  // here na napsok si crentials sa table
                            PreparedStatement insertStmt = conn.prepareStatement(insertAdmin);
                            insertStmt.setString(1, username);
                            insertStmt.setString(2, password);
                            insertStmt.executeUpdate();
                            out.print("\n");
        
                            out.println("+----------------------------------------+");
                            out.println("|   Admin registered successfully!       |");
                            out.println("+----------------------------------------+");
                            return;

                        } else {
                            out.print("\n");
        
                            out.println("+----------------------------------------------------------+");
                            out.println("|  Invalid Admin PIN. Returning to main menu.              |");
                            out.println("+----------------------------------------------------------+");
                            return;
                        }

                    }catch (Exception e) {
                        out.print("\n");
        
                            out.println("+----------------------------------------------+");
                            out.println("|  Error during admin registration.            |");
                            out.println("+----------------------------------------------+");
                        e.printStackTrace(out);
                        return;

                }
// ordinary user
            }else if (response.equals("n")) {
            
                try (Connection conn = DBConnection.getConnection()) {
                      String insertUser = "INSERT INTO user(username, password) VALUES (?, ?)"; // here na napsok si crentials sa table
                            PreparedStatement insertStmt = conn.prepareStatement(insertUser);
                            insertStmt.setString(1, username);
                            insertStmt.setString(2, password);
                            insertStmt.executeUpdate();
                            out.print("\n");
    
                            out.println("+----------------------------------------------+");
                            out.println("|  User registered successfully!               |");
                            out.println("+----------------------------------------------+");
                            return;

                
                }catch (Exception e) {
                     out.print("\n");
    
                        out.println("+----------------------------------------------+");
                        out.println("|  Error during user registration.             |");
                        out.println("+----------------------------------------------+");
                    e.printStackTrace(out);
                    return;
                }

            
            }else {
                 out.print("\n");
    
                    out.println("+----------------------------------------------+");
                    out.println("|  Invalid input. Please type 'y' or 'n'.      |");
                    out.println("+----------------------------------------------+");
                attempts++;
            }
        }
         
    }
}
