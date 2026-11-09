package br.com.mappet.controller;

import br.com.mappet.dao.AnimalAdocaoDAO;
import br.com.mappet.dao.EspecieDAO;
import br.com.mappet.dao.InteresseAdocaoDAO;
import br.com.mappet.model.AnimalAdocao;
import br.com.mappet.model.ContatoAcolhedor;
import br.com.mappet.model.Especie;
import br.com.mappet.util.FotoUtil;
import br.com.mappet.util.Sessao;
import br.com.mappet.util.ValidacaoException;
import br.com.mappet.util.Validador;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Área de adoção: vitrine e consulta (UC09), disponibilizar (UC10) e manifestar interesse (UC11). */
public class AdocaoController {

    private final AnimalAdocaoDAO animalDAO = new AnimalAdocaoDAO();
    private final EspecieDAO especieDAO = new EspecieDAO();
    private final InteresseAdocaoDAO interesseDAO = new InteresseAdocaoDAO();

    /** Vitrine da tela de login: liberada sem login, só foto, nome e espécie (UC09). */
    public List<AnimalAdocao> vitrine() throws SQLException {
        return animalDAO.listarVitrine();
    }

    /** UC09: a área de adoção completa exige login (A2). */
    public List<AnimalAdocao> listar() throws ValidacaoException, SQLException {
        if (!Sessao.estaLogado()) throw new ValidacaoException("Faça login para acessar a área de adoção.");
        return animalDAO.listar();
    }

    /** UC10: o acolhedor disponibiliza para adoção um animal do seu lar temporário. */
    public AnimalAdocao disponibilizar(String nome, Especie especie, String bairro, String descricao, File foto)
            throws ValidacaoException, SQLException, IOException {
        if (!Sessao.estaLogado()) throw new ValidacaoException("Faça login para continuar.");

        List<String> erros = new ArrayList<>();
        Validador.textoSemNumeros(erros, nome, "O nome do pet", 60);
        if (especie == null || !especieDAO.existe(especie.getId())) erros.add("Escolha uma espécie da lista.");
        Validador.textoSemNumeros(erros, bairro, "O bairro", 80);
        Validador.obrigatorio(erros, descricao, "A descrição");
        try {
            FotoUtil.validar(foto);
        } catch (ValidacaoException e) {
            erros.addAll(e.getErros());
        }
        if (!erros.isEmpty()) throw new ValidacaoException(erros);     // UC10 E1

        String caminho = FotoUtil.salvar(foto);
        AnimalAdocao a = new AnimalAdocao();
        a.setNome(nome.trim());
        a.setIdEspecie(especie.getId());
        a.setBairro(bairro.trim());
        a.setDescricao(descricao.trim());
        a.setFotoUrl(caminho);
        a.setIdUsuario(Sessao.getUsuario().getId());
        try {
            animalDAO.inserir(a);
        } catch (SQLException e) {
            FotoUtil.apagar(caminho);
            throw e;
        }
        return a;
    }

    /**
     * UC11: registra o interesse do adotante e só então devolve o contato do acolhedor.
     * Se o interesse já existia (A2), não grava de novo e devolve o contato mesmo assim.
     */
    public ContatoAcolhedor quererAdotar(AnimalAdocao animal) throws ValidacaoException, SQLException {
        if (!Sessao.estaLogado()) throw new ValidacaoException("Faça login para continuar."); // UC11 E1
        ContatoAcolhedor contato = animalDAO.buscarContato(animal.getId());
        if (contato == null) throw new ValidacaoException("Este animal não está mais disponível.");
        boolean novo = interesseDAO.registrar(Sessao.getUsuario().getId(), animal.getId()); // UC11 passo 4
        return new ContatoAcolhedor(contato.getNome(), contato.getTelefone(), contato.getEmail(), !novo);
    }
}
