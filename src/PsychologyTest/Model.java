package PsychologyTest;

import MediaPlayer.MediaPlayer;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class Model {
    public void switchPanel(JFrame jf, JPanel cur, JPanel next){
        jf.remove(cur);
        jf.add(next);
        jf.revalidate();
        jf.repaint();
    }

    public void changePanel(JPanel jf, JPanel cur, JPanel next){
        jf.remove(cur);
        jf.add(next);
        jf.revalidate();
        jf.repaint();
    }

    // Copy upload files to the server
    public ArrayList<File>  copyFile(ArrayList<TestPage> testPages){
        ArrayList<File> newFiles = new ArrayList();
        for (TestPage testPage : testPages){
            for(File f : testPage.getFiles()){
                File newFile = new File("upload/",f.getName());
                try {
                    Files.copy(f.toPath(),newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    newFiles.add(newFile);
                } catch (IOException event) {
                    event.printStackTrace();
                }
            }
        }
        return newFiles;
    }

    public String getFileType(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".",fileName.length())).substring(1);
    }
}
