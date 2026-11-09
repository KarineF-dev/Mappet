package br.com.mappet.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Janela de aviso no estilo do site (como o "Ops! Faltam informações"), no lugar das caixas padrão do Java. */
public class Dialogo {

    public enum Tipo { INFO, ALERTA, ERRO }

    public static void mostrar(Component pai, Tipo tipo, String titulo, String mensagemHtml) {
        JLabel texto = Estilo.rotulo(mensagemHtml, Estilo.NORMAL, Estilo.TEXTO);
        mostrar(pai, tipo, titulo, texto, "Entendi");
    }

    /** Mostra um aviso com um conteúdo qualquer (usado no contato do acolhedor, UC11). */
    public static void mostrar(Component pai, Tipo tipo, String titulo, JComponent conteudo, String textoBotao) {
        Window dono = pai == null ? null : SwingUtilities.getWindowAncestor(pai);
        JDialog d = new JDialog(dono, "MAPPET", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        d.setUndecorated(true);
        GraphicsDevice tela = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        boolean transparente = tela.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
        if (transparente) d.setBackground(new Color(0, 0, 0, 0));

        PainelArredondado cartao = new PainelArredondado(transparente ? 26 : 0, Color.WHITE, Estilo.BORDA);
        cartao.setLayout(new BorderLayout());
        cartao.setBorder(BorderFactory.createEmptyBorder(26, 30, 24, 30));

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        Icone icone = new Icone(tipo);
        icone.setAlignmentX(Component.CENTER_ALIGNMENT);
        corpo.add(icone);
        corpo.add(Box.createVerticalStrut(10));
        JLabel tit = Estilo.titulo(titulo, 34f, Estilo.TITULO_COR);
        tit.setAlignmentX(Component.CENTER_ALIGNMENT);
        corpo.add(tit);
        corpo.add(Box.createVerticalStrut(8));
        conteudo.setAlignmentX(Component.CENTER_ALIGNMENT);
        corpo.add(conteudo);
        corpo.add(Box.createVerticalStrut(20));
        cartao.add(corpo, BorderLayout.CENTER);

        JButton ok = Estilo.botaoPrimario(textoBotao);
        ok.addActionListener(e -> d.dispose());
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        rodape.setOpaque(false);
        ok.setPreferredSize(new Dimension(220, 46));
        rodape.add(ok);
        cartao.add(rodape, BorderLayout.SOUTH);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setOpaque(false);
        raiz.add(cartao);
        d.setContentPane(raiz);
        d.getRootPane().setOpaque(false);
        d.getRootPane().setDefaultButton(ok);
        d.pack();
        d.setSize(Math.max(d.getWidth(), 440), d.getHeight());
        d.setLocationRelativeTo(dono);
        d.setVisible(true);
    }

    /** Círculo colorido com "!" ou "i" no topo do aviso. */
    static class Icone extends JComponent {
        private final Tipo tipo;

        Icone(Tipo tipo) {
            this.tipo = tipo;
            setPreferredSize(new Dimension(56, 56));
            setMaximumSize(new Dimension(56, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Estilo.suave(g);
            Color fundo = tipo == Tipo.INFO ? Estilo.AZUL_CLARO : Estilo.LARANJA_CLARO;
            Color cor = tipo == Tipo.INFO ? Estilo.AZUL_MEDIO : (tipo == Tipo.ERRO ? Estilo.VERMELHO : Estilo.LARANJA);
            g2.setColor(fundo);
            g2.fillOval(0, 0, 56, 56);
            g2.setColor(cor);
            g2.fillOval(12, 12, 32, 32);
            g2.setColor(Color.WHITE);
            Font f = Estilo.BOTAO.deriveFont(20f);
            g2.setFont(f);
            String s = tipo == Tipo.INFO ? "i" : "!";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(s, 28 - fm.stringWidth(s) / 2, 28 + fm.getAscent() / 2 - 3);
            g2.dispose();
        }
    }
}
