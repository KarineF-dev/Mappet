package br.com.mappet.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Abre conexões com o Oracle.
 * Os dados de acesso ficam no arquivo config.properties, na pasta de onde o programa é executado,
 * para que a senha do banco não fique escrita no código.
 */
public class ConexaoBD {

    private static Properties config;

    private static Properties config() {
        if (config == null) {
            Properties p = new Properties();
            try (InputStream in = new FileInputStream("config.properties")) {
                p.load(in);
            } catch (IOException e) {
                throw new IllegalStateException(
                    "Arquivo config.properties não encontrado. Copie o config.properties.example e preencha os dados do Oracle.");
            }
            config = p;
        }
        return config;
    }

    public static Connection getConexao() throws SQLException {
        Properties p = config();
        return DriverManager.getConnection(
            p.getProperty("db.url"),
            p.getProperty("db.usuario"),
            p.getProperty("db.senha"));
    }
}
