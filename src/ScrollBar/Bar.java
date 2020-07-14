package ScrollBar;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class Bar extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        // thumbLightShadowColor
        trackColor = Color.black;
        setThumbBounds(0, 0, 3, 10);
    }

    // Width
    @Override
    public Dimension getPreferredSize(JComponent c) {
        c.setPreferredSize(new Dimension(15, 0));
        return super.getPreferredSize(c);

    }

    // Background
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint gp = null;

        //Direction
        if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            gp = new GradientPaint(0, 0, new Color(80, 80, 80),
                    trackBounds.width, 0, new Color(80, 80, 80));
        }
        if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
            gp = new GradientPaint(0, 0, new Color(80, 80, 80),
                    trackBounds.height, 0, new Color(80, 80, 80));
        }
        g2.setPaint(gp);

        //Track
        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width,
                trackBounds.height);

        //Track Border
        if (trackHighlight == BasicScrollBarUI.DECREASE_HIGHLIGHT)
            this.paintDecreaseHighlight(g);
        if (trackHighlight == BasicScrollBarUI.INCREASE_HIGHLIGHT)
            this.paintIncreaseHighlight(g);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        // x,y
        g.translate(thumbBounds.x, thumbBounds.y);

        // Handle color
        g.setColor(new Color(   230,230,250));

        // Clear
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.addRenderingHints(rh);

        // Opacity
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        // Fill
        g2.fillRoundRect(0, 0, 40, thumbBounds.height - 1, 5, 5);

    }

    // Buttons
    @Override
    protected JButton createIncreaseButton(int orientation) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setBorder(null);
        return button;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusable(false);
        button.setBorder(null);
        return button;
    }

}
