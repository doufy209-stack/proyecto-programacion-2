package vampirewargame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class BotonMenu extends JButton {

    private boolean claro;

    public BotonMenu(String texto) {
        super(texto);
        setFocusPainted(false);
        setBorderPainted(true);
        setContentAreaFilled(true);
        setOpaque(true);
        setBackground(Tema.FONDO_SECUNDARIO);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(155, 155, 160), 1, true),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        setFont(new Font("SansSerif", Font.BOLD, 13));
        setPreferredSize(new Dimension(300, 48));
        setMinimumSize(new Dimension(160, 44));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(CENTER);
        claro = false;
        setMargin(new Insets(8, 18, 8, 18));
    }

    public void setEstiloClaro(boolean claro) {
        this.claro = claro;
        if (claro) {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.BLACK, 1),
                    BorderFactory.createEmptyBorder(8, 14, 8, 14)
            ));
        }
        repaint();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        if (claro) {
            if (getModel().isPressed()) {
                setBackground(new Color(220, 220, 220));
            } else if (getModel().isRollover()) {
                setBackground(new Color(238, 238, 238));
            } else if (isEnabled()) {
                setBackground(Color.WHITE);
            }
        } else if (getModel().isPressed()) {
            setBackground(new Color(48, 48, 52));
        } else if (getModel().isRollover()) {
            setBackground(new Color(82, 82, 88));
        } else if (isEnabled()) {
            setBackground(Tema.FONDO_SECUNDARIO);
        }
        super.paintComponent(g);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (claro) {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 1),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            } else {
                setBackground(Tema.FONDO_SECUNDARIO);
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(155, 155, 160), 1, true),
                        BorderFactory.createEmptyBorder(4, 12, 4, 12)
                ));
            }
        } else {
            setBackground(new Color(40, 40, 44));
            setForeground(new Color(230, 230, 234));
            setBorder(BorderFactory.createLineBorder(new Color(105, 105, 110), 1, true));
        }
    }
}
