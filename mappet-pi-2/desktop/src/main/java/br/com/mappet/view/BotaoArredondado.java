package br.com.mappet.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/** Botão em formato de pílula, como os botões do site. */
public class BotaoArredondado extends JButton {

    private final Color fundo;
    private final Color fundoHover;
    private final Color borda;

    public BotaoArredondado(String texto, Color fundo, Color fundoHover, Color corTexto, Color borda) {
        super(texto);
        this.fundo = fundo;
        this.fundoHover = fundoHover;
        this.borda = borda;
        setFont(Estilo.BOTAO);
        setForeground(corTexto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Estilo.suave(g);
        int w = getWidth(), h = getHeight();
        if (borda == null) {                                  // sombra leve nos botões laranja
            g2.setColor(new Color(fundo.getRed(), fundo.getGreen(), fundo.getBlue(), 60));
            g2.fillRoundRect(1, 3, w - 2, h - 3, h, h);
        }
        g2.setColor(getModel().isRollover() || getModel().isPressed() ? fundoHover : fundo);
        g2.fillRoundRect(0, 0, w - 1, h - (borda == null ? 3 : 1), h, h);
        if (borda != null) {
            g2.setColor(borda);
            g2.drawRoundRect(0, 0, w - 1, h - 1, h, h);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
