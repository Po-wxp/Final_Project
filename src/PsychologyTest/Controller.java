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
    private ArrayList<String> questionList;
    private int index = 0;
    private ArrayList<TestPage> testPageList;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
        filesList = new ArrayList();
        questionList = new ArrayList();
        testPageList = new ArrayList();
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
                view.next.setVisible(true);
               for(File f : files){
                   filesList.add(f);
               }
               showPanel();
            }
        }

        if (e.getSource() == view.save) {

//            model.copyFile(filesList);
            initialize();
            testPageList = new ArrayList();
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
            testPageList = new ArrayList();
        }

        if(index == 0 ){
            view.pre.setVisible(false);
        }else{
            view.pre.setVisible(true);
        }

        if(index == filesList.size() -1 ){
            view.next.setVisible(false);
        }

        if(e.getSource() == view.addQuestion){
            String question= JOptionPane.showInputDialog("Please input the question: ");
            if((question != null)){
                questionList.add(question);
                view.question.append(questionList.lastIndexOf(question)+1 +". "+question+"\n");
                view.removeQuestion.setVisible(true);
            }
        }

        if(e.getSource() == view.removeQuestion){
            Object[] list = new Object[questionList.size()];
            for (int i = 0; i < questionList.size(); i++) {
                list[i] = i+1;
            }
            Object selectedValue = JOptionPane.showInputDialog(null, "Select the index", "Remove a question",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    list, list[0]);
            questionList.remove((int) selectedValue - 1);
            showQuestions();
            if(questionList.size() == 0){
                view.removeQuestion.setVisible(false);
            }
        }

        if(e.getSource() == view.nextTest){
            storeTestPage();
            initialize();
            view.addTestPanel.revalidate();
            view.addTestPanel.repaint();
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

    public void showQuestions(){
        String output = "";
        for (int i = 0; i < questionList.size(); i++) {
            output += (i+1)+". "+questionList.get(i);
            if(i != questionList.size()-1){
                output += "\n";
            }
        }
        view.question.setText(output);
    }

    public void storeTestPage() {
        TestPage page = new TestPage(filesList, questionList);
        testPageList.add(page);
//        System.out.println(page.getFiles());
//        System.out.println(page.getQuestions());
    }

    public void initialize(){
        index = 0;
        filesList = new ArrayList<>();
        questionList = new ArrayList<>();
        view.showURL.setText("");
        view.uploadNum.setText("");
        view.question.setText("");
        view.imgPanel.setVisible(false);
        view.mp.setVisible(false);
    }




}