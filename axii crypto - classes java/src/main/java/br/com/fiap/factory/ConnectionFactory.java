package br.com.fiap.factory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final String ARQUIVO_PROPRIEDADES = "db.properties";

    private static final String URL;
    private static final String USUARIO;
    private static final String SENHA;

    static {
        Properties propriedades = new Properties();
        try (InputStream input = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream(ARQUIVO_PROPRIEDADES)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Arquivo '" + ARQUIVO_PROPRIEDADES + "' não encontrado em src/main/env. " +
                                "Crie o arquivo com db.url, db.user e db.password preenchidos com suas credenciais.");
            }
            propriedades.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler o arquivo '" + ARQUIVO_PROPRIEDADES + "'.", e);
        }

        URL = propriedades.getProperty("db.url");
        USUARIO = propriedades.getProperty("db.user");
        SENHA = propriedades.getProperty("db.password");
    }

    // Método para obter uma conexão com o banco de dados
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

}
