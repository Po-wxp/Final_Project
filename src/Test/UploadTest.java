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

        new DatabaseAgent().upload(1,2, urls, questions);
    }
}
