package br.com.fiap.dao;

import br.com.fiap.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class Dao {

    // Tempo limite para cada comando SQL
    protected static final int TEMPO_LIMITE_SEGUNDOS = 15;

    protected final Connection conexao;

    // Só fecha a conexão quem a abriu
    private final boolean conexaoPropria;

    protected Dao() throws SQLException {
        this.conexao = ConnectionFactory.getConnection();
        this.conexaoPropria = true;
    }

    protected Dao(Connection conexao) {
        this.conexao = conexao;
        this.conexaoPropria = false;
    }

    protected PreparedStatement prepare(String sql) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(sql);
        stm.setQueryTimeout(TEMPO_LIMITE_SEGUNDOS);
        return stm;
    }

    public void closeConnection() throws SQLException {
        if (conexaoPropria && conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    protected int toNumber(boolean valor) {
        return valor ? 1 : 0;
    }

    protected boolean toBoolean(int valor) {
        return valor == 1;
    }

    protected Timestamp toTimestamp(LocalDateTime data) {
        return data == null ? null : Timestamp.valueOf(data);
    }

    protected LocalDateTime toLocalDateTime(Timestamp data) {
        return data == null ? null : data.toLocalDateTime();
    }
}
