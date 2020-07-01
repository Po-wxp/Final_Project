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
    // Copy upload files to the server
    public void copyFile(ArrayList<File> filesList){
        for(File f : filesList){
            File newFile = new File("upload/",f.getName());
//                f.renameTo(newFile);
            try {
                Files.copy(f.toPath(),newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException event) {
                event.printStackTrace();
            }
        }
    }

    public String getFileType(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".",fileName.length())).substring(1);
    }
}
