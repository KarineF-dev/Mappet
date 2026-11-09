package br.com.mappet.dao;

import br.com.mappet.model.PublicacaoPet;
import br.com.mappet.util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela T_MAPPET_PUBLICACAO_PET (UC03, UC04, UC05 e UC06). */
public class PublicacaoPetDAO {

    public void inserir(PublicacaoPet p) throws SQLException {
        String sql = "INSERT INTO T_MAPPET_PUBLICACAO_PET (TIPO, NOME_PET, BAIRRO, FOTO_URL, ID_USUARIO, ID_ESPECIE) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, new String[] {"ID_PUBLICACAO"})) {
            ps.setString(1, p.getTipo());
            ps.setString(2, p.getNomePet());
            ps.setString(3, p.getBairro());
            ps.setString(4, p.getFotoUrl());
            ps.setLong(5, p.getIdUsuario());
            ps.setLong(6, p.getIdEspecie());
            ps.executeUpdate();
            try (ResultSet chave = ps.getGeneratedKeys()) {
                if (chave.next()) p.setId(chave.getLong(1));
            }
        }
    }

    /**
     * Lista as publicações da mais recente para a mais antiga (UC03).
     * bairro e idEspecie são os filtros opcionais do UC04 (null = sem filtro).
     */
    public List<PublicacaoPet> listar(String bairro, Long idEspecie) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT p.ID_PUBLICACAO, p.TIPO, p.NOME_PET, p.BAIRRO, p.FOTO_URL, p.DATA_PUBLICACAO, "
          + "p.ID_USUARIO, p.ID_ESPECIE, e.NOME AS ESPECIE "
          + "FROM T_MAPPET_PUBLICACAO_PET p "
          + "JOIN T_MAPPET_ESPECIE e ON e.ID_ESPECIE = p.ID_ESPECIE WHERE 1 = 1");
        if (bairro != null) sql.append(" AND UPPER(TRIM(p.BAIRRO)) = UPPER(TRIM(?))");
        if (idEspecie != null) sql.append(" AND p.ID_ESPECIE = ?");
        sql.append(" ORDER BY p.DATA_PUBLICACAO DESC");

        List<PublicacaoPet> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int i = 1;
            if (bairro != null) ps.setString(i++, bairro);
            if (idEspecie != null) ps.setLong(i, idEspecie);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PublicacaoPet p = new PublicacaoPet();
                    p.setId(rs.getLong("ID_PUBLICACAO"));
                    p.setTipo(rs.getString("TIPO"));
                    p.setNomePet(rs.getString("NOME_PET"));
                    p.setBairro(rs.getString("BAIRRO"));
                    p.setFotoUrl(rs.getString("FOTO_URL"));
                    Timestamp data = rs.getTimestamp("DATA_PUBLICACAO");
                    if (data != null) p.setDataPublicacao(data.toLocalDateTime());
                    p.setIdUsuario(rs.getLong("ID_USUARIO"));
                    p.setIdEspecie(rs.getLong("ID_ESPECIE"));
                    p.setNomeEspecie(rs.getString("ESPECIE"));
                    lista.add(p);
                }
            }
        }
        return lista;
    }
}
