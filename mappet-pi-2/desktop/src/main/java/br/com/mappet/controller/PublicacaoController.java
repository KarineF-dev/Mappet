package br.com.mappet.controller;

import br.com.mappet.dao.EspecieDAO;
import br.com.mappet.dao.PublicacaoPetDAO;
import br.com.mappet.model.Especie;
import br.com.mappet.model.PublicacaoPet;
import br.com.mappet.util.FotoUtil;
import br.com.mappet.util.Sessao;
import br.com.mappet.util.ValidacaoException;
import br.com.mappet.util.Validador;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Home e publicações: UC03, UC04, UC05, UC06, UC07 e UC08. */
public class PublicacaoController {

    private final PublicacaoPetDAO publicacaoDAO = new PublicacaoPetDAO();
    private final EspecieDAO especieDAO = new EspecieDAO();

    public List<Especie> especies() throws SQLException {
        return especieDAO.listar();
    }

    /** UC03 e UC04: bairro e espécie em branco = sem filtro. */
    public List<PublicacaoPet> listar(String bairro, Especie especie) throws SQLException {
        String filtroBairro = (bairro == null || bairro.trim().isEmpty()) ? null : bairro.trim();
        Long filtroEspecie = especie == null ? null : especie.getId();
        return publicacaoDAO.listar(filtroBairro, filtroEspecie);
    }

    /** UC05 (tipo "perdido") e UC06 (tipo "encontrado"), com foto (UC07) e validação (UC08). */
    public PublicacaoPet publicar(String tipo, String nomePet, Especie especie, String bairro, File foto)
            throws ValidacaoException, SQLException, IOException {
        if (!Sessao.estaLogado()) throw new ValidacaoException("Faça login para publicar.");

        List<String> erros = new ArrayList<>();
        Validador.textoSemNumeros(erros, nomePet, "O nome do pet", 60);   // UC08 passos 2 e 3
        if (especie == null || !especieDAO.existe(especie.getId())) erros.add("Escolha uma espécie da lista.");
        Validador.textoSemNumeros(erros, bairro, "O bairro", 80);
        try {
            FotoUtil.validar(foto);                                        // UC08 passo 4 e UC07
        } catch (ValidacaoException e) {
            erros.addAll(e.getErros());
        }
        if (!erros.isEmpty()) throw new ValidacaoException(erros);         // UC08 E1 e E2

        String caminho = FotoUtil.salvar(foto);  // a foto só é copiada depois de validar tudo
        PublicacaoPet p = new PublicacaoPet();
        p.setTipo(tipo);
        p.setNomePet(nomePet.trim());
        p.setIdEspecie(especie.getId());
        p.setBairro(bairro.trim());
        p.setFotoUrl(caminho);
        p.setIdUsuario(Sessao.getUsuario().getId()); // dono vem da sessão (UC05/UC06 passo 7)
        try {
            publicacaoDAO.inserir(p);
        } catch (SQLException e) {
            FotoUtil.apagar(caminho);
            throw e;
        }
        return p;
    }
}
