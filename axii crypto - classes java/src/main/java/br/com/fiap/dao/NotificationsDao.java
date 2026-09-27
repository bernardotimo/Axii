package br.com.fiap.dao;

import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Notifications;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationsDao extends Dao {

    private static final String SELECT =
            "SELECT id_notifications, transacao, variacao_preco, marketing FROM t_axii_notifications";

    public NotificationsDao() throws SQLException {
        super();
    }

    NotificationsDao(Connection conexao) {
        super(conexao);
    }

    public void insert(Notifications notifications) throws SQLException {
        if (notifications.getId() == null) {
            notifications.setId(UUID.randomUUID().toString());
        }
        try (PreparedStatement stm = prepare(
                "INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing) "
                        + "VALUES (?, ?, ?, ?)")) {
            stm.setString(1, notifications.getId());
            stm.setInt(2, toNumber(notifications.isTransaction()));
            stm.setInt(3, toNumber(notifications.isPriceVariation()));
            stm.setInt(4, toNumber(notifications.isMarketing()));
            stm.executeUpdate();
        }
    }

    public Notifications findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE id_notifications = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Preferência de notificação não encontrada: " + id);
                }
                return parse(result);
            }
        }
    }

    public List<Notifications> findAll() throws SQLException {
        List<Notifications> lista = new ArrayList<>();
        try (PreparedStatement stm = prepare(SELECT);
             ResultSet result = stm.executeQuery()) {
            while (result.next()) {
                lista.add(parse(result));
            }
        }
        return lista;
    }

    public void update(Notifications notifications) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "UPDATE t_axii_notifications SET transacao = ?, variacao_preco = ?, marketing = ? "
                        + "WHERE id_notifications = ?")) {
            stm.setInt(1, toNumber(notifications.isTransaction()));
            stm.setInt(2, toNumber(notifications.isPriceVariation()));
            stm.setInt(3, toNumber(notifications.isMarketing()));
            stm.setString(4, notifications.getId());
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException(
                        "Preferência de notificação não encontrada: " + notifications.getId());
            }
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "DELETE FROM t_axii_notifications WHERE id_notifications = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Preferência de notificação não encontrada: " + id);
            }
        }
    }

    private Notifications parse(ResultSet result) throws SQLException {
        return new Notifications(
                result.getString("id_notifications"),
                toBoolean(result.getInt("transacao")),
                toBoolean(result.getInt("variacao_preco")),
                toBoolean(result.getInt("marketing")));
    }
}
