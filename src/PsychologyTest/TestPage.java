package PsychologyTest;

import java.io.File;
import java.util.ArrayList;

public class TestPage {
    private ArrayList<File> files;
    private ArrayList<String> questions;

    public TestPage(ArrayList<File> files, ArrayList<String> questions){
        this.files = files;
        this.questions = questions;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

}
