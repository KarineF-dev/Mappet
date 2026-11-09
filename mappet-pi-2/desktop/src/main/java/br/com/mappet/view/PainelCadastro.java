package br.com.mappet.view;

import br.com.mappet.controller.UsuarioController;
import br.com.mappet.util.ValidacaoException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/** Tela de cadastro (UC01), com os gatinhos nas laterais como no site. */
public class PainelCadastro extends JPanel {

    private final JanelaPrincipal janela;
    private final UsuarioController controller = new UsuarioController();
    private final JTextField nome = new JTextField();
    private final JTextField email = new JTextField();
    private final JTextField telefone = new JTextField();
    private final JPasswordField senha = new JPasswordField();

    public PainelCadastro(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);
        JButton entrar = Estilo.botaoSecundario("Login");
        entrar.addActionListener(e -> janela.mostrarLogin());
        add(Estilo.cabecalho(null, entrar), BorderLayout.NORTH);

        PainelArredondado cartao = Estilo.cartao(28);
        cartao.setLayout(new BoxLayout(cartao, BoxLayout.Y_AXIS));
        cartao.setBorder(BorderFactory.createEmptyBorder(24, 36, 26, 36));
        cartao.setPreferredSize(new Dimension(460, 590));

        JLabel chamada = Estilo.chamada("Faça parte do MAPPET");
        JLabel titulo = Estilo.titulo("CADASTRE-SE", 58f, Estilo.AZUL_MEDIO);
        JLabel sub = Estilo.rotulo("Crie sua conta e faça parte dessa rede de carinho", Estilo.NORMAL, Estilo.CINZA);
        for (JLabel l : new JLabel[] {chamada, titulo, sub}) l.setAlignmentX(Component.LEFT_ALIGNMENT);
        cartao.add(chamada);
        cartao.add(titulo);
        cartao.add(sub);
        cartao.add(Box.createVerticalStrut(18));
        cartao.add(Estilo.campo("Nome", nome));
        cartao.add(Estilo.campo("E-mail", email));
        cartao.add(Estilo.campo("Telefone (só números, com DDD)", telefone));
        cartao.add(Estilo.campo("Senha", senha));

        JButton cadastrar = Estilo.botaoPrimario("CADASTRAR");
        cadastrar.addActionListener(e -> cadastrar());
        cadastrar.setAlignmentX(Component.LEFT_ALIGNMENT);
        cadastrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        cartao.add(cadastrar);
        cartao.add(Box.createVerticalStrut(8));
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        linha.setOpaque(false);
        linha.add(Estilo.rotulo("Já possui uma conta?", Estilo.PEQUENA, Estilo.CINZA));
        JButton voltar = Estilo.link("Faça login");
        voltar.addActionListener(e -> janela.mostrarLogin());   // UC01 A1: nada é salvo
        linha.add(voltar);
        linha.setAlignmentX(Component.LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        cartao.add(linha);

        JPanel centro = new JPanel(new GridBagLayout()) {
            private final Image gatoEsquerdo = Estilo.imagem("gato-esquerdo.png");
            private final Image gatoDireito = Estilo.imagem("gato-direito.png");

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = Estilo.suave(g);
                int h = getHeight();
                if (gatoEsquerdo != null) {
                    int ah = (int) (h * 0.62);
                    int aw = gatoEsquerdo.getWidth(null) * ah / gatoEsquerdo.getHeight(null);
                    g2.drawImage(gatoEsquerdo, 0, h - ah, aw, ah, null);
                }
                if (gatoDireito != null) {
                    int ah = (int) (h * 0.62);
                    int aw = gatoDireito.getWidth(null) * ah / gatoDireito.getHeight(null);
                    g2.drawImage(gatoDireito, getWidth() - aw, h - ah - 40, aw, ah, null);
                }
                g2.dispose();
            }
        };
        centro.setOpaque(false);
        centro.add(cartao);
        add(centro, BorderLayout.CENTER);
    }

    private void cadastrar() {
        try {
            controller.cadastrar(nome.getText(), email.getText(), telefone.getText(), new String(senha.getPassword()));
            Estilo.aviso(this, "Cadastro realizado! Faça login para continuar.");  // UC01 passo 8
            janela.mostrarLogin();
        } catch (ValidacaoException e) {                          // UC01 E1 e E2
            Estilo.alertaErros(this, e.getErros());
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
    }
}
