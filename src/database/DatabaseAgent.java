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
            String sqlStr = "CREATE TABLE TESTS(TID INT NOT NULL,TPID INT NOT NULL,MEDIAS TEXT, QUESTIONS TEXT NOT NULL)";
            statement.executeUpdate(sqlStr);

            sqlStr = "CREATE TABLE TEST_DETAIL(TID INT NOT NULL,PUBLISHER VARCHAR(20), DATE VARCHAR(20), ANSWER TEXT, STARS TEXT, PRIMARY KEY(TID))";
            statement.executeUpdate(sqlStr);
            System.out.println("Create table successfully");
            statement.close();
        } catch (Exception e) {
            System.out.println("The table has existed");
            e.printStackTrace();
        }
    }

    public void upload(int TID, int TPID, ArrayList<String> mediaUrls, ArrayList<String> questions,String name, String date,int time) {
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

            if(time == 1){
                statement = c.prepareStatement("INSERT INTO TEST_DETAIL VALUES (?,?,?,?,?)");
                statement.setInt(1, TID);
                statement.setString(2, name);
                statement.setString(3, date);
                statement.setString(4, "");
                statement.setString(5, "");
                statement.executeUpdate();
            }
            System.out.println("Insert data successfully");
            statement.close();
//            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTestsTable(int TID, int TPID, String media, String questions){
        try {
            PreparedStatement statement = c.prepareStatement("UPDATE TESTS SET MEDIAS=?, QUESTIONS=? WHERE TID=? AND TPID=?");
            statement.setString(1, media);
            statement.setString(2, questions);
            statement.setInt(3, TID);
            statement.setInt(4, TPID);
            statement.executeUpdate();

            System.out.println("Update data successfully");
            statement.close();
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
                String media = result.getString("M");
                if (!media.equals("")){
                    String[] output = media.split(";");
                    for(String s : output) filesUrl.add(s);
                }
            }
            System.out.println("Get attr successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filesUrl;
    }

    public String getTestDetail(int TID, String attr) {
        String output = "";
        try {
            PreparedStatement statement = c.prepareStatement("SELECT "+ attr +" AS M FROM TEST_DETAIL WHERE TID=?");
            statement.setInt(1, TID);
            result = statement.executeQuery();
            while(result.next()){
                output = result.getString("M");
            }
            System.out.println("Get detail successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void uploadAnswer(int TID, String answer, String mark){
        try {
            PreparedStatement statement = c.prepareStatement("UPDATE TEST_DETAIL SET ANSWER=?, STARS=? WHERE TID=?");
            statement.setString(1, answer);
            statement.setString(2, mark);
            statement.setInt(3, TID);
            statement.executeUpdate();
            System.out.println("update data successfully");
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
