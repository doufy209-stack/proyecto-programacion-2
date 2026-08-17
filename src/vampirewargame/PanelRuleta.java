package vampirewargame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

/** Ruleta limpia en blanco y negro, con sectores amplios y legibles. */
public class PanelRuleta extends JPanel {

    private final TipoPieza[] sectores;
    private double rotacion;
    private boolean animando;
    private TipoPieza ultimoResultado;

    public PanelRuleta() {
        sectores = new TipoPieza[]{
            TipoPieza.HOMBRE_LOBO, TipoPieza.VAMPIRO, TipoPieza.NECROMANTE,
            TipoPieza.HOMBRE_LOBO, TipoPieza.VAMPIRO, TipoPieza.NECROMANTE
        };
        rotacion = 0;
        animando = false;
        ultimoResultado = null;
        setOpaque(false);
        setPreferredSize(new Dimension(220, 220));
        setMinimumSize(new Dimension(210, 210));
    }

    public boolean isAnimando() { return animando; }

    public void animarHasta(TipoPieza resultadoAccion, Runnable alFinalizar) {
        if (animando || resultadoAccion == null) return;
        int[] coincidencias = new int[2];
        int cantidadDanio = 0;
        for (int i = 0; i < sectores.length; i++) {
            if (sectores[i] == resultadoAccion) coincidencias[cantidadDanio++] = i;
        }
        int indiceObjetivo = coincidencias[(int) (Math.random() * cantidadDanio)];
        double objetivoModulo = (360.0 - indiceObjetivo * 60.0) % 360.0;
        double actualModulo = ((rotacion % 360.0) + 360.0) % 360.0;
        double diferencia = objetivoModulo - actualModulo;
        if (diferencia < 0) diferencia += 360.0;

        final double inicio = rotacion;
        final double recorrido = 5 * 360.0 + diferencia;
        final long tiempoInicio = System.currentTimeMillis();
        final int duracion = 1900;
        animando = true;
        ultimoResultado = null;

        Timer temporizador = new Timer(16, null);
        temporizador.addActionListener(e -> {
            long transcurrido = System.currentTimeMillis() - tiempoInicio;
            double progreso = Math.min(1.0, transcurrido / (double) duracion);
            double suavizado = 1.0 - Math.pow(1.0 - progreso, 3.0);
            rotacion = inicio + recorrido * suavizado;
            repaint();
            if (progreso >= 1.0) {
                temporizador.stop();
                rotacion = inicio + recorrido;
                ultimoResultado = resultadoAccion;
                animando = false;
                repaint();
                if (alFinalizar != null) alFinalizar.run();
            }
        });
        temporizador.start();
    }

    private String getNombreSector(TipoPieza categoriaPieza) {
        if (categoriaPieza == TipoPieza.HOMBRE_LOBO) return "LOBO";
        if (categoriaPieza == TipoPieza.VAMPIRO) return "VAMPIRO";
        if (categoriaPieza == TipoPieza.NECROMANTE) return "NECROMANTE";
        return "ZOMBIE";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int diametro = Math.min(getWidth(), getHeight()) - 20;
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int x = cx - diametro / 2;
        int y = cy - diametro / 2;

        Graphics2D rueda = (Graphics2D) g2.create();
        rueda.rotate(Math.toRadians(rotacion), cx, cy);

        for (int i = 0; i < sectores.length; i++) {
            int inicio = 90 - i * 60;
            boolean blanco = i % 2 == 0;
            Color fondo = blanco ? new Color(245, 245, 245) : new Color(20, 20, 22);

            rueda.setColor(fondo);
            rueda.fillArc(x, y, diametro, diametro, inicio, -60);
            rueda.setColor(Color.BLACK);
            rueda.setStroke(new BasicStroke(2.5f));
            rueda.drawArc(x, y, diametro, diametro, inicio, -60);

            // Una sola ficha de imagen por sector, centrada dentro de su propio espacio.
            double angulo = Math.toRadians(-90 + i * 60 + 30);
            int radioImagen = (int) (diametro * 0.32);
            int tamImagen = Math.max(44, Math.min(60, diametro / 4));
            int ix = cx + (int) (Math.cos(angulo) * radioImagen) - tamImagen / 2;
            int iy = cy + (int) (Math.sin(angulo) * radioImagen) - tamImagen / 2;

            rueda.setColor(blanco ? new Color(35, 35, 38) : new Color(235, 235, 238));
            rueda.fillOval(ix - 3, iy - 3, tamImagen + 6, tamImagen + 6);

            BufferedImage imagen = RecursosImagenes.getImagenPieza(sectores[i]);
            if (imagen != null) {
                rueda.drawImage(imagen, ix, iy, tamImagen, tamImagen, null);
            }
        }

        rueda.setStroke(new BasicStroke(4f));
        rueda.setColor(Color.BLACK);
        rueda.drawOval(x, y, diametro, diametro);
        rueda.dispose();

        int centro = Math.min(82, diametro / 3);
        int centroX = cx - centro / 2;
        int centroY = cy - centro / 2;
        g2.setColor(new Color(250, 250, 250));
        g2.fillOval(centroX, centroY, centro, centro);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3f));
        g2.drawOval(centroX, centroY, centro, centro);

        String centroTexto = animando ? "..." : (ultimoResultado == null ? "RULETA" : getNombreSector(ultimoResultado));
        int tamCentro = centroTexto.length() > 8 ? 9 : 12;
        g2.setFont(new Font("Serif", Font.BOLD, tamCentro));
        FontMetrics fmCentro = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        g2.drawString(centroTexto, cx - fmCentro.stringWidth(centroTexto) / 2, cy + fmCentro.getAscent() / 3);

        // Puntero centrado exactamente sobre el eje vertical de la ruleta.
        Polygon puntero = new Polygon();
        puntero.addPoint(cx, 1);
        puntero.addPoint(cx - 12, 23);
        puntero.addPoint(cx + 12, 23);
        g2.setColor(Color.WHITE);
        g2.fillPolygon(puntero);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2f));
        g2.drawPolygon(puntero);
        g2.dispose();
    }
}
