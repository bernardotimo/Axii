package br.com.fiap.dao;

import br.com.fiap.enums.TipoChavePix;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.PixKey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PixKeyDao extends Dao {

    private static final String SELECT =
            "SELECT id_pix_key, chave, tipo, id_user FROM t_axii_pix_key";

    public PixKeyDao() throws SQLException {
        super();
    }

    PixKeyDao(Connection conexao) {
        super(conexao);
    }

    public void insert(PixKey pixKey) throws SQLException {
        if (pixKey.getId() == null) {
            pixKey.setId(UUID.randomUUID().toString());
        }
        try (PreparedStatement stm = prepare(
                "INSERT INTO t_axii_pix_key (id_pix_key, chave, tipo, id_user) VALUES (?, ?, ?, ?)")) {
            stm.setString(1, pixKey.getId());
            stm.setString(2, pixKey.getKey());
            stm.setString(3, pixKey.getType().getCodigo());
            stm.setString(4, pixKey.getUserId());
            stm.executeUpdate();
        }
    }

    public PixKey findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE id_pix_key = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Chave Pix não encontrada: " + id);
                }
                return parse(result);
            }
        }
    }

    public List<PixKey> findAll() throws SQLException {
        return query(SELECT + " ORDER BY tipo, chave", null);
    }

    public List<PixKey> findByUser(String userId) throws SQLException {
        return query(SELECT + " WHERE id_user = ? ORDER BY tipo, chave", userId);
    }

    public List<PixKey> findByType(TipoChavePix tipo) throws SQLException {
        return query(SELECT + " WHERE tipo = ? ORDER BY chave", tipo.getCodigo());
    }

    public void update(PixKey pixKey) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "UPDATE t_axii_pix_key SET chave = ?, tipo = ? WHERE id_pix_key = ?")) {
            stm.setString(1, pixKey.getKey());
            stm.setString(2, pixKey.getType().getCodigo());
            stm.setString(3, pixKey.getId());
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Chave Pix não encontrada: " + pixKey.getId());
            }
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare("DELETE FROM t_axii_pix_key WHERE id_pix_key = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Chave Pix não encontrada: " + id);
            }
        }
    }

    private List<PixKey> query(String sql, String parametro) throws SQLException {
        List<PixKey> lista = new ArrayList<>();
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

    private PixKey parse(ResultSet result) throws SQLException {
        return new PixKey(
                result.getString("id_pix_key"),
                result.getString("chave"),
                TipoChavePix.fromCodigo(result.getString("tipo")),
                result.getString("id_user"));
    }
}
