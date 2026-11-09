package br.com.mappet.dao;

import br.com.mappet.model.Usuario;
import br.com.mappet.util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Acesso à tabela T_MAPPET_USUARIO. Todas as consultas usam parâmetros (?), nunca texto concatenado. */
public class UsuarioDAO {

    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT 1 FROM T_MAPPET_USUARIO WHERE EMAIL = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(Usuario u) throws SQLException {
        String sql = "INSERT INTO T_MAPPET_USUARIO (NOME, EMAIL, TELEFONE, SENHA_HASH) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql, new String[] {"ID_USUARIO"})) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getTelefone());
            ps.setString(4, u.getSenhaHash());
            ps.executeUpdate();
            try (ResultSet chave = ps.getGeneratedKeys()) {
                if (chave.next()) u.setId(chave.getLong(1));
            }
        }
    }

    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT ID_USUARIO, NOME, EMAIL, TELEFONE, SENHA_HASH FROM T_MAPPET_USUARIO WHERE EMAIL = ?";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Usuario u = new Usuario(rs.getString("NOME"), rs.getString("EMAIL"),
                                        rs.getString("TELEFONE"), rs.getString("SENHA_HASH"));
                u.setId(rs.getLong("ID_USUARIO"));
                return u;
            }
        }
    }
}
