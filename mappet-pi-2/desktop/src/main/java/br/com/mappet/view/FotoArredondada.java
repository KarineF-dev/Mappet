package br.com.mappet.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Foto do pet com cantos arredondados, recortada para preencher o espaço sem distorcer,
 * com um selo opcional no canto (PERDIDO, ENCONTRADO).
 */
public class FotoArredondada extends JComponent {

    private Image imagem;
    private final int raio;
    private String selo;
    private Color seloFundo, seloCor;
    private String textoVazio = "sem foto";

    public FotoArredondada(String caminho, int largura, int altura, int raio) {
        this.raio = raio;
        setPreferredSize(new Dimension(largura, altura));
        setImagem(caminho);
    }

    public void setImagem(String caminho) {
        imagem = null;
        if (caminho != null) {
            try {
                imagem = ImageIO.read(new File(caminho));
            } catch (Exception e) {
                imagem = null;
            }
        }
        repaint();
    }

    public void setImagem(Image img) { imagem = img; repaint(); }

    public void setTextoVazio(String texto) { textoVazio = texto; }

    public boolean temImagem() { return imagem != null; }

    public void setSelo(String texto, Color fundo, Color cor) {
        selo = texto;
        seloFundo = fundo;
        seloCor = cor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;
        BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D b = Estilo.suave(buf.getGraphics());
        b.setColor(Color.WHITE);
        b.fillRoundRect(0, 0, w, h, raio, raio);
        b.setComposite(AlphaComposite.SrcIn);
        if (imagem != null) {
            int iw = imagem.getWidth(null), ih = imagem.getHeight(null);
            double escala = Math.max((double) w / iw, (double) h / ih);
            int dw = (int) Math.ceil(iw * escala), dh = (int) Math.ceil(ih * escala);
            b.drawImage(imagem, (w - dw) / 2, (h - dh) / 2, dw, dh, null);
        } else {
            b.setColor(Estilo.AZUL_CLARO);
            b.fillRect(0, 0, w, h);
        }
        b.dispose();

        Graphics2D g2 = Estilo.suave(g);
        g2.drawImage(buf, 0, 0, null);
        if (imagem == null && textoVazio != null) {
            g2.setFont(Estilo.NEGRITO);
            g2.setColor(Estilo.CINZA);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(textoVazio, (w - fm.stringWidth(textoVazio)) / 2, h / 2 + fm.getAscent() / 2 - 2);
        }
        if (selo != null) {
            g2.setFont(Estilo.CHAMADA);
            FontMetrics fm = g2.getFontMetrics();
            int sw = fm.stringWidth(selo) + 22, sh = 24;
            g2.setColor(seloFundo);
            g2.fillRoundRect(10, 10, sw, sh, sh, sh);
            g2.setColor(seloCor);
            g2.drawString(selo, 21, 10 + sh / 2 + fm.getAscent() / 2 - 2);
        }
        g2.dispose();
    }
}
