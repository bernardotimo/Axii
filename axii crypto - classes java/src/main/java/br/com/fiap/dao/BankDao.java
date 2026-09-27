package br.com.fiap.dao;

import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Bank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankDao extends Dao {

    private static final String SELECT =
            "SELECT id_bank, nome_banco, ativo, numero_conta, agencia, id_user FROM t_axii_bank";

    public BankDao() throws SQLException {
        super();
    }

    BankDao(Connection conexao) {
        super(conexao);
    }

    public void insert(Bank bank) throws SQLException {
        if (bank.getId() == null) {
            bank.setId(UUID.randomUUID().toString());
        }
        try (PreparedStatement stm = prepare(
                "INSERT INTO t_axii_bank (id_bank, nome_banco, ativo, numero_conta, agencia, id_user) "
                        + "VALUES (?, ?, ?, ?, ?, ?)")) {
            stm.setString(1, bank.getId());
            stm.setString(2, bank.getBankName());
            stm.setInt(3, toNumber(bank.isActive()));
            stm.setString(4, bank.getAccountNumber());
            stm.setInt(5, bank.getAgency());
            stm.setString(6, bank.getUserId());
            stm.executeUpdate();
        }
    }

    public Bank findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE id_bank = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Conta bancária não encontrada: " + id);
                }
                return parse(result);
            }
        }
    }

    public List<Bank> findAll() throws SQLException {
        return query(SELECT + " ORDER BY nome_banco", null);
    }

    public List<Bank> findByUser(String userId) throws SQLException {
        return query(SELECT + " WHERE id_user = ? ORDER BY nome_banco", userId);
    }

    public void update(Bank bank) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "UPDATE t_axii_bank SET nome_banco = ?, ativo = ?, numero_conta = ?, agencia = ? "
                        + "WHERE id_bank = ?")) {
            stm.setString(1, bank.getBankName());
            stm.setInt(2, toNumber(bank.isActive()));
            stm.setString(3, bank.getAccountNumber());
            stm.setInt(4, bank.getAgency());
            stm.setString(5, bank.getId());
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Conta bancária não encontrada: " + bank.getId());
            }
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare("DELETE FROM t_axii_bank WHERE id_bank = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Conta bancária não encontrada: " + id);
            }
        }
    }

    private List<Bank> query(String sql, String parametro) throws SQLException {
        List<Bank> lista = new ArrayList<>();
        try (PreparedStatement stm = prepare(sql)) {
            if (parametro != null) {
                stm.setString(1, parametro);
            }
            try (ResultSet result = stm.executeQuery()) {
                while (result.next()) {
                    lista.add(parse(result));
                }
            }
        }
        return lista;
    }

    private Bank parse(ResultSet result) throws SQLException {
        return new Bank(
                result.getString("id_bank"),
                result.getString("nome_banco"),
                toBoolean(result.getInt("ativo")),
                result.getString("numero_conta"),
                result.getInt("agencia"),
                result.getString("id_user"));
    }
}
