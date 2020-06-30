package PsychologyTest;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class View extends JFrame {
    private Controller controller;
    protected JButton psy, client, add, showList, back, addFiles, save, pre, next;
//    protected switchButton pre, next;
    protected JFileChooser fc;
    protected JLabel identity;
    protected JPanel index;
    protected Color blue;
    protected JPanel psyPanel;
    protected JPanel addTestPanel;
    protected FilePanel filePanel;

    public View(Controller controller) {
        this.controller = controller;
        blue = new Color(110, 110, 234);
        this.setSize(800,600);
        this.setTitle("Psychology Test");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center
        this.setLocationRelativeTo(null);
        // set logo
        ImageIcon logo = new ImageIcon("static/logo.png");
        setIconImage(logo.getImage());

        this.add(index());
    }

    public JPanel index(){
        index = new JPanel(new GridLayout(2,1,0,50));
        index.setBorder(BorderFactory.createEmptyBorder(200,300,200,300));
        index.setBackground(Color.white);

        // Buttons default;
        psy = new JButton("Psychologist");
        client = new JButton("Client");
        buttonDefault(psy,null,new Color(39, 159, 179));
        buttonDefault(client,null,new Color(39, 159, 179));

        index.add(psy);
        index.add(client);

        return index;
    }

    public JPanel psyPanel() {
        psyPanel = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new BorderLayout());
        JPanel leftTop = new JPanel(new BorderLayout());
        JPanel leftMid = new JPanel(new GridLayout(2,1,0,20));
        JPanel leftBot = new JPanel(new BorderLayout());
//        JPanel right = new JPanel();

        Font f1 = new Font("TimesRoman",Font.PLAIN,25);
        Font f2 = new Font("TimesRoman",Font.PLAIN,18);

        //Set left color
        leftTop.setBackground(blue);
        leftMid.setBackground(blue);
        leftBot.setBackground(blue);

        // button default
        add = new JButton("Add Tests");
        showList = new JButton("Show Tests");
        back= new JButton("Return");

        JButton[] btns = new JButton[3];
        btns[0] = add;
        btns[1] = showList;
        btns[2] = back;

        hover(btns);

        add.setActionCommand("unpressed");
        showList.setActionCommand("unpressed");
        back.setActionCommand("unpressed");

        buttonDefault(back,f2,blue);
        buttonDefault(add,f2,Color.white);
        buttonDefault(showList,f2,blue);

        //Left top -- label
        leftTop.setPreferredSize(new Dimension(0,100));
        identity = new JLabel("Psychology",SwingConstants.CENTER);
        identity.setFont(f1);
        leftTop.add(identity);

        //Left bottom -- return button
        leftBot.setPreferredSize(new Dimension(0,100));
        leftBot.setBorder(BorderFactory.createEmptyBorder(0,0,50,0));
        leftBot.add(back);

        //Left mid -- function buttons
        leftMid.add(add);
        leftMid.add(showList);
        leftMid.setBorder(BorderFactory.createEmptyBorder(50,0,200,0));

        //Left
        left.setPreferredSize(new Dimension(180,0));
        left.add(leftTop,BorderLayout.NORTH);
        left.add(leftMid,BorderLayout.CENTER);
        left.add(leftBot,BorderLayout.SOUTH);

        //Right
//        right.setBackground(Color.white);
//        right.add(addTestPanel());

        //Psychology Panel
        psyPanel.add(left,BorderLayout.WEST);
        psyPanel.add(addTestPanel(),BorderLayout.CENTER);

        return psyPanel;
    }

    public JPanel addTestPanel() {
        addTestPanel = new JPanel(null);
        addTestPanel.setBackground(Color.white);

        //Add button
        addFiles = new JButton("Add Files");
        buttonDefault(addFiles,null,new Color(41, 189, 226));
        addFiles.setBounds(30,30,100,30);
        addTestPanel.add(addFiles);

        //File chooser
        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG &MP4","jpg", "png", "mp4");
        fc.setFileFilter(filter);

        // Save Button
        save = new JButton("Save");
        buttonDefault(save, null, new Color(41, 189, 226));
        save.setBounds(480,500,80,30);
        addTestPanel.add(save);

        //File Panel
        filePanel = new FilePanel();
        filePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        filePanel.setBackground(Color.white);
        filePanel.setBounds(100,100,400,220);
        addTestPanel.add(filePanel);

        //Pre, next buttons
        pre = new JButton("<");
        next = new JButton(">");
        buttonDefault(pre,new Font("TimesRoman",Font.PLAIN,18),null);
        buttonDefault(next,new Font("TimesRoman",Font.PLAIN,18),null);
        pre.setContentAreaFilled(false);
        next.setContentAreaFilled(false);
        pre.setBounds(400,320,50,50);
        next.setBounds(460,320,50,50);
        addTestPanel.add(pre);
        addTestPanel.add(next);


        return addTestPanel;
    }

    public void buttonDefault(JButton btn, Font f, Color color){
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(controller);
        if(f!=null){
            btn.setFont(f);
        }
        if(color !=null){
            btn.setBackground(color);
        }
    }

    public void hover(JButton[] btns) {
        for(JButton btn : btns){
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(Color.white);
                }

                public void mouseExited(MouseEvent e) {
                    if(btn.getActionCommand().equals("unpressed")){
                        btn.setBackground(blue);
                    }
                }

                public void mouseClicked(MouseEvent e) {
                    for(JButton b : btns){
                        b.setActionCommand("unpressed");
                        b.setBackground(blue);
                    }
                    btn.setBackground(Color.white);
                    btn.setActionCommand("isPressed");
                }
            });
        }
    }
}

class FilePanel extends JPanel{

        ImageIcon icon;
        Image img;

        public FilePanel() {
            this.setLayout(null);
        }

        public void setImg(String filePath) {
            icon=new ImageIcon(filePath);
            img=icon.getImage();

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img,0, 0,400,220,this);
        }
}

//class switchButton extends JButton{
//
//    public switchButton(String s) {
//        this.setText(s);
//    }
//
//    @Override
//    protected void paintBorder(Graphics g) {
//        g.setColor(getForeground());
//        g.drawOval(0, 0, getSize().width-1,
//                getSize().height-1);
//    }
//}