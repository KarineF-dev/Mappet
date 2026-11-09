package br.com.mappet.dao;

import br.com.mappet.model.Especie;
import br.com.mappet.util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Acesso à tabela T_MAPPET_ESPECIE (opções do UC04 e do UC08). */
public class EspecieDAO {

    public List<Especie> listar() throws SQLException {
        String sql = "SELECT ID_ESPECIE, NOME FROM T_MAPPET_ESPECIE ORDER BY NOME";
        List<Especie> lista = new ArrayList<>();
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Especie(rs.getLong("ID_ESPECIE"), rs.getString("NOME")));
            }
        }
        return lista;
    }

    public boolean existe(long idEspecie) throws SQLException {
        String sql = "SELECT 1 FROM T_MAPPET_ESPECIE WHERE ID_ESPECIE = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idEspecie);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
