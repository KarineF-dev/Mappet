package br.com.mappet.dao;

import br.com.mappet.model.AnimalAdocao;
import br.com.mappet.model.ContatoAcolhedor;
import br.com.mappet.util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela T_MAPPET_ANIMAL_ADOCAO (UC09 e UC10). */
public class AnimalAdocaoDAO {

    /** Quantidade de animais em destaque na vitrine da tela de login (UC09). */
    public static final int LIMITE_VITRINE = 4;

    public void inserir(AnimalAdocao a) throws SQLException {
        String sql = "INSERT INTO T_MAPPET_ANIMAL_ADOCAO (NOME, BAIRRO, DESCRICAO, FOTO_URL, ID_USUARIO, ID_ESPECIE) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, new String[] {"ID_ANIMAL"})) {
            ps.setString(1, a.getNome());
            ps.setString(2, a.getBairro());
            ps.setString(3, a.getDescricao());
            ps.setString(4, a.getFotoUrl());
            ps.setLong(5, a.getIdUsuario());
            ps.setLong(6, a.getIdEspecie());
            ps.executeUpdate();
            try (ResultSet chave = ps.getGeneratedKeys()) {
                if (chave.next()) a.setId(chave.getLong(1));
            }
        }
    }

    /** Área de adoção completa, só para usuário logado (UC09). */
    public List<AnimalAdocao> listar() throws SQLException {
        String sql = "SELECT a.ID_ANIMAL, a.NOME, a.BAIRRO, a.DESCRICAO, a.FOTO_URL, a.DATA_CADASTRO, "
                   + "a.ID_USUARIO, a.ID_ESPECIE, e.NOME AS ESPECIE "
                   + "FROM T_MAPPET_ANIMAL_ADOCAO a "
                   + "JOIN T_MAPPET_ESPECIE e ON e.ID_ESPECIE = a.ID_ESPECIE "
                   + "ORDER BY a.DATA_CADASTRO DESC";
        List<AnimalAdocao> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AnimalAdocao a = new AnimalAdocao();
                a.setId(rs.getLong("ID_ANIMAL"));
                a.setNome(rs.getString("NOME"));
                a.setBairro(rs.getString("BAIRRO"));
                a.setDescricao(rs.getString("DESCRICAO"));
                a.setFotoUrl(rs.getString("FOTO_URL"));
                Timestamp data = rs.getTimestamp("DATA_CADASTRO");
                if (data != null) a.setDataCadastro(data.toLocalDateTime());
                a.setIdUsuario(rs.getLong("ID_USUARIO"));
                a.setIdEspecie(rs.getLong("ID_ESPECIE"));
                a.setNomeEspecie(rs.getString("ESPECIE"));
                lista.add(a);
            }
        }
        return lista;
    }

    /** Vitrine da tela de login: só foto, nome e espécie de alguns animais (UC09). */
    public List<AnimalAdocao> listarVitrine() throws SQLException {
        String sql = "SELECT a.ID_ANIMAL, a.NOME, a.FOTO_URL, e.NOME AS ESPECIE "
                   + "FROM T_MAPPET_ANIMAL_ADOCAO a "
                   + "JOIN T_MAPPET_ESPECIE e ON e.ID_ESPECIE = a.ID_ESPECIE "
                   + "ORDER BY a.DATA_CADASTRO DESC "
                   + "FETCH FIRST " + LIMITE_VITRINE + " ROWS ONLY";
        List<AnimalAdocao> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                AnimalAdocao a = new AnimalAdocao();
                a.setId(rs.getLong("ID_ANIMAL"));
                a.setNome(rs.getString("NOME"));
                a.setFotoUrl(rs.getString("FOTO_URL"));
                a.setNomeEspecie(rs.getString("ESPECIE"));
                lista.add(a);
            }
        }
        return lista;
    }

    /** Contato do acolhedor que cadastrou o animal (exibido no UC11). */
    public ContatoAcolhedor buscarContato(long idAnimal) throws SQLException {
        String sql = "SELECT u.NOME, u.TELEFONE, u.EMAIL "
                   + "FROM T_MAPPET_ANIMAL_ADOCAO a "
                   + "JOIN T_MAPPET_USUARIO u ON u.ID_USUARIO = a.ID_USUARIO "
                   + "WHERE a.ID_ANIMAL = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idAnimal);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new ContatoAcolhedor(rs.getString("NOME"), rs.getString("TELEFONE"),
                                            rs.getString("EMAIL"), false);
            }
        }
    }
}
