package br.com.fiap.dao;

import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Settings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SettingsDao extends Dao {

    private static final String SELECT =
            "SELECT id_settings, modo_escuro, idioma FROM t_axii_settings";

    public SettingsDao() throws SQLException {
        super();
    }

    SettingsDao(Connection conexao) {
        super(conexao);
    }

    public void insert(Settings settings) throws SQLException {
        if (settings.getId() == null) {
            settings.setId(UUID.randomUUID().toString());
        }
        try (PreparedStatement stm = prepare(
                "INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma) VALUES (?, ?, ?)")) {
            stm.setString(1, settings.getId());
            stm.setInt(2, toNumber(settings.isDarkMode()));
            stm.setString(3, settings.getLanguage());
            stm.executeUpdate();
        }
    }

    public Settings findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE id_settings = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Configuração não encontrada: " + id);
                }
                return parse(result);
            }
        }
    }

    public List<Settings> findAll() throws SQLException {
        List<Settings> lista = new ArrayList<>();
        try (PreparedStatement stm = prepare(SELECT + " ORDER BY idioma");
             ResultSet result = stm.executeQuery()) {
            while (result.next()) {
                lista.add(parse(result));
            }
        }
        return lista;
    }

    public void update(Settings settings) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "UPDATE t_axii_settings SET modo_escuro = ?, idioma = ? WHERE id_settings = ?")) {
            stm.setInt(1, toNumber(settings.isDarkMode()));
            stm.setString(2, settings.getLanguage());
            stm.setString(3, settings.getId());
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Configuração não encontrada: " + settings.getId());
            }
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare("DELETE FROM t_axii_settings WHERE id_settings = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Configuração não encontrada: " + id);
            }
        }
    }

    private Settings parse(ResultSet result) throws SQLException {
        return new Settings(
                result.getString("id_settings"),
                result.getString("idioma"),
                toBoolean(result.getInt("modo_escuro")));
    }
}
