package br.com.mappet.dao;

import br.com.mappet.util.ConexaoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Acesso à tabela T_MAPPET_INTERESSE_ADOCAO, o relacionamento N:N "manifesta interesse" (UC11). */
public class InteresseAdocaoDAO {

    /** Erro do Oracle para chave primária repetida (ORA-00001). */
    private static final int ORA_CHAVE_REPETIDA = 1;

    /**
     * Registra o interesse do adotante no animal.
     * Devolve false quando o interesse já existia (UC11 A2): a chave primária composta
     * (ID_USUARIO, ID_ANIMAL) impede um segundo registro.
     */
    public boolean registrar(long idUsuario, long idAnimal) throws SQLException {
        String sql = "INSERT INTO T_MAPPET_INTERESSE_ADOCAO (ID_USUARIO, ID_ANIMAL) VALUES (?, ?)";
        try (Connection con = ConexaoBD.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, idUsuario);
            ps.setLong(2, idAnimal);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == ORA_CHAVE_REPETIDA) return false;
            throw e;
        }
    }
}
