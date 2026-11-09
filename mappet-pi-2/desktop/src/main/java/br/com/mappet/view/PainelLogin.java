package br.com.mappet.view;

import br.com.mappet.controller.AdocaoController;
import br.com.mappet.controller.UsuarioController;
import br.com.mappet.model.AnimalAdocao;
import br.com.mappet.util.ValidacaoException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Tela de login (UC02) com a vitrine "Adote aqui" ao lado (UC09). */
public class PainelLogin extends JPanel {

    private final JanelaPrincipal janela;
    private final UsuarioController usuarioController = new UsuarioController();
    private final AdocaoController adocaoController = new AdocaoController();
    private final JTextField email = new JTextField();
    private final JPasswordField senha = new JPasswordField();

    public PainelLogin(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);
        JButton cadastrese = Estilo.botaoSecundario("Cadastre-se");
        cadastrese.addActionListener(e -> janela.mostrarCadastro());
        add(Estilo.cabecalho(null, cadastrese), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        c.insets = new Insets(30, 30, 34, 14);
        c.weightx = 0.42;
        centro.add(formularioLogin(), c);
        c.gridx = 1;
        c.weightx = 0.58;
        c.insets = new Insets(30, 14, 34, 30);
        centro.add(vitrine(), c);
        add(centro, BorderLayout.CENTER);
    }

    private JPanel formularioLogin() {
        PainelArredondado cartao = Estilo.cartao(28);
        cartao.setLayout(new GridBagLayout());
        JPanel conteudo = new JPanel();
        conteudo.setOpaque(false);
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setPreferredSize(new Dimension(340, 400));

        JLabel chamada = Estilo.chamada("Área do usuário");
        JLabel titulo = Estilo.titulo("BEM-VINDO", 64f, Estilo.AZUL_MEDIO);
        JLabel sub = Estilo.rotulo("Faça login para continuar", Estilo.NORMAL, Estilo.CINZA);
        for (JLabel l : new JLabel[] {chamada, titulo, sub}) l.setAlignmentX(Component.LEFT_ALIGNMENT);
        conteudo.add(chamada);
        conteudo.add(titulo);
        conteudo.add(sub);
        conteudo.add(Box.createVerticalStrut(22));
        conteudo.add(Estilo.campo("E-mail", email));
        conteudo.add(Estilo.campo("Senha", senha));

        JButton entrar = Estilo.botaoPrimario("ENTRAR");
        entrar.addActionListener(e -> entrar());
        senha.addActionListener(e -> entrar());               // Enter no campo de senha
        entrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        entrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(entrar);
        conteudo.add(Box.createVerticalStrut(12));

        JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        linha.setOpaque(false);
        linha.add(Estilo.rotulo("Ainda não possui uma conta?", Estilo.PEQUENA, Estilo.CINZA));
        JButton cadastrar = Estilo.link("Cadastre-se");
        cadastrar.addActionListener(e -> janela.mostrarCadastro());  // UC02 A1
        linha.add(cadastrar);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        conteudo.add(linha);

        cartao.add(conteudo);
        return cartao;
    }

    private void entrar() {
        try {
            usuarioController.entrar(email.getText(), new String(senha.getPassword()));
            janela.mostrarHome();                            // UC02 passo 6
        } catch (ValidacaoException e) {                     // UC02 E1 e E2
            Estilo.alertaErros(this, e.getErros());
            senha.setText("");
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
    }

    /** Lado direito: alguns animais em destaque e o botão "Adote aqui" (UC09). */
    private JPanel vitrine() {
        PainelArredondado area = new PainelArredondado(28, Estilo.AZUL_CEU, null);
        area.setLayout(new BorderLayout(0, 16));
        area.setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));

        JPanel topo = new JPanel();
        topo.setOpaque(false);
        topo.setLayout(new BoxLayout(topo, BoxLayout.Y_AXIS));
        JLabel chamada = Estilo.rotulo("ADOÇÃO RESPONSÁVEL", Estilo.CHAMADA, java.awt.Color.WHITE);
        JLabel titulo = Estilo.titulo("Quer adotar? Conheça alguns animais", 40f, java.awt.Color.WHITE);
        JLabel texto = Estilo.rotulo("Para ver todos e falar com o acolhedor, faça login.", Estilo.NEGRITO, Estilo.TITULO_COR);
        topo.add(chamada);
        topo.add(titulo);
        topo.add(texto);
        area.add(topo, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);
        try {
            List<AnimalAdocao> destaque = adocaoController.vitrine();
            if (destaque.isEmpty()) {
                cards.add(Estilo.rotulo("Ainda não há animais para adoção.", Estilo.NEGRITO, Estilo.TITULO_COR));
            }
            int n = 0;
            for (AnimalAdocao a : destaque) {
                if (n++ == 3) break;
                CartaoPet card = new CartaoPet(a.getFotoUrl(), 200, 240, a.getNome(), a.getNomeEspecie());
                card.aoClicar(this::pedirLogin);
                cards.add(card);
            }
        } catch (SQLException | RuntimeException e) {
            cards.add(Estilo.rotulo("Não foi possível carregar os animais em destaque.", Estilo.NEGRITO, Estilo.TITULO_COR));
        }
        JPanel seguraCards = new JPanel(new BorderLayout());
        seguraCards.setOpaque(false);
        seguraCards.add(cards, BorderLayout.NORTH);
        area.add(seguraCards, BorderLayout.CENTER);

        JButton adoteAqui = Estilo.botaoPrimario("Adote aqui");
        adoteAqui.addActionListener(e -> pedirLogin());
        adoteAqui.setPreferredSize(new Dimension(200, 48));
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rodape.setOpaque(false);
        rodape.add(adoteAqui);
        area.add(rodape, BorderLayout.SOUTH);
        return area;
    }

    /** UC09 A2: sem login, o sistema pede para entrar ou se cadastrar. */
    private void pedirLogin() {
        Estilo.aviso(this, "Faça login para continuar",
            "Para ver todos os animais para adoção e manifestar interesse,\nfaça login ou cadastre-se.");
        email.requestFocusInWindow();
    }
}
