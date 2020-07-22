package MediaPlayer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.DefaultAdaptiveRuntimeFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class MediaPlayer extends JPanel {

    private JPanel contentPane;
    //Create Panel
    EmbeddedMediaPlayerComponent playerComponent;
    private final JPanel panel = new JPanel();
    private JProgressBar progress;

    public MediaPlayer(Window w) {
        this.setLayout(new BorderLayout());
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createLineBorder(Color.black));
        contentPane.setLayout(new BorderLayout(0, 0));


        JPanel videopanel = new JPanel();
        contentPane.add(videopanel, BorderLayout.CENTER);
        videopanel.setLayout(new BorderLayout(0, 0));

        playerComponent = new EmbeddedMediaPlayerComponent();

        playerComponent.getMediaPlayer().setFullScreenStrategy(
                new DefaultAdaptiveRuntimeFullScreenStrategy(w){
                    @Override
                    protected void beforeEnterFullScreen() {

                    }

                    @Override
                    protected void afterExitFullScreen() {
                    }
                }
        );

        videopanel.add(playerComponent, BorderLayout.CENTER);
        videopanel.add(panel, BorderLayout.SOUTH);


        panel.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.white);
        controlPanel.setPreferredSize(new Dimension(0,15));
        panel.add(controlPanel);

        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, -5));

        playerComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                controlPanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                controlPanel.setVisible(false);
            }
        });

        JToggleButton button = new JToggleButton();
        button.setIcon(new ImageIcon("static/play.png"));
        button.setSelectedIcon(new ImageIcon("static/pause.png"));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(Color.white);
        controlPanel.add(button);

        JButton stopBtn = new JButton();
        stopBtn.setIcon(new ImageIcon("static/stop.png"));
        stopBtn.setBackground(Color.white);
        stopBtn.setFocusPainted(false);
        stopBtn.setBorderPainted(false);
        controlPanel.add(stopBtn);

        JSlider slider = new JSlider();
        slider.setBackground(Color.white);
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
        progress = new JProgressBar();
        progress.setBackground(Color.white);
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
        progress.setPreferredSize(new Dimension(0,15));
        panel.add(progress, BorderLayout.NORTH);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!button.isSelected()){
                    playerComponent.getMediaPlayer().pause();
                }else{
                    playerComponent.getMediaPlayer().play();
                }

            }
        });

        stopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerComponent.getMediaPlayer().stop();
                button.setSelected(false);
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