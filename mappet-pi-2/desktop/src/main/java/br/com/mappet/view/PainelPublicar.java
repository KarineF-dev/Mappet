package br.com.mappet.view;

import br.com.mappet.controller.PublicacaoController;
import br.com.mappet.model.Especie;
import br.com.mappet.model.PublicacaoPet;
import br.com.mappet.util.ValidacaoException;
import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Formulário "Perdi meu pet" (UC05) e "Encontrei um pet" (UC06), com foto (UC07) e validação (UC08). */
public class PainelPublicar extends JPanel {

    private final JanelaPrincipal janela;
    private final String tipo;
    private final PublicacaoController controller = new PublicacaoController();
    private final JTextField nomePet = new JTextField();
    private final JTextField bairro = new JTextField();
    private JComboBox<Especie> especie;
    private final SeletorFoto foto = new SeletorFoto();

    public PainelPublicar(JanelaPrincipal janela, String tipo) {
        this.janela = janela;
        this.tipo = tipo;
        boolean perdido = PublicacaoPet.PERDIDO.equals(tipo);
        setLayout(new BorderLayout());
        setBackground(Estilo.FUNDO);

        JButton voltar = Estilo.botaoSecundario("Voltar");
        voltar.addActionListener(e -> janela.mostrarHome());       // UC05/UC06 A1: nada é salvo
        add(Estilo.cabecalho(perdido ? "Perdi meu pet" : "Encontrei um pet", voltar), BorderLayout.NORTH);

        List<Especie> especies = new ArrayList<>();
        try {
            especies = controller.especies();
        } catch (SQLException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
        especie = Estilo.comboEspecies(especies, "Selecione a espécie");

        JPanel campos = MoldeFormulario.coluna(
            MoldeFormulario.dupla(
                Estilo.campo(perdido ? "Nome do pet" : "Nome do pet (ou um apelido)", nomePet),
                Estilo.campo("Espécie", especie)),
            Estilo.campo(perdido ? "Bairro onde foi visto pela última vez" : "Bairro onde foi encontrado", bairro),
            Estilo.rotulo(perdido
                ? "<html>Dica: uma foto nítida e o bairro certo ajudam muito no reencontro.</html>"
                : "<html>Dica: conte o bairro exato para o tutor reconhecer o pet mais rápido.</html>",
                Estilo.PEQUENA, Estilo.CINZA));

        JButton publicar = Estilo.botaoPrimario("Publicar anúncio");
        publicar.addActionListener(e -> publicar());

        String sub = perdido
            ? "Conte como é o seu pet e onde ele foi visto pela última vez."
            : "Conte como é o pet que você encontrou e em qual bairro ele está.";
        add(MoldeFormulario.montar(janela, perdido ? Abas.PERDIDO : Abas.ENCONTRADO, sub, foto, campos, publicar),
            BorderLayout.CENTER);
    }

    private void publicar() {
        try {
            controller.publicar(tipo, nomePet.getText(), Estilo.especieEscolhida(especie),
                                bairro.getText(), foto.getArquivo());
            Estilo.aviso(this, "Pet publicado! Ele já aparece na Home.");
            janela.mostrarHome();                                   // UC05/UC06 passo 8
        } catch (ValidacaoException e) {                            // UC08 E1 e E2: os dados ficam na tela
            Estilo.alertaErros(this, e.getErros());
        } catch (SQLException | IOException | RuntimeException e) {
            Estilo.erroBanco(this, e);
        }
    }
}
