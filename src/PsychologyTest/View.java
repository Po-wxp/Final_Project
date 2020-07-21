package PsychologyTest;

import MediaPlayer.MediaPlayer;
import ScrollBar.Bar;
import com.sun.jna.NativeLibrary;
import database.DatabaseAgent;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;

public class View extends JFrame {
    private Controller controller;
    protected JButton psy, client, add, showList, back, back2, addFiles, save, pre, next, nextTest,
            addQuestion, removeQuestion, edit, showNextPage, showPrePage,backToList, finish, submit, removeMedia;
    protected JTextArea question;
    protected JFileChooser fc;
    protected JLabel identity, showURL, uploadNum;
    protected JPanel index;
    protected Color blue;
    protected JPanel psyPanel, addTestPanel, showTestsPanel, emptyPanel, clientPanel, doTestPanel, testDetailPanel, thanksPanel;
    protected imgPanel imgPanel;
    public MediaPlayer mp;
    protected JTable testsTable;
    protected ArrayList<File> fileList;
    private DatabaseAgent database;
    private Font f1, f2;
    private JScrollPane TextJp;
    protected ArrayList<String> questionsList, lists;
    protected JToggleButton[] stars;
    protected ArrayList<ButtonGroup> pageBtnGroup;
    protected ArrayList<ArrayList<ButtonGroup>> totalBtnGroup;

    public View(Controller controller) {
        initialize();
        this.controller = controller;
        blue = new Color(110, 110, 234);
        this.setSize(800, 600);
        this.setTitle("Psychology Test");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Center
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        // set logo
        ImageIcon logo = new ImageIcon("static/logo.png");
        setIconImage(logo.getImage());

        this.add(index());
    }

    public JPanel index() {
        index = new JPanel(new GridLayout(2, 1, 0, 50));
        index.setBorder(BorderFactory.createEmptyBorder(200, 300, 200, 300));
        index.setBackground(Color.white);

        // Buttons default;
        psy = new JButton("Psychologist");
        client = new JButton("Client");
        buttonDefault(psy, null, new Color(39, 159, 179));
        buttonDefault(client, null, new Color(39, 159, 179));

        index.add(psy);
        index.add(client);

        return index;
    }

    public JPanel psyPanel() {
        psyPanel = new JPanel(new BorderLayout());
        JPanel left = new JPanel(new BorderLayout());
        JPanel leftTop = new JPanel(new BorderLayout());
        JPanel leftMid = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel leftBot = new JPanel(new BorderLayout());

        //Set left color
        leftTop.setBackground(blue);
        leftMid.setBackground(blue);
        leftBot.setBackground(blue);

        // button default
        add = new JButton("Add Tests");
        showList = new JButton("Show Tests");
        back = new JButton("Return");

        JButton[] btns = new JButton[3];
        btns[0] = add;
        btns[1] = showList;
        btns[2] = back;

        hover(btns);

        add.setActionCommand("isPressed");
        showList.setActionCommand("unpressed");
        back.setActionCommand("unpressed");

        buttonDefault(back, f2, blue);
        buttonDefault(add, f2, Color.white);
        buttonDefault(showList, f2, blue);

        //Left top -- label
        leftTop.setPreferredSize(new Dimension(0, 100));
        identity = new JLabel("Psychology", SwingConstants.CENTER);
        identity.setFont(f1);
        leftTop.add(identity);

        //Left bottom -- return button
        leftBot.setPreferredSize(new Dimension(0, 100));
        leftBot.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        leftBot.add(back);

        //Left mid -- function buttons
        leftMid.add(add);
        leftMid.add(showList);
        leftMid.setBorder(BorderFactory.createEmptyBorder(50, 0, 200, 0));

        //Left
        left.setPreferredSize(new Dimension(180, 0));
        left.add(leftTop, BorderLayout.NORTH);
        left.add(leftMid, BorderLayout.CENTER);
        left.add(leftBot, BorderLayout.SOUTH);

        //Psychology Panel
        psyPanel.add(left, BorderLayout.WEST);
        psyPanel.add(addTestPanel(), BorderLayout.CENTER);

        return psyPanel;
    }

    public JPanel addTestPanel() {
        addTestPanel = new JPanel(null);
        addTestPanel.setBackground(Color.white);

        //Add button
        addFiles = new JButton("Add Files");
        buttonDefault(addFiles, null, new Color(41, 189, 226));
        addFiles.setBounds(30, 30, 100, 30);
        addTestPanel.add(addFiles);

        //File chooser (windows appearance)
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (InstantiationException instantiationException) {
            instantiationException.printStackTrace();
        } catch (IllegalAccessException illegalAccessException) {
            illegalAccessException.printStackTrace();
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
            unsupportedLookAndFeelException.printStackTrace();
        }
        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG & MP4 & MP3", "jpg", "png", "mp4", "mp3");
        fc.setFileFilter(filter);

        // Save Button
        save = new JButton("Save");
        buttonDefault(save, null, new Color(41, 189, 226));
        save.setBounds(480, 500, 80, 30);
        addTestPanel.add(save);

        //Next Test Button
        nextTest = new JButton("Next");
        buttonDefault(nextTest, null, new Color(41, 189, 226));
        nextTest.setBounds(350, 500, 80, 30);
        addTestPanel.add(nextTest);

        // Empty panel
        emptyPanel = new JPanel(null);
        JLabel message = new JLabel("Please upload the file", SwingConstants.CENTER);
        message.setFont(new Font("TimesRoman", Font.PLAIN, 28));
        emptyPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        emptyPanel.setBackground(Color.white);
        message.setBounds(0, 0, 400, 200);
        emptyPanel.setBounds(100, 100, 400, 220);
        emptyPanel.add(message);
        addTestPanel.add(emptyPanel);

        mediaDisplay(addTestPanel);
        textAreaDisplay(addTestPanel, true);

        // Show URL
        showURL = new JLabel("");
        showURL.setBounds(110, 80, 350, 20);
        addTestPanel.add(showURL);

        // Upload numbers
        uploadNum = new JLabel("");
        uploadNum.setBounds(110, 320, 200, 20);
        addTestPanel.add(uploadNum);

        //Add question
        ImageIcon addQIcon = new ImageIcon("static/add.png");
        addQuestion = new JButton(addQIcon);
        buttonDefault(addQuestion, null, Color.white);
        addQuestion.setBounds(70, 460, 40, 40);
        addTestPanel.add(addQuestion);

        //Remove question
        ImageIcon removeIcon = new ImageIcon("static/remove.png");
        removeQuestion = new JButton(removeIcon);
        removeQuestion.setVisible(false);
        buttonDefault(removeQuestion, null, Color.white);
        removeQuestion.setBounds(140, 460, 40, 40);
        addTestPanel.add(removeQuestion);

        // Remove media
        removeMedia = new JButton(new ImageIcon("static/delete.png"));
        removeMedia.setVisible(false);
        buttonDefault(removeMedia, null, Color.white);
        removeMedia.setBounds(470, 70, 30, 30);
        addTestPanel.add(removeMedia);

        return addTestPanel;
    }

    public void textAreaDisplay(JPanel panel, boolean has_border) {
        //Text area
        question = new JTextArea("");
        question.setEditable(false);
        question.setBorder(BorderFactory.createLineBorder(Color.black));
        question.setFont(f2);
        // Use JScrollPane to hold JTextArea in order to view more info
        TextJp = new JScrollPane(question);
        TextJp.setBounds(70, 350, 500, 100);
        question.setBorder(null);
        TextJp.getVerticalScrollBar().setUI(new Bar());
        TextJp.getHorizontalScrollBar().setUI(new Bar());
        if (!has_border) {
            TextJp.setBorder(null);
        }else{
            TextJp.setBorder(BorderFactory.createLineBorder(Color.black));
        }
        panel.add(TextJp);
    }

    public void mediaDisplay(JPanel panel) {
        //Img Panel
        imgPanel = new imgPanel();
        imgPanel.setVisible(false);
        imgPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        imgPanel.setBackground(Color.white);
        imgPanel.setBounds(100, 100, 400, 220);
        panel.add(imgPanel);

        // Media Panel
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "d:/VideoLAN/VLC");
        mp = new MediaPlayer(this);
        mp.setVisible(false);

        mp.setBounds(100, 100, 400, 220);
        panel.add(mp);

        //Pre, next buttons
        pre = new JButton("<");
        next = new JButton(">");
        pre.setVisible(false);
        next.setVisible(false);
        buttonDefault(pre, f2, null);
        buttonDefault(next, f2, null);
        pre.setContentAreaFilled(false);
        next.setContentAreaFilled(false);
        pre.setBounds(400, 305, 50, 50);
        next.setBounds(460, 305, 50, 50);
        panel.add(pre);
        panel.add(next);
    }

    public JPanel showTestsPanel() {
        showTestsPanel = new JPanel(null);
        showTestsPanel.setBackground(Color.white);

        // Label
        JLabel label = new JLabel("Psychology Tests List");
        label.setFont(new Font("TimesRoman", Font.BOLD, 22));
        label.setBounds(50, 20, 270, 80);
        showTestsPanel.add(label);

        database.connect();
        int max = database.getMaxTID();

        String[][] tests = new String[max][6];
        String[] title = {"No", "Media Count", "Questions Count", "Publisher", "Creation date", "Stars"};
        for (int i = 0; i < max; i++) {
            tests[i][0] = "" + (i + 1);
            tests[i][1] = database.getAttr(i + 1, "MEDIAS").size() + "";
            tests[i][2] = database.getAttr(i + 1, "QUESTIONS").size() + "";
            tests[i][3] = database.getTestDetail(i + 1, "PUBLISHER");
            tests[i][4] = database.getTestDetail(i + 1, "DATE");
            tests[i][5] = database.getTestDetail(i + 1, "STARS");
        }
        database.close();
        // Cannot be modified
        testsTable = new JTable(tests, title) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        // Test center
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        testsTable.setDefaultRenderer(Object.class, r);
        testsTable.getColumn(title[0]).setPreferredWidth(20);
        testsTable.setRowHeight(30);
        testsTable.setShowVerticalLines(false);

        testsTable.getTableHeader().setBackground(Color.white);
        testsTable.setGridColor(new Color(222, 222, 222));
        testsTable.setFillsViewportHeight(true);
        // Forbid move header
        testsTable.getTableHeader().setReorderingAllowed(false);
        // Cancel header border
        UIManager.getDefaults().put("TableHeader.cellBorder", BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // Cell background
        JScrollPane scrollPane = new JScrollPane(testsTable);
        scrollPane.getViewport().setBackground(Color.white);
        // Cancel border
        scrollPane.setBounds(50, 100, 500, 400);
        LineBorder lb = new LineBorder(Color.black, 0);
        scrollPane.setBorder(lb);
        // Add hover listener
        tableHover(testsTable);
        testsTable.addMouseListener(controller);

        showTestsPanel.add(scrollPane);
        return showTestsPanel;
    }

    public JPanel clientPanel() {
        clientPanel = new JPanel(new BorderLayout());

        JPanel left = new JPanel(new BorderLayout());
        JPanel leftTop = new JPanel(new BorderLayout());
        JPanel leftMid = new JPanel(new BorderLayout());
        JPanel leftBot = new JPanel(new BorderLayout());

        //Set left color
        leftTop.setBackground(blue);
        leftMid.setBackground(blue);
        leftBot.setBackground(blue);

        // button default
        JButton showList2 = new JButton("Show Tests");
        back2 = new JButton("Return");

        JButton[] btns = new JButton[2];
        btns[0] = showList2;
        btns[1] = back2;

        hover(btns);

        showList2.setActionCommand("isPressed");
        back2.setActionCommand("unpressed");

        buttonDefault(back2, f2, blue);
        buttonDefault(showList2, f2, Color.white);

        //Left top -- label
        leftTop.setPreferredSize(new Dimension(0, 100));
        JLabel identity2 = new JLabel("Client", SwingConstants.CENTER);
        identity2.setFont(f1);
        leftTop.add(identity2);

        //Left bottom -- return button
        leftBot.setPreferredSize(new Dimension(0, 100));
        leftBot.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        leftBot.add(back2);

        //Left mid -- function buttons
        leftMid.add(showList2);
        leftMid.setBorder(BorderFactory.createEmptyBorder(50, 0, 260, 0));

        //Left
        left.setPreferredSize(new Dimension(180, 0));
        left.add(leftTop, BorderLayout.NORTH);
        left.add(leftMid, BorderLayout.CENTER);
        left.add(leftBot, BorderLayout.SOUTH);

        //Psychology Panel
        clientPanel.add(left, BorderLayout.WEST);
        clientPanel.add(showTestsPanel(), BorderLayout.CENTER);

        return clientPanel;
    }

    public JPanel doTestPanel(int TID, int TPID) {
        doTestPanel = new JPanel(null);
        // Add components
        detailCommonPart(doTestPanel, TID, TPID);

        JPanel answerPanel = new JPanel(new GridLayout(0,1));
        answerPanel.setBackground(Color.white);

        TextJp = new JScrollPane(answerPanel);
        TextJp.setBounds(20, 340, 580, 150);
        TextJp.setBorder(null);
        TextJp.getVerticalScrollBar().setUI(new Bar());

        doTestPanel.add(TextJp);
        setLocation(doTestPanel);

        if(lists.size() == 0){
            int height = (questionsList.size()+1) * 50;
            TextJp.setBounds(20, 100, 580, height);
        }

        for (int i = 0; i <= questionsList.size(); i++) {
            // The Question rows -> set height
            int max_rows = 1;
            JPanel each = new JPanel(new BorderLayout());
            JPanel left = new JPanel(new BorderLayout());
            JPanel right = new JPanel(new GridLayout(1,5));

            each.setBackground(Color.white);
            right.setBackground(Color.white);

            // Likert Scale
            if(i == 0){
                left.setBackground(Color.white);
                String[] scale = {"Strongly disagree", "Disagree", "Neural", "Agree", "Strongly agree"};
                for(String s : scale) {
                    JLabel label = new JLabel(s, SwingConstants.CENTER);
                    right.add(label);
                }
            }else{ // Questions
                String s = questionsList.get(i-1);
                JTextArea question = new JTextArea(s);
                question.setEditable(false);
                question.setLineWrap(true);
                question.setWrapStyleWord(true);
                left.add(question);
                ButtonGroup group = new ButtonGroup();
                for (int j = 0; j < 5; j++) {
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.white);
                    JRadioButton radioButton = new JRadioButton();
                    radioButton.setActionCommand((j+1)+"");
                    radioButton.setBackground(Color.white);
                    group.add(radioButton);
                    buttonPanel.add(radioButton);
                    right.add(buttonPanel);
                }
                pageBtnGroup.add(group);
            }
            left.setPreferredSize(new Dimension(180,30 + 20*(max_rows)));
            right.setPreferredSize(new Dimension(385,30 + 20*(max_rows)));
            each.add(left, BorderLayout.WEST);
            each.add(right, BorderLayout.CENTER);
            answerPanel.add(each);
        }
        totalBtnGroup.add(pageBtnGroup);
        // Reset pageBtnGroup
        pageBtnGroup = new ArrayList();

        return doTestPanel;
    }

    public JPanel thanksPanel(){
        thanksPanel = new JPanel(null);
        thanksPanel.setBackground(Color.white);

        ImageIcon inactivateIcon = new ImageIcon("static/inactivate.png");
        ImageIcon activateIcon = new ImageIcon("static/activate.png");

        // Thank Label
        JLabel thanksLabel = new JLabel("Thanks for filling in the questionnaire！");
        thanksLabel.setFont(new Font("TimesRoman", Font.PLAIN, 27));
        thanksLabel.setBounds(80,130, 470,50);
        thanksPanel.add(thanksLabel);

        // Please Label
        JLabel pleaseLabel = new JLabel("Please give a mark for this test 😊");
        pleaseLabel.setFont(f1);
        pleaseLabel.setBounds(120,220, 400,50);
        thanksPanel.add(pleaseLabel);

        // Mark Button
        for (int i = 0; i < 5; i++) {
            JToggleButton star = stars[i] = new JToggleButton();
            star.setIcon(inactivateIcon);
            star.setSelectedIcon(activateIcon);
            star.setBounds(140 + i * 70,300,40,40);
            star.setFocusPainted(false);
            star.setBorderPainted(false);
            star.addActionListener(controller);
            star.setContentAreaFilled(false);
            thanksPanel.add(star);
        }

        // Submit button
        submit = new JButton("Submit");
        buttonDefault(submit, null, new Color(41, 189, 226));
        submit.setBounds(450, 500, 100, 30);
        thanksPanel.add(submit);

        return thanksPanel;
    }

    public JPanel testDetailPanel(int TID, int TPID) {
        testDetailPanel = new JPanel(null);
        // Add components
        detailCommonPart(testDetailPanel, TID, TPID);

        // Edit Button
        edit = new JButton("Edit");
        buttonDefault(edit, null, new Color(41, 189, 226));
        edit.setBounds(30, 30, 100, 30);
        testDetailPanel.add(edit);

        // Query questions for specific page
        String content = "";
        for (int i = 1; i <= questionsList.size(); i++) {
            content += "Q" + i + ": " + questionsList.get(i - 1) + "\n";
        }
        // Display TextArea
        textAreaDisplay(testDetailPanel, false);
        question.setText(content);
        // Ensure the text are location
        setLocation(testDetailPanel);

        return testDetailPanel;
    }

    public void detailCommonPart(JPanel panel, int TID, int TPID) {
        panel.setBackground(Color.white);

        // Database Query
        database.connect();
        questionsList = database.getPageMediaList(TID, TPID, "QUESTIONS");
        lists = database.getPageMediaList(TID, TPID, "MEDIAS");
        int maxPageID = database.getMaxPageID(TID);
        database.close();

        // Back Button
        ImageIcon backIcon = new ImageIcon("static/back.png");
        backToList = new JButton(backIcon);
        buttonDefault(backToList,null,null);
        backToList.setBounds(500,50,40,40);
        panel.add(backToList);

        // Next Page Button
        showNextPage = new JButton("Next Page");
        buttonDefault(showNextPage, null, new Color(41, 189, 226));
        showNextPage.setBounds(450, 500, 100, 30);
        panel.add(showNextPage);
        if(TPID >= maxPageID) {
            if(controller.is_client){
                showNextPage.setVisible(false);
                finish = new JButton("Finish");
                buttonDefault(finish, null, new Color(41, 189, 226));
                finish.setBounds(450, 500, 100, 30);
                panel.add(finish);
            }else {
                showNextPage.setEnabled(false);
                showNextPage.setText("Final Page");
            }
        }

        // Pre Page Button
        showPrePage = new JButton("Pre Page");
        buttonDefault(showPrePage, null, new Color(41, 189, 226));
        showPrePage.setBounds(310, 500, 100, 30);
        panel.add(showPrePage);
        if(TPID == 1) {
            showPrePage.setVisible(false);
        }
    }
    // If no media, set text area in the middle of the panel
    public void setLocation(JPanel panel) {
        // Query media files
        if (lists.size() == 0) {
            TextJp.setBounds(70, 100, 460, 320);
        } else {
            mediaDisplay(panel);
            for (String s : lists) {
                File file = new File(s);
                fileList.add(file);
            }
        }
    }

    public void initialize() {
        pre = new JButton();
        database = new DatabaseAgent();
        fileList = new ArrayList();
        f1 = new Font("TimesRoman", Font.PLAIN, 25);
        f2 = new Font("TimesRoman", Font.PLAIN, 18);
        questionsList = new ArrayList();
        lists = new ArrayList();
        uploadNum = new JLabel("");
        showURL = new JLabel("");
        stars = new JToggleButton[5];
        pageBtnGroup = new ArrayList();
        totalBtnGroup = new ArrayList();
    }

    public void buttonDefault(JButton btn, Font f, Color color) {
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(controller);
        if (f != null) {
            btn.setFont(f);
        }
        if (color != null) {
            btn.setBackground(color);
        }
    }

    public void hover(JButton[] btns) {
        for (JButton btn : btns) {
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(Color.white);
                }

                public void mouseExited(MouseEvent e) {
                    if (btn.getActionCommand().equals("unpressed")) {
                        btn.setBackground(blue);
                    }
                }

                public void mouseClicked(MouseEvent e) {
                    for (JButton b : btns) {
                        b.setActionCommand("unpressed");
                        b.setBackground(blue);
                    }
                    btn.setBackground(Color.white);
                    btn.setActionCommand("isPressed");
                }
            });
        }
    }

    public void tableHover(JTable table) {
        table.addMouseMotionListener(new MouseMotionListener() {
            int hoveredRow = -1, hoveredColumn = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                java.awt.Point p = e.getPoint();
                hoveredRow = table.rowAtPoint(p);
                hoveredColumn = table.columnAtPoint(p);
                if (hoveredRow > -1 && hoveredRow < table.getRowCount() + 1) {
                    table.repaint();
                    table.setRowSelectionInterval(hoveredRow, hoveredRow);
                } else {
                    table.clearSelection();
                    table.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                hoveredRow = hoveredColumn = -1;
                table.repaint();
            }
        });
    }
}

class imgPanel extends JPanel {

    ImageIcon icon;
    Image img;

    public imgPanel() {
        this.setLayout(null);
    }

    public void setImg(String filePath) {
        icon = new ImageIcon(filePath);
        img = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, 400, 220, this);
    }
}

