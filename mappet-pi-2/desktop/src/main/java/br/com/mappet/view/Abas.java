package br.com.mappet.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;

/**
 * Abas "Meu pet está perdido", "Encontrei um pet" e "Colocar para adoção",
 * iguais às da área do usuário do site. Cada aba abre o formulário correspondente.
 */
public class Abas extends PainelArredondado {

    public static final int PERDIDO = 0, ENCONTRADO = 1, ADOCAO = 2;

    public Abas(JanelaPrincipal janela, int ativa) {
        super(18, Estilo.AZUL_CLARO, null);
        setLayout(new GridLayout(1, 3, 6, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        String[] nomes = {"Meu pet está perdido", "Encontrei um pet", "Colocar para adoção"};
        Runnable[] acoes = {
            () -> janela.mostrarPublicar(br.com.mappet.model.PublicacaoPet.PERDIDO),     // UC05
            () -> janela.mostrarPublicar(br.com.mappet.model.PublicacaoPet.ENCONTRADO),  // UC06
            janela::mostrarDisponibilizar                                                // UC10
        };
        for (int i = 0; i < 3; i++) {
            final int n = i;
            JButton aba = new JButton(nomes[i]) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = Estilo.suave(g);
                    if (n == ativa) {
                        g2.setColor(new Color(40, 86, 117, 18));
                        g2.fillRoundRect(0, 2, getWidth(), getHeight() - 2, 14, 14);
                        g2.setColor(Color.WHITE);
                        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 14, 14);
                    }
                    g2.setFont(getFont());
                    g2.setColor(n == ativa ? Estilo.TITULO_COR : Estilo.CINZA);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                                  (getHeight() - 2) / 2 + fm.getAscent() / 2 - 2);
                    g2.dispose();
                }
            };
            aba.setFont(Estilo.BOTAO.deriveFont(13.5f));
            aba.setContentAreaFilled(false);
            aba.setBorderPainted(false);
            aba.setFocusPainted(false);
            aba.setOpaque(false);
            aba.setPreferredSize(new java.awt.Dimension(200, 42));
            aba.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            if (i != ativa) aba.addActionListener(e -> acoes[n].run());
            add(aba);
        }
    }
}
