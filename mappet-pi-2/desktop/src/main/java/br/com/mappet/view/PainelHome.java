package br.com.mappet.view;

import br.com.mappet.controller.PublicacaoController;
import br.com.mappet.controller.UsuarioController;
import br.com.mappet.model.Especie;
import br.com.mappet.model.PublicacaoPet;
import br.com.mappet.util.Sessao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Home: pets publicados (UC03) com filtros rápidos por bairro e espécie (UC04). */
public class PainelHome extends JPanel {

    private final JanelaPrincipal janela;
    private final PublicacaoController controller = new PublicacaoController();
    private final JTextField bairro = new JTextField();
    private JComboBox<Especie> especie;
    private final JPanel grade = new JPanel(new GridLayout(0, 4, 18, 18));
    private final JLabel mensagem = Estilo.rotulo(" ", Estilo.NEGRITO, Estilo.CINZA);

    public PainelHome(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);

        JButton adocao = Estilo.botaoSecundario("Adoção");
        adocao.addActionListener(e -> janela.mostrarAdocao());          // UC09
        JButton sair = Estilo.botaoPrimario("Sair");
        sair.addActionListener(e -> {                                   // UC02 A2
            new UsuarioController().sair();
            janela.mostrarLogin();
        });
        add(Estilo.cabecalho(null, rotuloUsuario(), adocao, sair), BorderLayout.NORTH);

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(BorderFactory.createEmptyBorder(22, 40, 30, 40));
        JPanel b = banner();
        JPanel f = filtros();
        for (JPanel p : new JPanel[] {b, f}) p.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(b);
        corpo.add(Box.createVerticalStrut(18));
        corpo.add(f);
        corpo.add(Box.createVerticalStrut(18));

        JLabel titulo = Estilo.titulo("Pets publicados", 40f, Estilo.TITULO_COR);
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        mensagem.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(titulo);
        corpo.add(mensagem);
        corpo.add(Box.createVerticalStrut(12));
        grade.setOpaque(false);
        grade.setAlignmentX(Component.LEFT_ALIGNMENT);
        corpo.add(grade);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(corpo, BorderLayout.NORTH);
        add(Estilo.rolagem(topo), BorderLayout.CENTER);

        carregar();
    }

    private JLabel rotuloUsuario() {
        JLabel ola = Estilo.rotulo("Olá, " + Sessao.getUsuario().getNome().split(" ")[0] + "!",
                                   Estilo.BOTAO.deriveFont(15f), Estilo.TITULO_COR);
        ola.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        return ola;
    }

    /** Faixa azul do topo da Home, com a chamada do site e os atalhos de publicação. */
    private JPanel banner() {
        PainelArredondado banner = new PainelArredondado(28, Estilo.AZUL_CEU, null) {
            private final Image bichos = Estilo.imagem("catndog.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bichos == null) return;
                Graphics2D g2 = Estilo.suave(g);
                int h = getHeight() - 10;
                int w = bichos.getWidth(null) * h / bichos.getHeight(null);
                g2.drawImage(bichos, getWidth() - w - 30, getHeight() - h, w, h, null);
                g2.dispose();
            }
        };
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(24, 34, 26, 34));
        banner.setPreferredSize(new Dimension(100, 210));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 210));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel chamada = Estilo.rotulo("ENCONTRE, PUBLIQUE, REENCONTRE", Estilo.CHAMADA, Color.WHITE);
        JLabel titulo = Estilo.titulo("Trabalhamos para o reencontro", 52f, Color.WHITE);
        JLabel texto = Estilo.rotulo("Publique um pet perdido ou encontrado e ajude a vizinhança a reunir famílias.",
                                     Estilo.NEGRITO, Estilo.TITULO_COR);
        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        botoes.setOpaque(false);
        JButton perdi = Estilo.botaoPrimario("Perdi meu pet");
        perdi.addActionListener(e -> janela.mostrarPublicar(PublicacaoPet.PERDIDO));        // UC05
        JButton encontrei = Estilo.botaoSecundario("Encontrei um pet");
        encontrei.addActionListener(e -> janela.mostrarPublicar(PublicacaoPet.ENCONTRADO)); // UC06
        JButton disponibilizar = Estilo.botaoSecundario("Colocar para adoção");
        disponibilizar.addActionListener(e -> janela.mostrarDisponibilizar());               // UC10
        botoes.add(perdi);
        botoes.add(Box.createHorizontalStrut(10));
        botoes.add(encontrei);
        botoes.add(Box.createHorizontalStrut(10));
        botoes.add(disponibilizar);
        for (JPanel p : new JPanel[] {botoes}) p.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (JLabel l : new JLabel[] {chamada, titulo, texto}) l.setAlignmentX(Component.LEFT_ALIGNMENT);
        textos.add(chamada);
        textos.add(titulo);
        textos.add(texto);
        textos.add(Box.createVerticalStrut(16));
        textos.add(botoes);
        banner.add(textos, BorderLayout.WEST);
        return banner;
    }

    /** Cartão branco com os filtros por bairro e espécie (UC04). */
    private JPanel filtros() {
        PainelArredondado cartao = Estilo.cartao(22);
        cartao.setLayout(new GridBagLayout());
        cartao.setBorder(BorderFactory.createEmptyBorder(16, 24, 4, 24));
        cartao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        List<Especie> especies = new ArrayList<>();
        try {
            especies = controller.especies();
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
        especie = Estilo.comboEspecies(especies, "Todas as espécies");

        JButton filtrar = Estilo.botaoPrimario("Filtrar");
        filtrar.addActionListener(e -> carregar());                 // UC04 passos 1 e 2
        bairro.addActionListener(e -> carregar());
        JButton limpar = Estilo.link("Limpar filtros");
        limpar.addActionListener(e -> {                             // UC04 A1
            bairro.setText("");
            especie.setSelectedIndex(0);
            carregar();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.SOUTH;
        c.insets = new Insets(0, 0, 0, 16);
        c.weightx = 1;
        cartao.add(Estilo.campo("Bairro", bairro), c);
        c.gridx = 1;
        cartao.add(Estilo.campo("Espécie", especie), c);
        c.gridx = 2;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 14, 8);
        filtrar.setPreferredSize(new Dimension(130, 44));
        cartao.add(filtrar, c);
        c.gridx = 3;
        c.insets = new Insets(0, 0, 22, 0);
        cartao.add(limpar, c);
        return cartao;
    }

    /** Busca as publicações, da mais recente para a mais antiga, e monta os cards (UC03 passo 2). */
    private void carregar() {
        grade.removeAll();
        try {
            Especie filtroEspecie = Estilo.especieEscolhida(especie);
            List<PublicacaoPet> lista = controller.listar(bairro.getText(), filtroEspecie);
            boolean temFiltro = !bairro.getText().trim().isEmpty() || filtroEspecie != null;
            if (lista.isEmpty()) {
                mensagem.setText(temFiltro
                    ? "Nenhum pet encontrado para esses filtros. Tente limpar ou mudar a busca."  // UC04 E1
                    : "Ainda não há pets publicados.");                                          // UC03 E1
            } else {
                mensagem.setText(lista.size() + (lista.size() == 1 ? " pet publicado" : " pets publicados")
                                 + (temFiltro ? " com esses filtros" : ""));
            }
            for (PublicacaoPet p : lista) {
                CartaoPet card = new CartaoPet(p.getFotoUrl(), 230, 170, p.getNomePet(),
                                               p.getNomeEspecie(), "Bairro: " + p.getBairro());
                if (PublicacaoPet.PERDIDO.equals(p.getTipo())) {
                    card.adicionarSelo("PERDIDO", Estilo.LARANJA, Color.WHITE);
                } else {
                    card.adicionarSelo("ENCONTRADO", Estilo.AZUL_MEDIO, Color.WHITE);
                }
                grade.add(card);
            }
            // completa a última linha para os cards manterem o mesmo tamanho
            for (int i = lista.size(); i > 0 && i % 4 != 0; i++) {
                JPanel vazio = new JPanel();
                vazio.setOpaque(false);
                grade.add(vazio);
            }
        } catch (SQLException | RuntimeException e) {
            mensagem.setText("Não foi possível carregar as publicações.");
            Estilo.erroBanco(this, e);
        }
        grade.revalidate();
        grade.repaint();
    }
}
