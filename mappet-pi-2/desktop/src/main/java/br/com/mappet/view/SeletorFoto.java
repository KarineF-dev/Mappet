package br.com.mappet.view;

import br.com.mappet.util.FotoUtil;
import br.com.mappet.util.ValidacaoException;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Campo de foto com prévia (UC07), no estilo do site: caixa tracejada "Escolha uma foto do seu pet".
 * A foto escolhida fica só na memória: ela só é copiada para a pasta "fotos" quando o usuário publica.
 */
public class SeletorFoto extends JPanel {

    private File arquivo;
    private final FotoArredondada previa = new FotoArredondada(null, 300, 200, 18);
    private final JComponent vazio = new CaixaTracejada();

    public SeletorFoto() {
        setLayout(new BorderLayout(0, 10));
        setOpaque(false);
        add(vazio, BorderLayout.CENTER);
        MouseAdapter clique = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { escolher(); }
        };
        vazio.addMouseListener(clique);
        previa.addMouseListener(clique);
        previa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JButton escolher = Estilo.botaoSecundario("Escolher foto (JPG ou PNG, até 5 MB)");
        escolher.addActionListener(e -> escolher());
        add(escolher, BorderLayout.SOUTH);
    }

    private void escolher() {
        JFileChooser seletor = new JFileChooser();                       // UC07 passo 2
        seletor.setFileFilter(new FileNameExtensionFilter("Imagens JPG ou PNG", "jpg", "jpeg", "png"));
        if (seletor.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File escolhido = seletor.getSelectedFile();
        try {
            FotoUtil.validar(escolhido);                                 // UC07 passo 4
        } catch (ValidacaoException ex) {                                // UC07 E1
            Estilo.alertaErros(this, ex.getErros());
            return;
        }
        definirArquivo(escolhido);                                       // UC07 A1: troca a foto anterior
    }

    /** Mostra a prévia da foto escolhida (UC07 passo 5). */
    public void definirArquivo(File escolhido) {
        arquivo = escolhido;
        previa.setImagem(escolhido.getPath());
        remove(vazio);
        remove(previa);
        add(previa, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public File getArquivo() { return arquivo; }

    /** Caixa tracejada com o ícone de câmera, antes de escolher a foto. */
    private static class CaixaTracejada extends JComponent {
        CaixaTracejada() {
            setPreferredSize(new Dimension(300, 200));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Estilo.suave(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(Estilo.CAMPO);
            g2.fillRoundRect(1, 1, w - 3, h - 3, 18, 18);
            g2.setColor(Estilo.AZUL_CEU);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1f, new float[] {6f, 5f}, 0f));
            g2.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);
            int cx = w / 2, cy = h / 2 - 14;
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(Estilo.AZUL_CLARO);
            g2.fillOval(cx - 22, cy - 22, 44, 44);
            g2.setColor(Estilo.AZUL_MEDIO);
            g2.fillRoundRect(cx - 11, cy - 6, 22, 15, 5, 5);
            g2.fillRect(cx - 5, cy - 9, 10, 4);
            g2.setColor(Estilo.AZUL_CLARO);
            g2.fillOval(cx - 5, cy - 3, 10, 10);
            g2.setColor(Estilo.AZUL_MEDIO);
            g2.fillOval(cx - 3, cy - 1, 6, 6);
            g2.setFont(Estilo.NEGRITO);
            g2.setColor(Estilo.TEXTO);
            String s = "Escolha uma foto do seu pet";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, cx - fm.stringWidth(s) / 2, cy + 46);
            g2.dispose();
        }
    }
}
