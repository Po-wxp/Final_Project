package PsychologyTest;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class Controller implements ActionListener{
    private View view;
    private Model model;
    private ArrayList<File> filesList;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
        filesList = new ArrayList();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.psy) {
            model.switchPanel(view, view.index, view.psyPanel());
        }
        if (e.getSource() == view.add) {
//            view.add.setBackground(Color.white);
        }
        if (e.getSource() == view.back) {
            model.switchPanel(view, view.psyPanel, view.index);
        }

        if (e.getSource() == view.addFiles){
            int interval = view.fc.showDialog(view,"Add Files");
            if(interval == view.fc.APPROVE_OPTION){
               File[] files = view.fc.getSelectedFiles();
               view.filePanel.setImg(files[0].getAbsolutePath());
               view.filePanel.repaint();
               for(File f : files){
                   filesList.add(f);
               }
            }
        }

        if (e.getSource() == view.save) {
            model.copyFile(filesList);
        }

        if(e.getSource() == view.pre){
//            view.identity.setText("ddd");
        }
    }


}