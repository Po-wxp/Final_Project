package PsychologyTest;

import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;


public class Controller implements ActionListener{
    private View view;
    private Model model;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
        view.setVisible(true);
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
                System.out.println("You chose to open this file: " +
                        view.fc.getSelectedFile().getName());
            }
        }

//        hover(view.back);
//        hover(view.showList);
//        hover(btns);
    }




//        boolean isPress = false;
//        System.out.println(false);
//        ButtonModel model = (ButtonModel) e.getSource();
//        if (!isPress) {
//            if (model.isPressed()) {
//                System.out.println(1);
//                isPress = true;
//                btn.setBackground(Color.white);
//            }else if (model.isRollover()) {
//                btn.setBackground(Color.white);
//            }else {
//                btn.setBackground(view.blue);
//            }
//        }
//    }
//    @Override
//    public void stateChanged(ChangeEvent e) {
////        hover(view.back,e);
////        hover(view.showList,e);
//        hover(view.add,e);
//    }
}