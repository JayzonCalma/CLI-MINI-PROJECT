package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/cli_application_database"; //DB name
            String user = "root"; // Replace with your DB username
            String pass = "Password";     // Replace with your DB password
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();  // print all the error encounter
            return null;
        }
    }
}