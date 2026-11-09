package br.com.mappet.view;

import br.com.mappet.controller.AdocaoController;
import br.com.mappet.model.AnimalAdocao;
import br.com.mappet.model.ContatoAcolhedor;
import br.com.mappet.util.ValidacaoException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Área de adoção, só com login (UC09), com o botão "Quero adotar" de cada animal (UC11). */
public class PainelAdocao extends JPanel {

    private final JanelaPrincipal janela;
    private final AdocaoController controller = new AdocaoController();

    public PainelAdocao(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);

        JButton disponibilizar = Estilo.botaoSecundario("Colocar para adoção");
        disponibilizar.addActionListener(e -> janela.mostrarDisponibilizar());
        JButton voltar = Estilo.botaoPrimario("Voltar");
        voltar.addActionListener(e -> janela.mostrarHome());              // UC09 A1
        add(Estilo.cabecalho("Adoção", disponibilizar, voltar), BorderLayout.NORTH);

        JPanel corpo = new JPanel();
        corpo.setOpaque(false);
        corpo.setLayout(new BoxLayout(corpo, BoxLayout.Y_AXIS));
        corpo.setBorder(BorderFactory.createEmptyBorder(24, 40, 30, 40));
        JLabel chamada = Estilo.chamada("Adoção responsável");
        JLabel titulo = Estilo.titulo("Patinhas esperando por um lar", 50f, Estilo.TITULO_COR);
        JLabel sub = Estilo.rotulo("Clique em \"Quero adotar\" para ver o contato de quem está cuidando do animal.",
                                   Estilo.NORMAL, Estilo.CINZA);
        for (JLabel l : new JLabel[] {chamada, titulo, sub}) {
            l.setAlignmentX(Component.LEFT_ALIGNMENT);
            corpo.add(l);
        }
        corpo.add(Box.createVerticalStrut(20));

        JPanel grade = new JPanel(new GridLayout(0, 3, 22, 22));
        grade.setOpaque(false);
        grade.setAlignmentX(Component.LEFT_ALIGNMENT);
        try {
            List<AnimalAdocao> animais = controller.listar();             // UC09 passo 6
            if (animais.isEmpty()) {                                      // UC09 E1
                JLabel vazio = Estilo.rotulo("Não há animais para adoção no momento.", Estilo.NEGRITO, Estilo.CINZA);
                vazio.setAlignmentX(Component.LEFT_ALIGNMENT);
                corpo.add(vazio);
            }
            for (AnimalAdocao a : animais) grade.add(cartao(a));
            for (int i = animais.size(); i > 0 && i % 3 != 0; i++) {
                JPanel v = new JPanel();
                v.setOpaque(false);
                grade.add(v);
            }
        } catch (ValidacaoException e) {
            Estilo.alertaErros(this, e.getErros());
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
        corpo.add(grade);

        JPanel topo = new JPanel(new BorderLayout());
        topo.setOpaque(false);
        topo.add(corpo, BorderLayout.NORTH);
        add(Estilo.rolagem(topo), BorderLayout.CENTER);
    }

    /** Card com foto, dados do animal e o botão "Quero adotar" (UC11 passo 2). */
    private JPanel cartao(AnimalAdocao a) {
        PainelArredondado card = new PainelArredondado(24, java.awt.Color.WHITE, Estilo.BORDA);
        card.setSombra(true);
        card.setLayout(new BorderLayout(0, 12));
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 18, 12));

        FotoArredondada foto = new FotoArredondada(a.getFotoUrl(), 320, 210, 20);
        foto.setSelo("PARA ADOÇÃO", Estilo.LARANJA, java.awt.Color.WHITE);
        card.add(foto, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        JLabel nome = Estilo.titulo(a.getNome(), 34f, Estilo.TITULO_COR);
        JLabel dados = Estilo.rotulo(a.getNomeEspecie() + "  •  " + a.getBairro(), Estilo.NEGRITO, Estilo.LARANJA);
        String texto = a.getDescricao() == null ? "" : a.getDescricao();
        if (texto.length() > 120) texto = texto.substring(0, 117).trim() + "...";
        JLabel descricao = Estilo.rotulo("<html><div style='width:250px'>" + texto + "</div></html>",
                                         Estilo.NORMAL, Estilo.TEXTO);
        descricao.setPreferredSize(new Dimension(260, 62));
        descricao.setVerticalAlignment(JLabel.TOP);
        JButton quero = Estilo.botaoPrimario("Quero adotar");
        quero.addActionListener(e -> quererAdotar(a));                    // UC11 passo 3
        quero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        for (JComponent c : new JComponent[] {nome, dados, descricao, quero}) c.setAlignmentX(Component.LEFT_ALIGNMENT);
        info.add(nome);
        info.add(dados);
        info.add(Box.createVerticalStrut(8));
        info.add(descricao);
        info.add(Box.createVerticalStrut(12));
        info.add(quero);
        card.add(info, BorderLayout.CENTER);
        return card;
    }

    /** UC11: registra o interesse e mostra o contato do acolhedor. */
    private void quererAdotar(AnimalAdocao a) {
        try {
            ContatoAcolhedor c = controller.quererAdotar(a);
            String titulo = c.isInteresseJaRegistrado()
                ? "Você já demonstrou interesse em " + a.getNome()             // UC11 A2
                : "Interesse em " + a.getNome() + " registrado!";               // UC11 passo 4
            Dialogo.mostrar(this, Dialogo.Tipo.INFO, titulo, contato(c), "Fechar");  // UC11 passos 5 e 6
        } catch (ValidacaoException e) {                                     // UC11 E1
            Estilo.alertaErros(this, e.getErros());
            janela.mostrarLogin();
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
    }

    /** Bloco "Responsável pela adoção", como no site. */
    private JComponent contato(ContatoAcolhedor c) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel intro = Estilo.rotulo("Entre em contato com o acolhedor para combinar a adoção.", Estilo.NORMAL, Estilo.TEXTO);
        intro.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(intro);
        p.add(Box.createVerticalStrut(12));

        PainelArredondado caixa = new PainelArredondado(18, Estilo.AZUL_CLARO, null);
        caixa.setLayout(new BoxLayout(caixa, BoxLayout.Y_AXIS));
        caixa.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        caixa.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel cab = Estilo.rotulo("RESPONSÁVEL PELA ADOÇÃO", Estilo.CHAMADA, Estilo.LARANJA);
        cab.setAlignmentX(Component.LEFT_ALIGNMENT);
        caixa.add(cab);
        caixa.add(Box.createVerticalStrut(8));
        String[][] linhas = {{"Nome", c.getNome()}, {"Telefone/WhatsApp", c.getTelefone()}, {"E-mail", c.getEmail()}};
        for (String[] l : linhas) {
            JLabel r = Estilo.rotulo("<html><b>" + l[0] + ":</b>&nbsp; " + l[1] + "</html>", Estilo.NORMAL, Estilo.TITULO_COR);
            r.setAlignmentX(Component.LEFT_ALIGNMENT);
            r.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
            caixa.add(r);
        }
        p.add(caixa);
        return p;
    }
}
