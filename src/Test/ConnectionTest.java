package Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class ConnectionTest {

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();
        System.out.println();

        input.close();
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/", username, password);
//            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/", "postgres", "wxp339762");
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Database is online and available");

    }

}
