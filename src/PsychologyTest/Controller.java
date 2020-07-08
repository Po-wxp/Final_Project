package PsychologyTest;


import database.DatabaseAgent;

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
    private int pageNum;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
        filesList = new ArrayList();
        questionList = new ArrayList();
        testPageList = new ArrayList();
        pageNum = 1;
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
            nextPageInitialize();
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
            if(view.question.getText().equals("")){
                JOptionPane.showMessageDialog(null, "Please input at least one question", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            pageNum ++;
            storeTestPage();
            nextPageInitialize();
            view.addTestPanel.revalidate();
            view.addTestPanel.repaint();
        }

        if (e.getSource() == view.save) {
            ArrayList<File> newFiles = model.copyFile(testPageList);
            saveData(newFiles);
            nextPageInitialize();
            initialize();
        }
    }

    public void saveData(ArrayList<File> newFiles) {
        ArrayList<String> questions = new ArrayList();
        ArrayList<String> urls = new ArrayList();
        for(TestPage page : testPageList){
            for(String question : page.getQuestions()){
                questions.add(question);
            }
        }

        for(File f : newFiles) {
            urls.add(f.getAbsolutePath());
        }

        DatabaseAgent database = new DatabaseAgent();
        int TID = database.getMaxTID();
        database.upload(TID, pageNum, urls, questions);
        database.close();
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

    public void nextPageInitialize(){
        index = 0;
        filesList = new ArrayList<>();
        questionList = new ArrayList<>();
        view.showURL.setText("");
        view.uploadNum.setText("");
        view.question.setText("");
        view.imgPanel.setVisible(false);
        view.mp.setVisible(false);
        view.next.setVisible(false);
        view.pre.setVisible(false);
    }

    public void initialize() {
        pageNum = 1;
        testPageList = new ArrayList();
    }

}