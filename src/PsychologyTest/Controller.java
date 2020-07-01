package PsychologyTest;


import MediaPlayer.MediaPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;


public class Controller implements ActionListener{
    private View view;
    private Model model;
    private ArrayList<File> filesList;
    private int index = 0;

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
//               view.imgPanel.setImg(files[0].getAbsolutePath());
//               view.imgPanel.repaint();
//                view.pre.setVisible(true);
                view.next.setVisible(true);
               for(File f : files){
                   filesList.add(f);
               }
               showPanel();
            }
        }

        if (e.getSource() == view.save) {
            model.copyFile(filesList);
            initialize();
        }

        if(e.getSource() == view.pre){
            index--;
            showPanel();
            if(index < filesList.size() -1){
                view.next.setVisible(true);
            }
        }

        if(e.getSource() == view.next){
            index++;
            showPanel();
        }

        if(e.getSource() == view.back){
            initialize();
        }

        if(index == 0 ){
            view.pre.setVisible(false);
        }else{
            view.pre.setVisible(true);
        }

        if(index == filesList.size() -1 ){
            view.next.setVisible(false);
        }
    }

    public void showPanel(){
        if(model.getFileType(filesList.get(index)).equals("png") || model.getFileType(filesList.get(index)).equals("jpg")){
            view.imgPanel.setVisible(true);
            view.mp.setVisible(false);
            view.imgPanel.setImg(filesList.get(index).getAbsolutePath());
            view.imgPanel.revalidate();
            view.imgPanel.repaint();
        }else {
            view.imgPanel.setVisible(false);
            view.mp.setVisible(true);
            view.mp.setFile(filesList.get(index));
            view.mp.revalidate();
            view.mp.repaint();
        }
        view.showURL.setText(filesList.get(index).getAbsolutePath());
        view.uploadNum.setText("Total: "+filesList.size()+" items.    "+"Current: "+(index+1));
    }

    public void initialize(){
        index = 0;
        filesList = new ArrayList<>();
        view.showURL.setText("");
        view.uploadNum.setText("");
    }


}