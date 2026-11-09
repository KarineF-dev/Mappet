package br.com.mappet.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/** Painel com cantos arredondados, usado nos cartões, campos e áreas da tela. */
public class PainelArredondado extends JPanel {

    private final int raio;
    private Color fundo;
    private final Color borda;
    private boolean sombra;

    public PainelArredondado(int raio, Color fundo, Color borda) {
        this.raio = raio;
        this.fundo = fundo;
        this.borda = borda;
        setOpaque(false);
    }

    public void setSombra(boolean sombra) { this.sombra = sombra; }

    public void setFundo(Color fundo) { this.fundo = fundo; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Estilo.suave(g);
        int w = getWidth(), h = getHeight();
        int s = sombra ? 4 : 0;
        if (sombra) {
            for (int i = 0; i < 4; i++) {
                g2.setColor(new Color(40, 86, 117, 10 - i * 2));
                g2.fillRoundRect(i, i + 2, w - i * 2, h - i * 2 - 2, raio + 4, raio + 4);
            }
        }
        g2.setColor(fundo);
        g2.fillRoundRect(0, 0, w - 1, h - 1 - s, raio, raio);
        if (borda != null) {
            g2.setColor(borda);
            g2.drawRoundRect(0, 0, w - 1, h - 1 - s, raio, raio);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}
