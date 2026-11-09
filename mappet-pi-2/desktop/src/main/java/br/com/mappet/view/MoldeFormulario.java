package br.com.mappet.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Molde visual das telas de cadastro de pet (UC05, UC06 e UC10), igual à área do usuário do site:
 * cartão "Vamos começar?", abas de situação, foto à esquerda, campos à direita e o botão no rodapé.
 */
final class MoldeFormulario {

    private MoldeFormulario() { }

    static JPanel montar(JanelaPrincipal janela, int abaAtiva, String subtitulo,
                         SeletorFoto foto, JComponent campos, JButton publicar) {
        JPanel tela = new JPanel(new BorderLayout());
        tela.setBackground(Estilo.FUNDO);

        PainelArredondado cartao = Estilo.cartao(28);
        cartao.setLayout(new BorderLayout(0, 18));
        cartao.setBorder(BorderFactory.createEmptyBorder(26, 34, 26, 34));

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        JLabel chamada = Estilo.chamada("Cadastrar pet");
        JLabel titulo = Estilo.titulo("Vamos começar?", 50f, Estilo.AZUL_MEDIO);
        JLabel sub = Estilo.rotulo(subtitulo, Estilo.NORMAL, Estilo.CINZA);
        Abas abas = new Abas(janela, abaAtiva);
        for (JComponent c : new JComponent[] {chamada, titulo, sub, abas}) c.setAlignmentX(Component.LEFT_ALIGNMENT);
        abas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        topo.add(chamada);
        topo.add(titulo);
        topo.add(sub);
        topo.add(Box.createVerticalStrut(16));
        topo.add(abas);
        cartao.add(topo, BorderLayout.NORTH);

        JPanel corpo = new JPanel(new GridBagLayout());
        corpo.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 30);
        JPanel ladoFoto = new JPanel(new BorderLayout(0, 8));
        ladoFoto.setOpaque(false);
        ladoFoto.add(Estilo.rotulo("Foto do pet", Estilo.ROTULO, Estilo.TEXTO), BorderLayout.NORTH);
        ladoFoto.add(foto, BorderLayout.CENTER);
        ladoFoto.setPreferredSize(new Dimension(300, 300));
        corpo.add(ladoFoto, c);
        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(0, 0, 0, 0);
        corpo.add(campos, c);
        cartao.add(corpo, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rodape.setOpaque(false);
        publicar.setPreferredSize(new Dimension(230, 50));
        rodape.add(publicar);
        cartao.add(rodape, BorderLayout.SOUTH);

        JPanel margem = new JPanel(new BorderLayout());
        margem.setOpaque(false);
        margem.setBorder(BorderFactory.createEmptyBorder(24, 48, 24, 48));
        margem.add(cartao, BorderLayout.NORTH);
        tela.add(Estilo.rolagem(margem), BorderLayout.CENTER);
        return tela;
    }

    /** Coluna de campos com BoxLayout. */
    static JPanel coluna(JComponent... itens) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        for (JComponent i : itens) {
            i.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(i);
        }
        return p;
    }

    /** Dois campos lado a lado. */
    static JPanel dupla(JComponent a, JComponent b) {
        JPanel p = new JPanel(new java.awt.GridLayout(1, 2, 18, 0));
        p.setOpaque(false);
        p.add(a);
        p.add(b);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        return p;
    }
}
