package vampirewargame;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaInicio extends VentanaBase {

    public VentanaInicio(SistemaJuego sistema) {
        super(sistema, "VAMPIRE WARGAME");
        setSubtitulo("Selecciona una opción para comenzar");
        construirMenu();
    }

    private void construirMenu() {
        panelBotones.setLayout(new GridLayout(3, 1, 0, 18));

        BotonMenu botonIniciar = crearBoton("INICIAR SESIÓN");
        BotonMenu botonCrear = crearBoton("CREAR JUGADOR");
        BotonMenu botonSalir = crearBoton("SALIR");

        botonIniciar.addActionListener(e -> iniciarSesion());
        botonCrear.addActionListener(e -> crearJugador());
        botonSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(botonIniciar);
        panelBotones.add(botonCrear);
        panelBotones.add(botonSalir);
    }

    private void iniciarSesion() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContrasena = new JPasswordField();
        Tema.estilizarCampo(campoUsuario);
        Tema.estilizarCampo(campoContrasena);

        JPanel panel = crearPanelFormulario();
        panel.add(crearEtiqueta("Usuario"));
        panel.add(campoUsuario);
        panel.add(crearEtiqueta("Contraseña"));
        panel.add(campoContrasena);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Iniciar sesión", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            sistema.iniciarSesion(campoUsuario.getText(), new String(campoContrasena.getPassword()));
            abrirMenuPrincipal();
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo iniciar sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearJugador() {
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContrasena = new JPasswordField();
        Tema.estilizarCampo(campoUsuario);
        Tema.estilizarCampo(campoContrasena);

        JPanel panel = crearPanelFormulario();
        panel.add(crearEtiqueta("Nuevo usuario"));
        panel.add(campoUsuario);
        panel.add(crearEtiqueta("Contraseña de 5 caracteres"));
        panel.add(campoContrasena);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Crear jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            sistema.crearJugador(campoUsuario.getText(), new String(campoContrasena.getPassword()));
            JOptionPane.showMessageDialog(this, "Jugador creado correctamente. La sesión se inició automáticamente.");
            abrirMenuPrincipal();
        } catch (JuegoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo crear el jugador", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 6, 6));
        panel.setBackground(Tema.PANEL);
        return panel;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(Tema.TEXTO);
        return etiqueta;
    }

    private void abrirMenuPrincipal() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }
}
