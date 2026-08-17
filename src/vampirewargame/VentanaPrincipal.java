package vampirewargame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class VentanaPrincipal extends VentanaBase {

    public VentanaPrincipal(SistemaJuego sistema) {
        super(sistema, "VAMPIRE WARGAME");
        ocultarEncabezado();
        Jugador participante = sistema.getSesionActual();
        construirMenu(participante);
    }

    private void construirMenu(Jugador participante) {
        panelBotones.removeAll();
        panelBotones.setLayout(new GridLayout(2, 2, 22, 22));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));

        panelBotones.add(crearBotonIcono("▶", "JUGAR", e -> abrirVentanaJugar()));
        panelBotones.add(crearBotonIcono("●", "MI CUENTA", e -> abrirMiCuenta()));
        panelBotones.add(crearBotonIcono("▥", "REPORTES", e -> abrirReportes()));
        panelBotones.add(crearBotonIcono("↪", "CERRAR SESIÓN", e -> cerrarSesion()));

        // El jugador queda indicado de forma discreta dentro del propio menú,
        // sin reproducir el encabezado del diseño original.
        subtitulo.setText(participante.getUsuario() + "  ·  " + participante.getPuntos() + " puntos");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitulo.setForeground(Color.WHITE);

        panelBotones.revalidate();
        panelBotones.repaint();
    }

    private JButton crearBotonIcono(String icono, String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton();
        boton.setLayout(new java.awt.BorderLayout());
        boton.setFocusPainted(false);
        boton.setOpaque(true);
        boton.setBackground(new Color(12, 12, 14));
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(185, 185, 190), 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        boton.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        boton.setMargin(new Insets(0, 0, 0, 0));

        javax.swing.JLabel etiquetaIcono = new javax.swing.JLabel(icono, SwingConstants.CENTER);
        etiquetaIcono.setFont(new Font("SansSerif", Font.BOLD, 42));
        etiquetaIcono.setForeground(Color.WHITE);

        javax.swing.JLabel etiquetaTexto = new javax.swing.JLabel(texto, SwingConstants.CENTER);
        etiquetaTexto.setFont(new Font("SansSerif", Font.BOLD, 17));
        etiquetaTexto.setForeground(Color.WHITE);

        boton.add(etiquetaIcono, java.awt.BorderLayout.CENTER);
        boton.add(etiquetaTexto, java.awt.BorderLayout.SOUTH);
        boton.addActionListener(accion);
        boton.setPreferredSize(new Dimension(250, 150));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(32, 32, 36));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(new Color(12, 12, 14));
            }
        });
        return boton;
    }

    private void abrirVentanaJugar() {
        new VentanaJugar(sistema).setVisible(true);
        dispose();
    }

    private void abrirMiCuenta() {
        new VentanaMiCuenta(sistema).setVisible(true);
        dispose();
    }

    private void abrirReportes() {
        new VentanaReportes(sistema).setVisible(true);
        dispose();
    }

    private void cerrarSesion() {
        sistema.cerrarSesion();
        new VentanaInicio(sistema).setVisible(true);
        dispose();
    }
}
