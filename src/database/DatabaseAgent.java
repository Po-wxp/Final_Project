package database;

import PsychologyTest.PsychologyTest;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseAgent {
    private Connection c = null;
    private ResultSet result = null;

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
            result = statement.executeQuery(sqlStr);
            while(result.next()){
                max = result.getInt("TID");
            }
            System.out.println("Get max TID successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }

    public int getMaxPageID(int TID){
        int max = 0;
        try {
            Statement statement = c.createStatement();
            String sqlStr = "SELECT MAX(TPID) AS TPID FROM TESTS WHERE TID="+TID;
            result = statement.executeQuery(sqlStr);
            while(result.next()){
                max = result.getInt("TPID");
            }
            System.out.println("Get max TPID successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return max;
    }

    public ArrayList<String> getAttr(int TID, String attribute) {
        ArrayList<String> attrList = new ArrayList();
        try {
            PreparedStatement statement = c.prepareStatement("SELECT "+ attribute +" AS M FROM TESTS WHERE TID=?");
            statement.setInt(1, TID);
            result = statement.executeQuery();
            while(result.next()){
                String[] output = result.getString("M").split(";");
                for(String s : output) attrList.add(s);
            }
            System.out.println("Get attr successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return attrList;
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

    public ArrayList<String> getPageMediaList(int TID, int TPID, String attr) {
        ArrayList<String> filesUrl = new ArrayList();

        try {
            PreparedStatement statement = c.prepareStatement("SELECT "+ attr +" AS M FROM TESTS WHERE TID=? AND TPID=?");
            statement.setInt(1, TID);
            statement.setInt(2, TPID);
            result = statement.executeQuery();
            while(result.next()){
                String[] output = result.getString("M").split(";");
                for(String s : output) filesUrl.add(s);
            }
            System.out.println("Get attr successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filesUrl;
    }

}
