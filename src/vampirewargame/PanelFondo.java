package vampirewargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/** Fondo limpio y monocromático, sin tablero, ajedrez ni elementos que distraigan. */
public class PanelFondo extends JPanel {

    public PanelFondo() {
        setOpaque(true);
        setBackground(Tema.FONDO);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        GradientPaint fondo = new GradientPaint(0, 0, new Color(11, 11, 14), 0, h, new Color(4, 4, 6));
        g2.setPaint(fondo);
        g2.fillRect(0, 0, w, h);

        // Líneas y marcos monocromáticos, sin iconografía de ajedrez.
        g2.setColor(new Color(235, 235, 238, 80));
        g2.setStroke(new BasicStroke(1f));
        g2.drawRect(12, 12, Math.max(0, w - 24), Math.max(0, h - 24));
        g2.setColor(new Color(255, 255, 255, 26));
        g2.drawRect(18, 18, Math.max(0, w - 36), Math.max(0, h - 36));

        g2.dispose();
    }
}
