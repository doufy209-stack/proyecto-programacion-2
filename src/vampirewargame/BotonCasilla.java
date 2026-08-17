package vampirewargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JButton;

public class BotonCasilla extends JButton {

    private final int renglon;
    private final int indiceColumna;
    private Pieza elementoPieza;
    private boolean seleccionada;

    public BotonCasilla(int renglon, int indiceColumna) {
        this.renglon = renglon;
        this.indiceColumna = indiceColumna;
        setPreferredSize(new Dimension(122, 128));
        setMinimumSize(new Dimension(90, 100));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setRolloverEnabled(true);
    }

    public void actualizar(Pieza elementoPieza, boolean seleccionada) {
        this.elementoPieza = elementoPieza;
        this.seleccionada = seleccionada;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        boolean casillaClara = (renglon + indiceColumna) % 2 == 0;
        Color colorCasilla = casillaClara ? new Color(45, 50, 60) : new Color(21, 24, 31);
        if (getModel().isRollover()) {
            colorCasilla = casillaClara ? new Color(62, 68, 80) : new Color(34, 38, 48);
        }

        g2.setColor(colorCasilla);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(new Color(100, 108, 124));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (seleccionada) {
            g2.setStroke(new BasicStroke(4f));
            g2.setColor(Tema.DORADO);
            g2.drawRect(3, 3, getWidth() - 7, getHeight() - 7);
        }

        if (elementoPieza != null) {
            boolean blanco = elementoPieza.getColor() == ColorBando.BLANCO;
            int margen = 4;
            int cardX = margen;
            int cardY = margen;
            int cardW = getWidth() - margen * 2;
            int cardH = getHeight() - margen * 2;
            Color fondoCarta = blanco ? Tema.BLANCO_CARTA : Tema.NEGRO_CARTA;
            Color bordeCarta = blanco ? new Color(30, 30, 34) : new Color(230, 230, 235);

            g2.setColor(fondoCarta);
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 18, 18);
            g2.setStroke(new BasicStroke(3f));
            g2.setColor(bordeCarta);
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 18, 18);

            // La ficha muestra únicamente el retrato. No se imprimen etiquetas ni A/V/E.
            int tamImagen = Math.min(cardW - 10, cardH - 10);
            int x = (getWidth() - tamImagen) / 2;
            int y = (getHeight() - tamImagen) / 2;
            BufferedImage imagen = RecursosImagenes.getImagenPieza(elementoPieza.getTipo());

            if (imagen != null) {
                Shape anterior = g2.getClip();
                Shape clip = new Ellipse2D.Double(x, y, tamImagen, tamImagen);
                g2.setClip(clip);
                g2.drawImage(imagen, x, y, tamImagen, tamImagen, null);
                g2.setClip(anterior);
            }

            g2.setStroke(new BasicStroke(2f));
            g2.setColor(blanco ? new Color(70, 70, 76) : new Color(230, 230, 235));
            g2.drawOval(x, y, tamImagen, tamImagen);
        }

        g2.dispose();
    }
}
