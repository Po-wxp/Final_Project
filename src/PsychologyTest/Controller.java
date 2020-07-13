package PsychologyTest;


import database.DatabaseAgent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;


public class Controller implements ActionListener, MouseListener {
    private View view;
    private Model model;
    private ArrayList<File> filesList;
    private ArrayList<String> questionList;
    private int index = 0;
    private ArrayList<TestPage> testPageList;
    private int pageNum;
    private boolean show_isSelected, add_isSelected, is_client, is_showDetailed;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
        filesList = new ArrayList();
        questionList = new ArrayList();
        testPageList = new ArrayList();
        pageNum = 1;
        show_isSelected = false;
        add_isSelected = true;
        is_client = false;
        // If the table is be clicked to show detail
        is_showDetailed = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.psy) {
            model.switchPanel(view, view.index, view.psyPanel());
            is_client = false;
        }
        if (e.getSource() == view.client) {
            model.switchPanel(view, view.index, view.clientPanel());
            is_client = true;
        }

        if (e.getSource() == view.addFiles){
            view.emptyPanel.setVisible(false);
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
            model.switchPanel(view, view.psyPanel, view.index);
            nextPageInitialize();
            initialize();
            show_isSelected = false;
            add_isSelected = true;
        }

        if(e.getSource() == view.back2){
            model.switchPanel(view, view.clientPanel, view.index);
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
            if(selectedValue != null){
                questionList.remove((int) selectedValue - 1);
                showQuestions();
                if(questionList.size() == 0){
                    view.removeQuestion.setVisible(false);
                }
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
            if(testPageList.size() == 0){
                JOptionPane.showMessageDialog(null, "Please input at least one question", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ArrayList<File> newFiles = model.copyFile(testPageList);
            saveData(newFiles);
            nextPageInitialize();
            initialize();
        }

        if (e.getSource() == view.showList) {
            if(!show_isSelected){
                add_isSelected = false;
                show_isSelected = true;
                model.changePanel(view.psyPanel, view.addTestPanel, view.showTestsPanel());
                nextPageInitialize();
                initialize();
            }
        }

        if (e.getSource() == view.add) {
            if(!add_isSelected){
                add_isSelected = true;
                show_isSelected = false;
                nextPageInitialize();
                initialize();
                if(!is_showDetailed){
                    model.changePanel(view.psyPanel, view.showTestsPanel, view.addTestPanel());
                }else{
                    model.changePanel(view.psyPanel, view.testDetailPanel, view.showTestsPanel);
                    model.changePanel(view.psyPanel, view.showTestsPanel, view.addTestPanel());
                    is_showDetailed = false;
                }
            }
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
        database.connect();
        int TID = database.getMaxTID() + 1;
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
        view.emptyPanel.setVisible(true);
        view.fileList = new ArrayList();
    }

    public void initialize() {
        pageNum = 1;
        testPageList = new ArrayList();
        is_client = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(is_client){
            model.changePanel(view.clientPanel, view.showTestsPanel, view.doTestPanel(view.testsTable.getSelectedRow()+1));
        }else{
            is_showDetailed = true;
            model.changePanel(view.psyPanel, view.showTestsPanel, view.testDetailPanel(view.testsTable.getSelectedRow()+1,1));
            filesList = view.fileList;
            showPanel();
            if(filesList.size() > 1){
                view.next.setVisible(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}