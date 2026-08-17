package vampirewargame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public final class Tema {

    public static final Color FONDO = new Color(8, 8, 10);
    public static final Color FONDO_SECUNDARIO = new Color(22, 22, 25);
    public static final Color PANEL = new Color(16, 16, 19);
    public static final Color PANEL_CLARO = new Color(30, 30, 34);

    public static final Color ROJO = new Color(80, 80, 84);
    public static final Color ROJO_HOVER = new Color(108, 108, 114);
    public static final Color ROJO_PRESIONADO = new Color(48, 48, 52);
    public static final Color BORDE = new Color(104, 104, 112);
    public static final Color DORADO = new Color(208, 208, 214);

    public static final Color TEXTO = new Color(248, 248, 248);
    public static final Color TEXTO_SECUNDARIO = new Color(205, 205, 210);

    public static final Color NEGRO_CARTA = new Color(18, 18, 21);
    public static final Color BLANCO_CARTA = new Color(242, 242, 244);
    public static final Color BORDE_BLANCO = new Color(232, 232, 235);
    public static final Color BORDE_NEGRO = new Color(210, 210, 215);

    public static final Color EQUIPO_BLANCO = new Color(238, 238, 240);
    public static final Color EQUIPO_NEGRO = new Color(25, 25, 29);
    public static final Color ACENTO_BLANCO = new Color(188, 188, 194);
    public static final Color ACENTO_NEGRO = new Color(235, 235, 238);

    private Tema() {
    }

    public static JLabel crearTitulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("Serif", Font.BOLD, tamano));
        etiqueta.setForeground(TEXTO);
        return etiqueta;
    }

    public static JLabel crearSubtitulo(String texto, int tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.PLAIN, tamano));
        etiqueta.setForeground(TEXTO_SECUNDARIO);
        return etiqueta;
    }

    public static JPanel crearTarjeta() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        return panel;
    }

    public static JPanel crearTarjeta(String titulo) {
        JPanel panel = crearTarjeta();
        panel.putClientProperty("titulo", titulo);
        return panel;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setBackground(new Color(24, 24, 28));
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);
        campo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        campo.setPreferredSize(new Dimension(260, 38));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE, 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
    }

    public static void prepararBoton(BotonMenu boton) {
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(8, 18, 8, 18));
    }
}
