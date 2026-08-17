package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class VentanaPartida extends JFrame {

    private final SistemaJuego sistema;
    private final Partida estadoPartida;
    private final BotonCasilla[][] botonesTablero;
    private final PanelRuleta panelRuleta;
    private JLabel etiquetaTurno;
    private JLabel textoTipoSeleccionado;
    private JLabel contadorGiros;
    private JLabel mensajeInstruccion;
    private JLabel etiquetaEquipoBlanco;
    private JLabel etiquetaEquipoNegro;
    private JTextArea registroEventos;
    private BotonMenu botonRuleta;
    private PanelDescripcionPieza panelDetalleFicha;
    private int renglonOrigen;
    private int indiceColumnaOrigen;
    private boolean ruletaEnMovimiento;

    public VentanaPartida(SistemaJuego sistema, Partida estadoPartida) {
        this.sistema = sistema;
        this.estadoPartida = estadoPartida;
        botonesTablero = new BotonCasilla[Tablero.TAMANO][Tablero.TAMANO];
        panelRuleta = new PanelRuleta();
        renglonOrigen = -1;
        indiceColumnaOrigen = -1;
        ruletaEnMovimiento = false;

        setTitle("Vampire Wargame - Partida");
        setSize(1360, 900);
        setMinimumSize(new Dimension(1180, 820));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        PanelFondo fondo = new PanelFondo();
        fondo.setLayout(new BorderLayout(16, 14));
        fondo.setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));
        setContentPane(fondo);

        JPanel cabecera = crearCabecera();
        fondo.add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.setOpaque(false);
        centro.add(crearPanelTablero(), BorderLayout.CENTER);
        centro.add(crearLateral(), BorderLayout.EAST);
        fondo.add(centro, BorderLayout.CENTER);

        fondo.add(crearPie(), BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                retirarse();
            }
        });

        registrarEvento("Partida iniciada. El Equipo Blanco comienza.");
        actualizarVista();
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(14, 0));
        cabecera.setOpaque(true);
        cabecera.setBackground(new Color(10, 13, 19, 235));
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.DORADO, 1, true),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));

        JLabel titulo = Tema.crearTitulo("VAMPIRE WARGAME", 30);
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        titulo.setHorizontalAlignment(SwingConstants.LEFT);
        izquierda.add(titulo);

        JLabel jugadores = Tema.crearSubtitulo(
                "BLANCO  " + estadoPartida.getJugador1().getUsuario() + "   VS   NEGRO  " + estadoPartida.getJugador2().getUsuario(), 13);
        jugadores.setAlignmentX(LEFT_ALIGNMENT);
        jugadores.setHorizontalAlignment(SwingConstants.LEFT);
        izquierda.add(jugadores);

        JPanel equipos = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 4));
        equipos.setOpaque(false);
        etiquetaEquipoBlanco = crearChip("BLANCO", Tema.EQUIPO_BLANCO, Color.BLACK);
        etiquetaEquipoNegro = crearChip("NEGRO", Tema.EQUIPO_NEGRO, Color.WHITE);
        equipos.add(etiquetaEquipoBlanco);
        equipos.add(etiquetaEquipoNegro);
        izquierda.add(equipos);

        etiquetaTurno = Tema.crearTitulo("", 19);
        etiquetaTurno.setHorizontalAlignment(SwingConstants.RIGHT);

        cabecera.add(izquierda, BorderLayout.WEST);
        cabecera.add(etiquetaTurno, BorderLayout.EAST);
        return cabecera;
    }

    private JLabel crearChip(String texto, Color fondo, Color textoColor) {
        JLabel chip = new JLabel(texto, SwingConstants.CENTER);
        chip.setOpaque(true);
        chip.setBackground(fondo);
        chip.setForeground(textoColor);
        chip.setFont(new Font("SansSerif", Font.BOLD, 11));
        chip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(textoColor, 1, true),
                BorderFactory.createEmptyBorder(4, 11, 4, 11)
        ));
        return chip;
    }

    private JPanel crearPanelTablero() {
        JPanel tarjeta = Tema.crearTarjeta();
        tarjeta.setLayout(new BorderLayout(0, 9));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(106, 113, 127), 1, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        JPanel cabeceraTablero = new JPanel(new BorderLayout());
        cabeceraTablero.setOpaque(false);
        JLabel arriba = Tema.crearSubtitulo("▲  JUGADOR NEGRO — " + estadoPartida.getJugador2().getUsuario(), 13);
        arriba.setFont(new Font("SansSerif", Font.BOLD, 13));
        arriba.setOpaque(true);
        arriba.setBackground(Tema.EQUIPO_NEGRO);
        arriba.setForeground(Color.WHITE);
        arriba.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        cabeceraTablero.add(arriba, BorderLayout.NORTH);

        JPanel tableroActual = new JPanel(new GridLayout(Tablero.TAMANO, Tablero.TAMANO, 4, 4));
        tableroActual.setOpaque(false);
        for (int renglon = 0; renglon < Tablero.TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < Tablero.TAMANO; indiceColumna++) {
                final int f = renglon;
                final int c = indiceColumna;
                BotonCasilla boton = new BotonCasilla(renglon, indiceColumna);
                boton.addActionListener(e -> seleccionarCasilla(f, c));
                botonesTablero[renglon][indiceColumna] = boton;
                tableroActual.add(boton);
            }
        }
        tarjeta.add(cabeceraTablero, BorderLayout.NORTH);
        tarjeta.add(tableroActual, BorderLayout.CENTER);

        JLabel abajo = Tema.crearSubtitulo("▼  JUGADOR BLANCO — " + estadoPartida.getJugador1().getUsuario(), 13);
        abajo.setFont(new Font("SansSerif", Font.BOLD, 13));
        abajo.setOpaque(true);
        abajo.setBackground(Tema.EQUIPO_BLANCO);
        abajo.setForeground(Color.BLACK);
        abajo.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        tarjeta.add(abajo, BorderLayout.SOUTH);
        return tarjeta;
    }

    private JPanel crearLateral() {
        JPanel lateral = new JPanel();
        lateral.setOpaque(false);
        lateral.setPreferredSize(new Dimension(430, 0));
        lateral.setLayout(new BoxLayout(lateral, BoxLayout.Y_AXIS));

        JPanel panelRueda = Tema.crearTarjeta();
        panelRueda.setLayout(new BoxLayout(panelRueda, BoxLayout.Y_AXIS));
        panelRueda.setAlignmentX(CENTER_ALIGNMENT);
        panelRueda.setPreferredSize(new Dimension(430, 300));
        panelRueda.setMinimumSize(new Dimension(410, 290));
        panelRueda.setMaximumSize(new Dimension(430, 305));
        panelRueda.add(crearTituloTarjeta("RULETA DE PERSONAJES"));
        panelRueda.add(Box.createRigidArea(new Dimension(0, 0)));
        panelRuleta.setAlignmentX(CENTER_ALIGNMENT);
        panelRueda.add(panelRuleta);

        botonRuleta = new BotonMenu("GIRAR RULETA");
        botonRuleta.setMaximumSize(new Dimension(360, 48));
        botonRuleta.setAlignmentX(CENTER_ALIGNMENT);
        botonRuleta.addActionListener(e -> girarRuleta());
        panelRueda.add(botonRuleta);

        textoTipoSeleccionado = Tema.crearSubtitulo("Tipo habilitado: ninguno", 14);
        textoTipoSeleccionado.setForeground(Tema.TEXTO);
        contadorGiros = Tema.crearSubtitulo("Giros: 0/1", 13);
        contadorGiros.setForeground(Tema.TEXTO_SECUNDARIO);
        panelRueda.add(Box.createRigidArea(new Dimension(0, 4)));
        panelRueda.add(textoTipoSeleccionado);
        panelRueda.add(Box.createRigidArea(new Dimension(0, 0)));
        panelRueda.add(contadorGiros);
        lateral.add(panelRueda);
        lateral.add(Box.createRigidArea(new Dimension(0, 6)));

        panelDetalleFicha = new PanelDescripcionPieza();
        panelDetalleFicha.setAlignmentX(CENTER_ALIGNMENT);
        panelDetalleFicha.setMaximumSize(new Dimension(430, 300));
        panelDetalleFicha.setMinimumSize(new Dimension(410, 285));
        lateral.add(panelDetalleFicha);
        lateral.add(Box.createRigidArea(new Dimension(0, 6)));
        lateral.add(Box.createVerticalGlue());

        BotonMenu retirar = new BotonMenu("RETIRARSE DE LA PARTIDA");
        retirar.setMaximumSize(new Dimension(430, 44));
        retirar.setAlignmentX(CENTER_ALIGNMENT);
        retirar.setBackground(new Color(30, 30, 34));
        retirar.addActionListener(e -> retirarse());
        lateral.add(retirar);
        return lateral;
    }

    private JLabel crearTituloTarjeta(String texto) {
        JLabel titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 14));
        titulo.setForeground(Tema.DORADO);
        titulo.setBorder(BorderFactory.createEmptyBorder(3, 2, 6, 2));
        return titulo;
    }

    private JPanel crearPie() {
        JPanel pie = new JPanel(new BorderLayout(8, 6));
        pie.setOpaque(false);

        JPanel instruccion = new JPanel(new BorderLayout());
        instruccion.setOpaque(true);
        instruccion.setBackground(new Color(10, 10, 13, 245));
        instruccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 155, 160), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        mensajeInstruccion = Tema.crearSubtitulo("Gira la ruleta para comenzar el turno.", 13);
        mensajeInstruccion.setHorizontalAlignment(SwingConstants.LEFT);
        mensajeInstruccion.setForeground(Color.WHITE);
        instruccion.add(mensajeInstruccion, BorderLayout.CENTER);

        registroEventos = new JTextArea(1, 20);
        registroEventos.setEditable(false);
        registroEventos.setLineWrap(true);
        registroEventos.setWrapStyleWord(true);
        registroEventos.setBackground(new Color(7, 7, 9));
        registroEventos.setForeground(Color.WHITE);
        registroEventos.setCaretColor(Color.WHITE);
        registroEventos.setFont(new Font("SansSerif", Font.PLAIN, 11));
        registroEventos.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        JScrollPane scroll = new JScrollPane(registroEventos);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(90, 90, 96), 1));
        scroll.setPreferredSize(new Dimension(0, 34));

        JPanel barra = new JPanel(new GridLayout(1, 2, 8, 0));
        barra.setOpaque(false);
        BotonMenu estado = new BotonMenu("PARTIDA ACTIVA");
        estado.setEnabled(false);
        BotonMenu salir = new BotonMenu("SALIR AL MENÚ");
        salir.addActionListener(e -> retirarse());
        barra.add(estado);
        barra.add(salir);

        JPanel contenido = new JPanel(new BorderLayout(0, 5));
        contenido.setOpaque(false);
        contenido.add(instruccion, BorderLayout.NORTH);
        contenido.add(scroll, BorderLayout.CENTER);
        contenido.add(barra, BorderLayout.SOUTH);
        pie.add(contenido, BorderLayout.CENTER);
        return pie;
    }

    private void mostrarReglas() {
        String reglas = "VAMPIRE WARGAME — REGLAS CLAVE\n\n"
                + "• Todas las piezas principales se mueven a una casilla al lado vacía.\n"
                + "• El Hombre Lobo puede avanzar hasta 2 casillas vacías.\n"
                + "• El daño resta primero Escudo y después Vida.\n"
                + "• Vampiro: Absorción de sangre, -1 Vida al enemigo y +1 Vida propia.\n"
                + "• Necrómante: Lanza de 2 de daño directo, invocación de Zombie y ataque por Zombie.\n"
                + "• El Zombie no se mueve solo y no participa en la ruleta.\n"
                + "• Si muere un Necrómante, todos sus Zombies mueren.\n"
                + "• Gana quien destruye todas las piezas del bando contrario, incluidos sus Zombies.";
        JOptionPane.showMessageDialog(this, reglas, "Reglas y habilidades", JOptionPane.INFORMATION_MESSAGE);
    }

    private void girarRuleta() {
        if (ruletaEnMovimiento || panelRuleta.isAnimando()) return;
        try {
            final ResultadoRuleta resultadoAccion = estadoPartida.girarRuleta();
            ruletaEnMovimiento = true;
            botonRuleta.setEnabled(false);
            limpiarSeleccion();
            mensajeInstruccion.setText("La ruleta está girando...");
            panelRuleta.animarHasta(resultadoAccion.getTipoSeleccionado(), () -> {
                ruletaEnMovimiento = false;
                registrarEvento(resultadoAccion.getMensaje());
                if (resultadoAccion.isPuedeJugar()) {
                    mensajeInstruccion.setText("Selecciona una pieza " + resultadoAccion.getTipoSeleccionado().getNombre() + " de tu bando y luego el destino.");
                    mostrarPrimeraPiezaHabilitada();
                } else if (resultadoAccion.isPuedeVolverAGirar()) {
                    mensajeInstruccion.setText("Ese tipo ya no está disponible. Puedes volver a girar la ruleta.");
                } else {
                    mensajeInstruccion.setText("No hay piezas disponibles de ese tipo. El turno cambió.");
                }
                actualizarVista();
            });
        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
            actualizarVista();
        }
    }

    private void mostrarPrimeraPiezaHabilitada() {
        TipoPieza categoriaPieza = estadoPartida.getTipoHabilitado();
        if (categoriaPieza == null) return;
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                try {
                    Pieza elementoPieza = estadoPartida.getTablero().getPieza(f, c);
                    if (elementoPieza != null && elementoPieza.getColor() == estadoPartida.getTurnoActual() && elementoPieza.getTipo() == categoriaPieza) {
                        panelDetalleFicha.mostrarPieza(elementoPieza);
                        return;
                    }
                } catch (JuegoException ex) {
                    // Ignorar casillas inválidas: no deben existir dentro del tablero.
                }
            }
        }
    }

    private void seleccionarCasilla(int renglon, int indiceColumna) {
        if (ruletaEnMovimiento || !estadoPartida.isActiva()) return;
        try {
            Pieza elementoPieza = estadoPartida.getTablero().getPieza(renglon, indiceColumna);
            if (renglonOrigen < 0) {
                seleccionarOrigen(renglon, indiceColumna, elementoPieza);
                return;
            }

            Pieza atacante = estadoPartida.getTablero().getPieza(renglonOrigen, indiceColumnaOrigen);
            if (elementoPieza == null) {
                ejecutarAccionCasillaVacia(atacante, renglon, indiceColumna);
            } else if (elementoPieza.getColor() == estadoPartida.getTurnoActual()) {
                throw new JuegoException("La casilla destino contiene una pieza propia.");
            } else {
                ejecutarAtaque(atacante, renglon, indiceColumna);
            }
        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void seleccionarOrigen(int renglon, int indiceColumna, Pieza elementoPieza) throws JuegoException {
        if (elementoPieza == null) throw new JuegoException("La casilla seleccionada está vacía.");
        if (elementoPieza.getColor() != estadoPartida.getTurnoActual()) throw new JuegoException("Debes seleccionar una pieza del jugador que tiene el turno.");
        if (elementoPieza.getTipo() != estadoPartida.getTipoHabilitado()) throw new JuegoException("La ruleta solo permite seleccionar una pieza de tipo " + estadoPartida.getTipoHabilitado().getNombre() + ".");
        renglonOrigen = renglon;
        indiceColumnaOrigen = indiceColumna;
        panelDetalleFicha.mostrarPieza(elementoPieza);
        mensajeInstruccion.setText("Pieza seleccionada: " + elementoPieza.getTipo().getNombre() + ". Selecciona el destino.");
        actualizarTablero();
    }

    private void ejecutarAccionCasillaVacia(Pieza atacante, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        String mensaje;
        if (atacante.getTipo() == TipoPieza.NECROMANTE) {
            String[] opciones = {"Mover", "Invocar Zombie", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "La casilla está vacía. ¿Qué deseas hacer con el Necrómante?", "Acción del Necrómante", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (opcion == 0) mensaje = estadoPartida.moverPieza(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
            else if (opcion == 1) mensaje = estadoPartida.invocarZombie(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
            else return;
        } else {
            mensaje = estadoPartida.moverPieza(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino);
        }
        finalizarAccionVisual(mensaje);
    }

    private void ejecutarAtaque(Pieza atacante, int renglonDestino, int indiceColumnaDestino) throws JuegoException {
        if (atacante.getTipo() == TipoPieza.VAMPIRO) {
            String[] opciones = {"Ataque normal", "Absorber sangre", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "Elige el tipo de ataque:", "Vampiro", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (opcion == 0) finalizarAccionVisual(estadoPartida.atacarNormal(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino));
            else if (opcion == 1) finalizarAccionVisual(estadoPartida.absorberSangre(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino));
            return;
        }
        if (atacante.getTipo() == TipoPieza.NECROMANTE) {
            String[] opciones = {"Ataque normal", "Lanzar lanza", "Ataque por Zombie", "Cancelar"};
            int opcion = JOptionPane.showOptionDialog(this, "Elige el ataque del Necrómante:", "Necrómante", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            if (opcion == 0) finalizarAccionVisual(estadoPartida.atacarNormal(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino));
            else if (opcion == 1) finalizarAccionVisual(estadoPartida.lanzarLanza(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino));
            else if (opcion == 2) atacarPorZombie(renglonDestino, indiceColumnaDestino);
            return;
        }
        finalizarAccionVisual(estadoPartida.atacarNormal(renglonOrigen, indiceColumnaOrigen, renglonDestino, indiceColumnaDestino));
    }

    private void atacarPorZombie(int renglonObjetivo, int indiceColumnaObjetivo) throws JuegoException {
        int[][] coordenadas = new int[Tablero.TAMANO * Tablero.TAMANO][2];
        String[] textosTemporales = new String[Tablero.TAMANO * Tablero.TAMANO];
        int cantidadDanio = 0;
        for (int renglon = 0; renglon < Tablero.TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < Tablero.TAMANO; indiceColumna++) {
                Pieza elementoPieza = estadoPartida.getTablero().getPieza(renglon, indiceColumna);
                if (elementoPieza != null && elementoPieza.getTipo() == TipoPieza.ZOMBIE && elementoPieza.getColor() == estadoPartida.getTurnoActual()) {
                    if (elementoPieza.estaAlLado(renglon, indiceColumna, renglonObjetivo, indiceColumnaObjetivo)) {
                        coordenadas[cantidadDanio][0] = renglon;
                        coordenadas[cantidadDanio][1] = indiceColumna;
                        textosTemporales[cantidadDanio] = "Zombie en [" + renglon + ", " + indiceColumna + "]";
                        cantidadDanio++;
                    }
                }
            }
        }
        if (cantidadDanio == 0) throw new JuegoException("No tienes un Zombie propio al lado a ese enemigo.");
        String[] opciones = new String[cantidadDanio];
        System.arraycopy(textosTemporales, 0, opciones, 0, cantidadDanio);
        String seleccionado = (String) JOptionPane.showInputDialog(this, "Selecciona el Zombie que ejecutará el ataque:", "Ataque por Zombie", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (seleccionado == null) return;
        int indice = 0;
        for (int i = 0; i < opciones.length; i++) if (opciones[i].equals(seleccionado)) { indice = i; break; }
        String mensaje = estadoPartida.atacarPorZombie(renglonOrigen, indiceColumnaOrigen, coordenadas[indice][0], coordenadas[indice][1], renglonObjetivo, indiceColumnaObjetivo);
        finalizarAccionVisual(mensaje);
    }

    private void finalizarAccionVisual(String mensaje) {
        registrarEvento(mensaje);
        limpiarSeleccion();
        actualizarVista();
        if (!estadoPartida.isActiva()) {
            registrarEvento(estadoPartida.getMensajeFinal());
            JOptionPane.showMessageDialog(this, estadoPartida.getMensajeFinal(), "Partida finalizada", JOptionPane.INFORMATION_MESSAGE);
            volverAlMenu();
        } else {
            mensajeInstruccion.setText("Turno completado. El siguiente jugador debe girar la ruleta.");
        }
    }

    private void retirarse() {
        if (!estadoPartida.isActiva()) {
            volverAlMenu();
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas retirarte? El jugador contrario será declarado ganador.", "Retirarse de la partida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (opcion != JOptionPane.YES_OPTION) return;
        try {
            String mensaje = estadoPartida.retirarse(estadoPartida.getJugadorTurnoActual());
            registrarEvento(mensaje);
            JOptionPane.showMessageDialog(this, mensaje, "Partida finalizada", JOptionPane.INFORMATION_MESSAGE);
            volverAlMenu();
        } catch (JuegoException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void volverAlMenu() {
        new VentanaPrincipal(sistema).setVisible(true);
        dispose();
    }

    private void limpiarSeleccion() {
        renglonOrigen = -1;
        indiceColumnaOrigen = -1;
    }

    private void actualizarVista() {
        actualizarTablero();
        Jugador turno = estadoPartida.getJugadorTurnoActual();
        etiquetaTurno.setText("TURNO: " + turno.getUsuario() + "  •  EQUIPO " + estadoPartida.getTurnoActual().toString());

        TipoPieza habilitado = estadoPartida.getTipoHabilitado();
        textoTipoSeleccionado.setText("Tipo habilitado: " + (habilitado == null ? "ninguno" : habilitado.getNombre()));
        contadorGiros.setText("Giros: " + estadoPartida.getGirosUsados() + "/" + estadoPartida.getGirosPermitidos());

        boolean puedeGirar = estadoPartida.isActiva() && !ruletaEnMovimiento && habilitado == null && estadoPartida.getGirosUsados() < estadoPartida.getGirosPermitidos();
        botonRuleta.setEnabled(puedeGirar);

        if (habilitado == null) {
            panelDetalleFicha.ocultar();
        }
    }

    private void actualizarTablero() {
        for (int renglon = 0; renglon < Tablero.TAMANO; renglon++) {
            for (int indiceColumna = 0; indiceColumna < Tablero.TAMANO; indiceColumna++) {
                try {
                    Pieza elementoPieza = estadoPartida.getTablero().getPieza(renglon, indiceColumna);
                    boolean seleccionada = renglon == renglonOrigen && indiceColumna == indiceColumnaOrigen;
                    botonesTablero[renglon][indiceColumna].actualizar(elementoPieza, seleccionada);
                } catch (JuegoException ex) {
                    botonesTablero[renglon][indiceColumna].actualizar(null, false);
                }
            }
        }
    }

    private void registrarEvento(String texto) {
        if (texto == null || texto.trim().isEmpty()) return;
        registroEventos.append("• " + texto + "\n");
        registroEventos.setCaretPosition(registroEventos.getDocument().getLength());
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Acción no válida", JOptionPane.WARNING_MESSAGE);
    }
}
