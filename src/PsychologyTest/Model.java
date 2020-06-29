package PsychologyTest;

import javax.swing.*;

public class Model {
    public void switchPanel(JFrame jf, JPanel cur, JPanel next){
        jf.remove(cur);
        jf.add(next);
        jf.revalidate();
        jf.repaint();
    }
}
