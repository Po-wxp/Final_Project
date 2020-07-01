package MediaPlayer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaPlayer extends JPanel {

    private JPanel contentPane;
    //创建播放器界面组件
    EmbeddedMediaPlayerComponent playerComponent;
    private final JPanel panel = new JPanel();
    private JProgressBar progress;

    public MediaPlayer() {
        this.setLayout(new BorderLayout());
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));


        JPanel videopanel = new JPanel();
        contentPane.add(videopanel, BorderLayout.CENTER);
        videopanel.setLayout(new BorderLayout(0, 0));

        playerComponent = new EmbeddedMediaPlayerComponent();
        videopanel.add(playerComponent, BorderLayout.CENTER);
        videopanel.add(panel, BorderLayout.SOUTH);

        panel.setLayout(new BorderLayout(0, 0));

        JPanel controlPanel = new JPanel();
        panel.add(controlPanel);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnNewButton = new JButton("play");
        btnNewButton.setFocusPainted(false);
        btnNewButton.setBorderPainted(false);
        controlPanel.add(btnNewButton);

        JButton button = new JButton("pause");
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        controlPanel.add(button);

        JSlider slider = new JSlider();
        //Max volume is 120
        slider.setValue(100);
        slider.setMaximum(120);
        controlPanel.add(slider);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent arg0) {
                //Send volume to the controller
                volume(slider.getValue());
            }
        });
        //视频播放进度条
        progress = new JProgressBar();
        progress.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Click volume position
                int x = e.getX();
                // Percentage
                jumpTo(((float)x/progress.getWidth()));
            }
        });
        progress.setStringPainted(true);
        panel.add(progress, BorderLayout.NORTH);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerComponent.getMediaPlayer().pause();
            }
        });
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                playerComponent.getMediaPlayer().play();
            }
        });
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        this.add(contentPane);



    }
    // Return the player
    public EmbeddedMediaPlayer getMediaPlayer() {
        return playerComponent.getMediaPlayer();
    }
    // Return the percentage
    public JProgressBar getProgressBar() {
        return progress;
    }

    public void jumpTo(float to) {
        playerComponent.getMediaPlayer().setTime((long)( to*playerComponent.getMediaPlayer().getLength()));
    }

    public void volume(int v) {
        playerComponent.getMediaPlayer().setVolume(v);
    }

    public void setFile(File file){
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    playerComponent.getMediaPlayer().prepareMedia(file.getAbsolutePath());
//                    Thread.sleep(200);
//                    playerComponent.getMediaPlayer().pause();
                    new SwingWorker<String, Integer>() {
                        // Adjust the volume
                        protected String doInBackground() throws Exception {
                            while (true) {
                                long total = playerComponent.getMediaPlayer().getLength();
                                long curr = playerComponent.getMediaPlayer().getTime();
                                float percent = ((float)curr/total);
                                publish((int)(percent*100));
                                Thread.sleep(100);
                            }
                        }
                        protected void process(java.util.List<Integer> chunks) {
                            for (int v:chunks) {
                                progress.setValue(v);
                            }
                        };
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}