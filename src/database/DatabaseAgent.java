package database;

import PsychologyTest.PsychologyTest;
import jdk.swing.interop.SwingInterOpUtils;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseAgent {
    Connection c = null;

    public DatabaseAgent() {
        connect();
    }

    public void close() {
        try {
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createTable() {
        try {
            Statement statement = c.createStatement();
            String sqlStr = "Create Table TESTS(TID INT NOT NULL,TPID INT NOT NULL,MEDIAS TEXT, QUESTIONS TEXT, PRIMARY KEY(TID,TPID))";
            statement.executeUpdate(sqlStr);
            System.out.println("Create table successfully");
            statement.close();
//            c.close();
        } catch (Exception e) {
            System.out.println("The table has existed");
            e.printStackTrace();
        }
    }

    public void upload(int TID, int TPID, ArrayList<String> mediaUrls, ArrayList<String> questions) {
        String urls = "";
        String question = "";
        for (int i = 0; i < mediaUrls.size(); i++) {
            urls += mediaUrls.get(i);
            if (i != mediaUrls.size() - 1){
                urls += ";";
            }
        }

        for (int i = 0; i < questions.size(); i++) {
            question += questions.get(i);
            if (i != questions.size() - 1){
                question += ";";
            }
        }

        try {
            PreparedStatement statement = c.prepareStatement("INSERT INTO TESTS VALUES (?,?,?,?)");
            statement.setInt(1, TID);
            statement.setInt(2, TPID);
            statement.setString(3, urls);
            statement.setString(4, question);
            statement.executeUpdate();
            System.out.println("Insert data successfully");
            statement.close();
//            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxTID() {
        int max = 0;
        try {
            Statement statement = c.createStatement();
            String sqlStr = "SELECT MAX(TID) AS TID FROM TESTS";
            ResultSet result = statement.executeQuery(sqlStr);
            while(result.next()){
                max = result.getInt("TID");
            }
            System.out.println("Get max TID successfully");
            statement.close();
//            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }

    public ArrayList<String> getAttr(int TID, String attribute) {
        ArrayList<String> mediaList = new ArrayList();
        try {
            PreparedStatement statement = c.prepareStatement("SELECT "+ attribute +" AS M FROM TESTS WHERE TID=?");
            statement.setInt(1, TID);
            ResultSet result = statement.executeQuery();
            while(result.next()){
                String[] output = result.getString("M").split(";");
                for(String s : output) mediaList.add(s);
            }
            System.out.println("Get attr successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaList;
    }

    public void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/", "postgres", "wxp339762");
            System.out.println("Database is online and available");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

}
