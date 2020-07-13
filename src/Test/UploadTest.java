package Test;

import database.DatabaseAgent;

import java.util.ArrayList;

public class UploadTest {
    public static void main(String[] args) {
        ArrayList<String> urls = new ArrayList();
        urls.add("c://");
        urls.add("d://");

        ArrayList<String> questions = new ArrayList();
        questions.add("hhh");
        questions.add("Test");

        DatabaseAgent database = new DatabaseAgent();
        database.connect();
        database.upload(1,1, urls, questions);
        database.upload(1,2, urls, questions);

        urls.remove(0);
        database.upload(2,1, urls, questions);
        database.upload(2,2, urls, questions);

        questions.remove(1);
        database.upload(3,1, urls, questions);
        database.upload(4,1, urls, questions);
        database.close();
    }
}
