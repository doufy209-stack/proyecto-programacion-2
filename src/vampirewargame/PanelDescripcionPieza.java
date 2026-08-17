package vampirewargame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PanelDescripcionPieza extends JPanel {

    private final JLabel titulo;
    private final JLabel equipo;
    private final JLabel imagen;
    private final JLabel puntosVida;
    private final JLabel puntosEscudo;
    private final JLabel dano;
    private final JTextArea descripcion;

    public PanelDescripcionPieza() {
        setLayout(new BorderLayout(8, 8));
        setOpaque(true);
        setPreferredSize(new Dimension(430, 300));
        setMinimumSize(new Dimension(410, 285));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(110, 110, 116), 1),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));

        imagen = new JLabel();
        imagen.setHorizontalAlignment(SwingConstants.CENTER);
        imagen.setVerticalAlignment(SwingConstants.CENTER);
        imagen.setPreferredSize(new Dimension(66, 66));

        titulo = new JLabel("PERSONAJE", SwingConstants.LEFT);
        titulo.setFont(new Font("Serif", Font.BOLD, 19));

        equipo = new JLabel("Gira la ruleta para seleccionar uno", SwingConstants.LEFT);
        equipo.setFont(new Font("SansSerif", Font.BOLD, 11));

        JPanel encabezadoTexto = new JPanel(new BorderLayout(0, 6));
        encabezadoTexto.setOpaque(false);
        encabezadoTexto.add(titulo, BorderLayout.NORTH);
        encabezadoTexto.add(equipo, BorderLayout.CENTER);

        JPanel encabezado = new JPanel(new BorderLayout(9, 0));
        encabezado.setOpaque(false);
        encabezado.add(imagen, BorderLayout.WEST);
        encabezado.add(encabezadoTexto, BorderLayout.CENTER);

        JPanel atributos = new JPanel(new GridLayout(1, 3, 8, 0));
        atributos.setOpaque(false);
        puntosVida = crearAtributo("VIDA");
        puntosEscudo = crearAtributo("ESCUDO");
        dano = crearAtributo("DAÑO");
        atributos.add(puntosVida);
        atributos.add(puntosEscudo);
        atributos.add(dano);

        descripcion = new JTextArea();
        descripcion.setEditable(false);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descripcion.setFocusable(false);
        descripcion.setMargin(new Insets(8, 9, 8, 9));
        descripcion.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 106), 1));

        JScrollPane scroll = new JScrollPane(descripcion);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(0, 185));
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setOpaque(false);
        centro.add(atributos, BorderLayout.NORTH);
        centro.add(scroll, BorderLayout.CENTER);

        add(encabezado, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
        ocultar();
    }

    private JLabel crearAtributo(String nombre) {
        JLabel etiqueta = new JLabel("<html><center>" + nombre + "<br>—</center></html>", SwingConstants.CENTER);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 12));
        etiqueta.setOpaque(true);
        etiqueta.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 106), 1));
        return etiqueta;
    }

    public void mostrarPieza(Pieza elementoPieza) {
        if (elementoPieza == null) { ocultar(); return; }
        boolean blanco = elementoPieza.getColor() == ColorBando.BLANCO;
        Color fondo = blanco ? Tema.BLANCO_CARTA : Tema.NEGRO_CARTA;
        Color texto = blanco ? new Color(20, 20, 22) : Color.WHITE;
        Color secundario = blanco ? new Color(70, 70, 76) : new Color(215, 215, 220);
        Color borde = new Color(120, 120, 126);

        setBackground(fondo);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde, 2, true),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        titulo.setForeground(texto);
        equipo.setForeground(secundario);
        titulo.setText(elementoPieza.getTipo().getNombre().toUpperCase());
        equipo.setText("EQUIPO " + (blanco ? "BLANCO" : "NEGRO"));

        puntosVida.setText("<html><center>VIDA<br>" + elementoPieza.getVida() + "/" + elementoPieza.getVidaMaxima() + "</center></html>");
        puntosEscudo.setText("<html><center>ESCUDO<br>" + elementoPieza.getEscudo() + "</center></html>");
        dano.setText("<html><center>DAÑO<br>" + elementoPieza.getAtaque() + "</center></html>");
        Color fondoAtributo = blanco ? new Color(226, 226, 229) : new Color(34, 34, 38);
        for (JLabel etiqueta : new JLabel[]{puntosVida, puntosEscudo, dano}) {
            etiqueta.setForeground(texto);
            etiqueta.setBackground(fondoAtributo);
        }

        String archivo = elementoPieza.getTipo() == TipoPieza.HOMBRE_LOBO ? "lobo.png"
                : elementoPieza.getTipo() == TipoPieza.VAMPIRO ? "vampiro.png"
                : elementoPieza.getTipo() == TipoPieza.NECROMANTE ? "necromante.png" : "zombie.png";
        URL url = getClass().getResource("/vampirewargame/recursos/" + archivo);
        if (url != null) {
            Image original = new ImageIcon(url).getImage();
            imagen.setIcon(new ImageIcon(original.getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
        } else imagen.setIcon(null);

        descripcion.setForeground(texto);
        descripcion.setBackground(blanco ? new Color(248, 248, 250) : new Color(11, 11, 14));
        descripcion.setText(crearDescripcionCompleta(elementoPieza));
        descripcion.setCaretPosition(0);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void ocultar() {
        setBackground(Tema.PANEL);
        titulo.setForeground(Tema.TEXTO);
        equipo.setForeground(Tema.TEXTO_SECUNDARIO);
        titulo.setText("PERSONAJE");
        equipo.setText("Gira la ruleta para seleccionar uno");
        imagen.setIcon(null);
        puntosVida.setText("<html><center>VIDA<br>—</center></html>");
        puntosEscudo.setText("<html><center>ESCUDO<br>—</center></html>");
        dano.setText("<html><center>DAÑO<br>—</center></html>");
        puntosVida.setForeground(Tema.TEXTO); puntosEscudo.setForeground(Tema.TEXTO); dano.setForeground(Tema.TEXTO);
        puntosVida.setBackground(Tema.PANEL_CLARO); puntosEscudo.setBackground(Tema.PANEL_CLARO); dano.setBackground(Tema.PANEL_CLARO);
        descripcion.setForeground(Tema.TEXTO);
        descripcion.setBackground(Tema.FONDO_SECUNDARIO);
        descripcion.setText("La información completa del personaje aparecerá aquí después de girar la ruleta.\n\nTambién se muestran aquí sus atributos actuales y todas sus habilidades especiales.");
        descripcion.setCaretPosition(0);
        setVisible(true);
        revalidate();
        repaint();
    }

    private String crearDescripcionCompleta(Pieza elementoPieza) {
        if (elementoPieza.getTipo() == TipoPieza.VAMPIRO) {
            return "HABILIDAD ESPECIAL — ABSORCIÓN DE SANGRE\n\n"
                    + "En lugar de ejecutar un ataque normal, el Vampiro puede absorber sangre de una pieza enemiga al lado. Esta habilidad resta 1 punto de vida al enemigo y recupera 1 punto de vida para el propio Vampiro.\n\n"
                    + "ATAQUE NORMAL\nPuede atacar a una pieza enemiga al lado. Su ataque es de 3 puntos y el daño consume primero el Escudo y luego la Vida.\n\n"
                    + "MOVIMIENTO\nPuede desplazarse 1 casilla en cualquier dirección hacia una casilla vacía y al lado.\n\n"
                    + "ATRIBUTOS ACTUALES\nAtaque: " + elementoPieza.getAtaque() + " | Vida: " + elementoPieza.getVida() + "/" + elementoPieza.getVidaMaxima() + " | Escudo: " + elementoPieza.getEscudo();
        }
        if (elementoPieza.getTipo() == TipoPieza.HOMBRE_LOBO) {
            return "HABILIDAD ESPECIAL — DESPLAZAMIENTO EXTENDIDO\n\n"
                    + "El Hombre Lobo puede desplazarse hasta 2 casillas vacías en cualquier dirección. Si avanza 2 casillas, ambas posiciones deben estar libres.\n\n"
                    + "ATAQUE NORMAL\nPuede atacar a una pieza enemiga al lado. Su ataque es de 5 puntos y el daño consume primero el Escudo y luego la Vida.\n\n"
                    + "MOVIMIENTO\nPuede moverse 1 o 2 casillas vacías en cualquier dirección, según lo permita el tablero.\n\n"
                    + "ATRIBUTOS ACTUALES\nAtaque: " + elementoPieza.getAtaque() + " | Vida: " + elementoPieza.getVida() + "/" + elementoPieza.getVidaMaxima() + " | Escudo: " + elementoPieza.getEscudo();
        }
        if (elementoPieza.getTipo() == TipoPieza.NECROMANTE) {
            return "HABILIDADES ESPECIALES DEL NECRÓMANTE\n\n"
                    + "1. LANZA A DISTANCIA\nAtaca a un enemigo ubicado a 2 casillas de distancia, en línea horizontal o vertical y sin obstrucciones. Hace 2 puntos directamente a la Vida e ignora por completo el Escudo.\n\n"
                    + "2. INVOCACIÓN DE ZOMBIE\nPuede conjurar un Zombie en cualquier casilla vacía del tablero.\n\n"
                    + "3. ATAQUE A TRAVÉS DE ZOMBIE\nPuede ordenar a un Zombie propio atacar a un enemigo que no esté a 1 ni a 2 casillas del Necrómante, siempre que ese enemigo esté al lado al Zombie. El ataque hace 1 punto de daño.\n\n"
                    + "REGLA ESPECIAL\nSi el Necrómante es destruido, todos los Zombies de su mismo equipo son destruidos inmediatamente.\n\n"
                    + "ATAQUE NORMAL\nPuede atacar a una pieza enemiga al lado por 4 puntos.\n\n"
                    + "ATRIBUTOS ACTUALES\nAtaque: " + elementoPieza.getAtaque() + " | Vida: " + elementoPieza.getVida() + "/" + elementoPieza.getVidaMaxima() + " | Escudo: " + elementoPieza.getEscudo();
        }
        return "FUNCIÓN DEL ZOMBIE\n\n"
                + "El Zombie no puede desplazarse por sí mismo. No participa en la selección aleatoria de la ruleta y solo puede actuar en combate cuando el Necrómante de su equipo ordena un ataque.\n\n"
                + "REGLA ESPECIAL\nSi el Necrómante de su equipo muere, este Zombie también es destruido.\n\n"
                + "ATRIBUTOS ACTUALES\nAtaque: " + elementoPieza.getAtaque() + " | Vida: " + elementoPieza.getVida() + "/" + elementoPieza.getVidaMaxima() + " | Escudo: " + elementoPieza.getEscudo();
    }
}
