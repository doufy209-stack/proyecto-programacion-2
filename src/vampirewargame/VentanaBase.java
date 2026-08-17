package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Base para los menús principales, con una composición propia y monocromática. */
public abstract class VentanaBase extends JFrame {

    protected final SistemaJuego sistema;
    protected JPanel panelBotones;
    protected JLabel subtitulo;
    protected JLabel encabezado;
    protected JPanel tarjetaPrincipal;

    public VentanaBase(SistemaJuego sistema, String titulo) {
        this.sistema = sistema;

        setTitle("Vampire Wargame - " + titulo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1080, 760));
        setMinimumSize(new Dimension(920, 660));
        setLocationRelativeTo(null);
        setResizable(true);

        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setOpaque(false);
        contenido.setBorder(BorderFactory.createEmptyBorder(42, 80, 42, 80));

        JPanel indiceColumna = new JPanel();
        indiceColumna.setOpaque(false);
        indiceColumna.setLayout(new BoxLayout(indiceColumna, BoxLayout.Y_AXIS));
        indiceColumna.setPreferredSize(new Dimension(720, 580));

        encabezado = Tema.crearTitulo(titulo, titulo.equalsIgnoreCase("VAMPIRE WARGAME") ? 50 : 38);
        encabezado.setAlignmentX(CENTER_ALIGNMENT);
        encabezado.setForeground(Tema.TEXTO);

        subtitulo = Tema.crearSubtitulo("", 15);
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        subtitulo.setForeground(Tema.TEXTO_SECUNDARIO);

        tarjetaPrincipal = Tema.crearTarjeta();
        tarjetaPrincipal.setLayout(new BorderLayout());
        tarjetaPrincipal.setMaximumSize(new Dimension(720, 420));
        tarjetaPrincipal.setAlignmentX(CENTER_ALIGNMENT);
        tarjetaPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(180, 180, 186), 1),
                BorderFactory.createEmptyBorder(20, 24, 20, 24)
        ));

        panelBotones = new JPanel();
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(24, 55, 22, 55));
        tarjetaPrincipal.add(panelBotones, BorderLayout.CENTER);

        indiceColumna.add(Box.createVerticalGlue());
        indiceColumna.add(encabezado);
        indiceColumna.add(Box.createRigidArea(new Dimension(0, 8)));
        indiceColumna.add(subtitulo);
        indiceColumna.add(Box.createRigidArea(new Dimension(0, 24)));
        indiceColumna.add(tarjetaPrincipal);
        indiceColumna.add(Box.createVerticalGlue());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        contenido.add(indiceColumna, gbc);

        fondo.add(contenido, BorderLayout.CENTER);
    }

    protected void ocultarEncabezado() {
        encabezado.setVisible(false);
        subtitulo.setVisible(false);
        columnaRecompactar();
    }

    private void columnaRecompactar() {
        if (encabezado != null) {
            encabezado.getParent().revalidate();
            encabezado.getParent().repaint();
        }
    }

    protected void setSubtitulo(String texto) {
        subtitulo.setText(texto);
    }

    protected BotonMenu crearBoton(String texto) {
        BotonMenu boton = new BotonMenu(texto);
        boton.setEstiloClaro(true);
        return boton;
    }
}
