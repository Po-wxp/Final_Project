package PsychologyTest;


import database.DatabaseAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class Controller implements ActionListener, MouseListener {
    private View view;
    private Model model;
    private ArrayList<File> filesList;
    private ArrayList<String> questionList;
    private int index;
    private ArrayList<TestPage> testPageList;
    private int pageNum, testNum;
    private boolean show_isSelected, add_isSelected, is_showDetailed, show2_isSelected,evaluation_isSelection;
    protected boolean is_client;
    private ArrayList<ArrayList<String>> answerList;
    private int mark;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
        filesList = new ArrayList();
        questionList = new ArrayList();
        testPageList = new ArrayList();
        answerList = new ArrayList();
        pageNum = 1;
        show_isSelected = false;
        add_isSelected = true;
        show2_isSelected = true;
        evaluation_isSelection = false;
        is_client = false;
        // If the table is be clicked to show detail
        is_showDetailed = false;
        testNum = 0;
        index = 0;
        mark = 0;
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

        if (e.getSource() == view.addFiles) {
            view.removeMedia.setVisible(true);
            UIManager.put("Panel.background", null);
            int interval = view.fc.showDialog(null, "Add Files");
            if (interval == view.fc.APPROVE_OPTION) {
                view.emptyPanel.setVisible(false);
                File[] files = view.fc.getSelectedFiles();
                view.next.setVisible(true);
                for (File f : files) {
                    if(!filesList.contains(f)) filesList.add(f);
                }
                index = filesList.size() - 1;
                showPanel();
            }
        }

        if (e.getSource() == view.pre) {
            index--;
            if(view.mp.getMediaPlayer().isPlaying()){
                view.mp.getMediaPlayer().pause();
            }
            showPanel();
            if (index < filesList.size() - 1) {
                view.next.setVisible(true);
            }
        }

        if (e.getSource() == view.next) {
            index++;
            if(view.mp.getMediaPlayer().isPlaying()){
                view.mp.getMediaPlayer().pause();
            }
            showPanel();
        }

        if (e.getSource() == view.back) {
            model.switchPanel(view, view.psyPanel, view.index);
            nextPageInitialize();
            initialize();
            show_isSelected = false;
            add_isSelected = true;
        }

        if (e.getSource() == view.back2) {
            model.switchPanel(view, view.clientPanel, view.index);
            initialize();
        }

        if (index == 0) {
            view.pre.setVisible(false);
        } else {
            view.pre.setVisible(true);
        }

        if (index == filesList.size() - 1) {
            view.next.setVisible(false);
        }

        if (e.getSource() == view.addQuestion) {
            model.dialogStyle();
            String question = JOptionPane.showInputDialog("Please input the question: ");
            if ((question != null)) {
                questionList.add(question);
                view.question.append(questionList.lastIndexOf(question) + 1 + ". " + question + "\n");
                view.removeQuestion.setVisible(true);
            }
        }

        if (e.getSource() == view.removeQuestion) {
            Object[] list = new Object[questionList.size()];
            for (int i = 0; i < questionList.size(); i++) {
                list[i] = i + 1;
            }
            model.dialogStyle();
            Object selectedValue = JOptionPane.showInputDialog(null, "Select the index", "Remove a question",
                    JOptionPane.INFORMATION_MESSAGE, null,
                    list, list[0]);
            if (selectedValue != null) {
                questionList.remove((int) selectedValue - 1);
                showQuestions();
                view.question.append("\n");
                if (questionList.size() == 0) {
                    view.removeQuestion.setVisible(false);
                }
            }
        }

        if (e.getSource() == view.nextTest) {
            if(view.mp.getMediaPlayer().isPlaying()){
                view.mp.getMediaPlayer().pause();
            }
            if (view.question.getText().equals("")) {
                model.dialogStyle();
                JOptionPane.showMessageDialog(null, "Please input at least one question", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            pageNum++;
            storeTestPage();
            nextPageInitialize();
            view.addTestPanel.revalidate();
            view.addTestPanel.repaint();
        }

        if (e.getSource() == view.save) {
            if (testPageList.size() == 0 && view.question.getText().equals("")) {
                model.dialogStyle();
                JOptionPane.showMessageDialog(null, "Please input at least one question", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!view.question.getText().equals("")) {
                storeTestPage();
            }
            // Get the publisher name
            model.dialogStyle();
            String name = JOptionPane.showInputDialog("Thank you for uploading, please input your name: ");
            if (name == null || name.equals("")) {
                name = "Anonymous";
            }

            // Get current date
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formatDate = sdf.format(date);

            saveData(name, formatDate);
            nextPageInitialize();
            initialize();
            view.removeMedia.setVisible(false);
//            JOptionPane.showConfirmDialog(null, "Thank you for uploading the test", "Completed", JOptionPane.DEFAULT_OPTION);
        }

        if (e.getSource() == view.showList) {
            if(!is_client){
                if (!show_isSelected) {
                    add_isSelected = false;
                    show_isSelected = true;
                    model.changePanel(view.psyPanel, view.addTestPanel, view.showTestsPanel());
                    nextPageInitialize();
                    initialize();
                }
            }else{
                if(!show2_isSelected){
                    evaluation_isSelection = false;
                    show2_isSelected = true;
                    model.changePanel(view.clientPanel, view.evaluationPanel, view.showTestsPanel());
                }

            }

        }

        if (e.getSource() == view.add) {
            if (!add_isSelected) {
                add_isSelected = true;
                show_isSelected = false;
                nextPageInitialize();
                initialize();
                if (!is_showDetailed) {
                    model.changePanel(view.psyPanel, view.showTestsPanel, view.addTestPanel());
                } else {
                    model.changePanel(view.psyPanel, view.testDetailPanel, view.showTestsPanel());
                    model.changePanel(view.psyPanel, view.showTestsPanel, view.addTestPanel());
                    is_showDetailed = false;
                }
            }
        }

        if (e.getSource() == view.backToList) {
            if (!is_client) {
                model.changePanel(view.psyPanel, view.testDetailPanel, view.showTestsPanel());
            } else {
                model.changePanel(view.clientPanel, view.doTestPanel, view.showTestsPanel());
            }
            is_showDetailed = false;
            view.totalBtnGroup = new ArrayList<>();
            filesList = new ArrayList();
            view.fileList = new ArrayList();
            answerList = new ArrayList();
            pageNum = 1;
            index = 0;
            if(view.mp.getMediaPlayer().isPlaying()){
                view.mp.getMediaPlayer().pause();
            }
        }

        if (e.getSource() == view.showNextPage) {
            pageNum++;
            index = 0;
            if (is_client) {
                ArrayList<String> pageAnswer = new ArrayList();
                for (int i = 0; i < view.totalBtnGroup.get(pageNum - 2).size(); i++) {
                    try{ // Answer all questions
                        pageAnswer.add(view.totalBtnGroup.get(pageNum - 2).get(i).getSelection().getActionCommand());
                    }catch (NullPointerException event){
                        model.dialogStyle();
                        JOptionPane.showMessageDialog(null, "Please answer all questions.");
                        pageNum--;
                        return;
                    }
                }
                answerList.add(pageAnswer);
                showDetail(view.clientPanel, view.doTestPanel, view.doTestPanel(testNum, pageNum));
            } else {
                showDetail(view.psyPanel, view.testDetailPanel, view.testDetailPanel(testNum, pageNum));
            }
        }

        if (e.getSource() == view.finish) {
            ArrayList<String> pageAnswer = new ArrayList();
            for (int i = 0; i < view.totalBtnGroup.get(pageNum - 1).size(); i++) {
                try{ // Answer all questions
                    pageAnswer.add(view.totalBtnGroup.get(pageNum - 1).get(i).getSelection().getActionCommand());
                }catch (NullPointerException event){
                    model.dialogStyle();
                    JOptionPane.showMessageDialog(null, "Please answer all questions.");
                    return;
                }
            }
            answerList.add(pageAnswer);
        }

        if (e.getSource() == view.submit) {
            DatabaseAgent database = new DatabaseAgent();
            database.connect();
            String answer = database.getTestDetail(testNum, "ANSWER");
            String star = database.getTestDetail(testNum, "STARS");
            String information = database.getTestDetail(testNum, "USER_INFORMATION");

            String gender = (String)view.genderComboBox.getSelectedItem();
            String age = (String)view.ageComboBox.getSelectedItem();
            String nationality = (String)view.nationality.getText();
            String evaluation = (String)view.evaluation.getText();
            String temp = gender + "/" + age + "/" + nationality + "/" + evaluation;

            if(!information.equals("")){
                information = information + ";" + temp;
            }else{
                information = temp;
            }

            if(!answer.equals("")){
                answer += ";";
            }

            if(!star.equals("")){
                star += ";";
            }
            star += mark+"";

            for (int i = 0; i < answerList.size(); i++) {
                for(String s : answerList.get(i)){
                    answer += s;
                }
            }
            database.uploadAnswer(testNum, answer, star, information);
            database.close();
            model.changePanel(view.clientPanel, view.thanksPanel, view.showTestsPanel());
            initialize();
            is_client = true;
        }

        if (e.getSource() == view.showPrePage) {
            pageNum--;
            index = 0;
            if (is_client) {
                // Remove btn group
                view.totalBtnGroup.remove(view.totalBtnGroup.size() - 1);
                view.totalBtnGroup.remove(view.totalBtnGroup.size() - 1);
                answerList.remove(answerList.size() - 1);

                showDetail(view.clientPanel, view.doTestPanel, view.doTestPanel(testNum, pageNum));
            } else {
                showDetail(view.psyPanel, view.testDetailPanel, view.testDetailPanel(testNum, pageNum));
            }
        }

        if(e.getSource() == view.removeMedia){
            model.dialogStyle();
            int select = JOptionPane.showConfirmDialog(null,"Are you sure to delete the current file?","warning",JOptionPane.YES_NO_OPTION);
            if(select == JOptionPane.YES_OPTION){
                filesList.remove(index);
                if(index > 0){
                    index --;
                }
                if(index == 0) view.pre.setVisible(false);
                if(index == filesList.size()-1) view.next.setVisible(false);
                showPanel();
            }
        }

        if(e.getSource() == view.edit){
            view.edit.setVisible(false);
            view.saveModifyBtn.setVisible(true);
            view.cancelModifyBtn.setVisible(true);
            view.question.setEditable(true);
            view.question.setBorder(BorderFactory.createLineBorder(Color.black));
            if(filesList.size() != 0){
                view.removeMedia.setVisible(true);
                view.addFiles.setVisible(true);
            }
        }

        if(e.getSource() == view.cancelModifyBtn) {
            filesList = new ArrayList();
            view.fileList = new ArrayList();
            index = 0;
            showDetail(view.psyPanel, view.testDetailPanel,view.testDetailPanel(testNum,pageNum));
        }

        if(e.getSource() == view.saveModifyBtn) {
            model.dialogStyle();
            int select = JOptionPane.showConfirmDialog(null,"Are you sure to save the modification?","Ensure",JOptionPane.YES_NO_OPTION);
            if(select == JOptionPane.YES_OPTION) {
                // Update database
                String medias = "";
                ArrayList<File> newFiles = model.copyFile(filesList);
                if(newFiles.size() != 0){
                    for (int i = 0; i < newFiles.size(); i++) {
                        medias += newFiles.get(i).getAbsolutePath();
                        if(i != newFiles.size()-1){
                            medias += ";";
                        }
                    }
                }
                String[] questionsArr= view.question.getText().split("\\r?\\n");
                String questions = "";
                for (int i = 0; i < questionsArr.length; i++) {
                    questions += questionsArr[i].substring(questionsArr[i].indexOf(' ')+1);
                    if(i != questionsArr.length - 1){
                        questions += ";";
                    }
                }
                DatabaseAgent database = new DatabaseAgent();
                database.connect();
                database.updateTestsTable(testNum, pageNum, medias, questions);
                database.close();
                filesList = new ArrayList();
                view.fileList = new ArrayList();
                index = 0;
                showDetail(view.psyPanel, view.testDetailPanel,view.testDetailPanel(testNum,pageNum));
            }
        }

        if (e.getSource() == view.finish) {
            model.changePanel(view.clientPanel, view.doTestPanel, view.thanksPanel());
        }

        if(e.getSource() == view.evaluationBtn){
            if (!evaluation_isSelection){
                evaluation_isSelection = true;
                show2_isSelected = false;
                if(is_showDetailed){
                    is_showDetailed = false;
                    view.backToList.doClick();
                    model.changePanel(view.clientPanel, view.doTestPanel, view.evaluationPanel());
                }else{
                    model.changePanel(view.clientPanel, view.showTestsPanel, view.evaluationPanel());
                }
            }
        }

        if(e.getSource() == view.submitEvaluationBtn){
            String evaluation = view.evaluation.getText();
            if(evaluation.equals("")){
                model.dialogStyle();
                JOptionPane.showMessageDialog(null, "Please write something", "Error", JOptionPane.ERROR_MESSAGE);
            }else{
                DatabaseAgent database = new DatabaseAgent();
                database.connect();
                database.system_evaluation(evaluation);
                model.dialogStyle();
                int input = JOptionPane.showConfirmDialog(null,
                        "Successful evaluation", "Thnks!", JOptionPane.DEFAULT_OPTION);
                if(input == 0 || input == -1){
                    view.evaluation.setText("");
                }
            }
        }

        for (int i = 0; i < view.stars.length; i++) {
            if (e.getSource() == view.stars[i]) {
                mark = i + 1;
                for (int k = i + 1; k < view.stars.length; k++) {
                    view.stars[k].setSelected(false);
                }
                for (int j = 0; j <= i; j++) {
                    view.stars[j].setSelected(true);
                }
            }
        }
    }

    public void saveData(String name, String date) {
        DatabaseAgent database = new DatabaseAgent();
        database.connect();
        int TID = database.getMaxTID() + 1;
        for (int i = 1; i <= testPageList.size(); i++) {
            ArrayList<String> questions = new ArrayList();
            ArrayList<String> urls = new ArrayList();
            ArrayList<File> newFiles = model.copyFile(testPageList.get(i-1).getFiles());
            for (String question : testPageList.get(i - 1).getQuestions()) {
                questions.add(question);
            }

            for (File f : newFiles) {
                urls.add(f.getAbsolutePath());
            }
            database.upload(TID, i, urls, questions, name, date, i);
        }
        database.close();
    }

    public void showPanel() {
        if (filesList.size() != 0) {
            if (model.getFileType(filesList.get(index)).equals("png") || model.getFileType(filesList.get(index)).equals("jpg")) {
                view.imgPanel.setVisible(true);
                view.mp.setVisible(false);
                view.imgPanel.setImg(filesList.get(index).getAbsolutePath());
                view.imgPanel.revalidate();
                view.imgPanel.repaint();
            } else {
                view.imgPanel.setVisible(false);
                view.mp.setVisible(true);
                view.mp.setFile(filesList.get(index));
                view.mp.revalidate();
                view.mp.repaint();
            }
            view.showURL.setText(filesList.get(index).getAbsolutePath());
            view.uploadNum.setText("Total: " + filesList.size() + " items.    " + "Current: " + (index + 1));
        }else{
            view.removeMedia.setVisible(false);
            view.imgPanel.setVisible(false);
            view.mp.setVisible(false);
            view.emptyPanel.setVisible(true);
            view.showURL.setVisible(false);
            view.uploadNum.setVisible(false);
        }
    }

    public void showQuestions() {
        String output = "";
        for (int i = 0; i < questionList.size(); i++) {
            output += (i + 1) + ". " + questionList.get(i);
            if (i != questionList.size() - 1) {
                output += "\n";
            }
        }
        view.question.setText(output);
    }

    public void storeTestPage() {
        TestPage page = new TestPage(filesList, questionList);
        testPageList.add(page);
    }

    public void nextPageInitialize() {
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
        if(view.mp.getMediaPlayer().isPlaying()){
            view.mp.getMediaPlayer().pause();
        }
    }

    public void initialize() {
        pageNum = 1;
        testPageList = new ArrayList();
        is_client = false;
        answerList = new ArrayList();
        view.totalBtnGroup = new ArrayList();
        mark = 0;

        show2_isSelected = true;
        evaluation_isSelection = false;
    }

    public void showDetail(JPanel p1, JPanel p2, JPanel p3) {
        is_showDetailed = true;
        model.changePanel(p1, p2, p3);
        filesList = view.fileList;
        view.fileList = new ArrayList();
        if (filesList.size() != 0) {
            showPanel();
        }
        if (filesList.size() > 1) {
            view.next.setVisible(true);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        testNum = view.testsTable.getSelectedRow() + 1;
        if (is_client) {
            showDetail(view.clientPanel, view.showTestsPanel, view.doTestPanel(testNum, 1));
        } else {
            showDetail(view.psyPanel, view.showTestsPanel, view.testDetailPanel(testNum, 1));
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