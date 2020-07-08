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

        new DatabaseAgent().upload(1,1, urls, questions);
        new DatabaseAgent().upload(1,2, urls, questions);

        urls.remove(0);
        new DatabaseAgent().upload(2,1, urls, questions);
        new DatabaseAgent().upload(2,2, urls, questions);

        questions.remove(1);
        new DatabaseAgent().upload(3,1, urls, questions);
        new DatabaseAgent().upload(4,1, urls, questions);
    }
}
