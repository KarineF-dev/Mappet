package br.com.mappet.view;

import br.com.mappet.controller.AdocaoController;
import br.com.mappet.controller.PublicacaoController;
import br.com.mappet.model.Especie;
import br.com.mappet.util.ValidacaoException;
import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Formulário para colocar um pet do lar temporário para adoção (UC10). */
public class PainelDisponibilizar extends JPanel {

    private final JanelaPrincipal janela;
    private final AdocaoController controller = new AdocaoController();
    private final JTextField nome = new JTextField();
    private final JTextField bairro = new JTextField();
    private final JTextArea descricao = new JTextArea(4, 20);
    private JComboBox<Especie> especie;
    private final SeletorFoto foto = new SeletorFoto();

    public PainelDisponibilizar(JanelaPrincipal janela) {
        this.janela = janela;
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);

        JButton voltar = Estilo.botaoSecundario("Voltar");
        voltar.addActionListener(e -> janela.mostrarHome());       // UC10 A1: nada é salvo
        add(Estilo.cabecalho("Colocar para adoção", voltar), BorderLayout.NORTH);

        List<Especie> especies = new ArrayList<>();
        try {
            especies = new PublicacaoController().especies();
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
        especie = Estilo.comboEspecies(especies, "Selecione a espécie");

        JPanel campos = MoldeFormulario.coluna(
            MoldeFormulario.dupla(Estilo.campo("Nome do pet", nome), Estilo.campo("Espécie", especie)),
            Estilo.campo("Bairro do lar temporário", bairro),
            Estilo.campo("Descrição (temperamento, idade, cuidados)", Estilo.rolagemTexto(descricao)));

        JButton publicar = Estilo.botaoPrimario("Publicar anúncio");
        publicar.addActionListener(e -> publicar());

        add(MoldeFormulario.montar(janela, Abas.ADOCAO,
            "Conte sobre o pet do seu lar temporário para encontrar uma família para ele.",
            foto, campos, publicar), BorderLayout.CENTER);
    }

    private void publicar() {
        try {
            controller.disponibilizar(nome.getText(), Estilo.especieEscolhida(especie), bairro.getText(),
                                      descricao.getText(), foto.getArquivo());
            Estilo.aviso(this, "Pet disponível para adoção!");
            janela.mostrarAdocao();                                // UC10 passo 8
        } catch (ValidacaoException e) {                           // UC10 E1: os dados ficam na tela
            Estilo.alertaErros(this, e.getErros());
        } catch (SQLException | IOException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
    }
}
