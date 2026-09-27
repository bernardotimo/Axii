package br.com.fiap.dao;

import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Identity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IdentityDao extends Dao {

    private static final String SELECT =
            "SELECT id_identity, telefone, cpf FROM t_axii_identity";

    public IdentityDao() throws SQLException {
        super();
    }

    IdentityDao(Connection conexao) {
        super(conexao);
    }

    public void insert(Identity identity) throws SQLException {
        if (identity.getId() == null) {
            identity.setId(UUID.randomUUID().toString());
        }
        try (PreparedStatement stm = prepare(
                "INSERT INTO t_axii_identity (id_identity, telefone, cpf) VALUES (?, ?, ?)")) {
            stm.setString(1, identity.getId());
            stm.setString(2, identity.getPhone());
            stm.setString(3, identity.getCpf());
            stm.executeUpdate();
        }
    }

    public Identity findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE id_identity = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Identidade não encontrada: " + id);
                }
                return parse(result);
            }
        }
    }

    public Identity findByCpf(String cpf) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE cpf = ?")) {
            stm.setString(1, cpf);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Nenhuma identidade com o CPF " + cpf);
                }
                return parse(result);
            }
        }
    }

    public List<Identity> findAll() throws SQLException {
        List<Identity> lista = new ArrayList<>();
        try (PreparedStatement stm = prepare(SELECT + " ORDER BY cpf");
             ResultSet result = stm.executeQuery()) {
            while (result.next()) {
                lista.add(parse(result));
            }
        }
        return lista;
    }

    public void update(Identity identity) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "UPDATE t_axii_identity SET telefone = ?, cpf = ? WHERE id_identity = ?")) {
            stm.setString(1, identity.getPhone());
            stm.setString(2, identity.getCpf());
            stm.setString(3, identity.getId());
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Identidade não encontrada: " + identity.getId());
            }
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare("DELETE FROM t_axii_identity WHERE id_identity = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Identidade não encontrada: " + id);
            }
        }
    }

    private Identity parse(ResultSet result) throws SQLException {
        return new Identity(
                result.getString("id_identity"),
                result.getString("telefone"),
                result.getString("cpf"));
    }
}
