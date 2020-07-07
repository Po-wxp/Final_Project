package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) {
        // Connect
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/", "postgres", "wxp339762");
            System.out.println("Database is online and available");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }

        try {
            Statement statement = c.createStatement();
            String sqlStr = "Create Table TESTS(TID INT NOT NULL,TPID INT NOT NULL,MEDIAS TEXT, QUESTIONS TEXT, PRIMARY KEY(TID,TPID))";
            statement.executeUpdate(sqlStr);
            System.out.println("Create table successfully");
            statement.close();
            c.close();
        } catch (Exception e) {
            System.out.println("The table has existed");
            e.printStackTrace();
        }
    }


}
