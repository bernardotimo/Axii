package br.com.fiap.dao;

import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.exception.UserEntityNotFoundException;
import br.com.fiap.model.Identity;
import br.com.fiap.model.Notifications;
import br.com.fiap.model.Settings;
import br.com.fiap.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao extends Dao {

    private static final String SELECT_USER =
            "SELECT u.id_user, u.nome, u.email, u.senha, u.data_criacao, u.data_atualizacao, "
                    + "       s.id_settings, s.modo_escuro, s.idioma, "
                    + "       n.id_notifications, n.transacao, n.variacao_preco, n.marketing, "
                    + "       i.id_identity, i.telefone, i.cpf "
                    + "  FROM t_axii_user u "
                    + "  JOIN t_axii_settings s      ON s.id_settings      = u.id_settings "
                    + "  JOIN t_axii_notifications n ON n.id_notifications = u.id_notifications "
                    + "  JOIN t_axii_identity i      ON i.id_identity      = u.id_identity";

    private final SettingsDao settingsDao;
    private final NotificationsDao notificationsDao;
    private final IdentityDao identityDao;

    public UserDao() throws SQLException {
        super();
        this.settingsDao = new SettingsDao(conexao);
        this.notificationsDao = new NotificationsDao(conexao);
        this.identityDao = new IdentityDao(conexao);
    }

    public void insert(User user) throws SQLException {
        validateRelations(user);

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(user.getCreatedAt());
        }

        conexao.setAutoCommit(false);
        try {
            settingsDao.insert(user.getSettings());
            notificationsDao.insert(user.getNotifications());
            identityDao.insert(user.getIdentity());

            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao, "
                            + "id_settings, id_notifications, id_identity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stm.setString(1, user.getId());
                stm.setString(2, user.getName());
                stm.setString(3, user.getEmail());
                stm.setString(4, user.getPassword());
                stm.setTimestamp(5, toTimestamp(user.getCreatedAt()));
                stm.setTimestamp(6, toTimestamp(user.getUpdatedAt()));
                stm.setString(7, user.getSettings().getId());
                stm.setString(8, user.getNotifications().getId());
                stm.setString(9, user.getIdentity().getId());
                stm.executeUpdate();
            }

            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public User findById(String id) throws SQLException, UserEntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT_USER + " WHERE u.id_user = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new UserEntityNotFoundException("Usuário não encontrado: " + id);
                }
                return parseUser(result);
            }
        }
    }

    public User findByEmail(String email) throws SQLException, UserEntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT_USER + " WHERE u.email = ?")) {
            stm.setString(1, email);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new UserEntityNotFoundException("Nenhum usuário com o e-mail " + email);
                }
                return parseUser(result);
            }
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (PreparedStatement stm = prepare(SELECT_USER + " ORDER BY u.nome");
             ResultSet result = stm.executeQuery()) {
            while (result.next()) {
                users.add(parseUser(result));
            }
        }
        return users;
    }

    public User findByIdWithRelations(String id) throws SQLException, UserEntityNotFoundException {
        User user = findById(id);

        BankDao bankDao = new BankDao(conexao);
        PixKeyDao pixKeyDao = new PixKeyDao(conexao);
        CryptoAssetDao cryptoAssetDao = new CryptoAssetDao(conexao);

        user.setBanks(bankDao.findByUser(id));
        user.setPixKeys(pixKeyDao.findByUser(id));
        user.setCryptoAssets(cryptoAssetDao.findByUser(id));

        return user;
    }

    public void update(User user) throws SQLException, UserEntityNotFoundException {
        validateRelations(user);
        LocalDateTime updatedAt = LocalDateTime.now();

        conexao.setAutoCommit(false);
        try {
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_user SET nome = ?, email = ?, senha = ?, data_atualizacao = ? "
                            + "WHERE id_user = ?")) {
                stm.setString(1, user.getName());
                stm.setString(2, user.getEmail());
                stm.setString(3, user.getPassword());
                stm.setTimestamp(4, toTimestamp(updatedAt));
                stm.setString(5, user.getId());
                if (stm.executeUpdate() == 0) {
                    throw new UserEntityNotFoundException("Usuário não encontrado: " + user.getId());
                }
            }

            settingsDao.update(user.getSettings());
            notificationsDao.update(user.getNotifications());
            identityDao.update(user.getIdentity());

            conexao.commit();
            user.setUpdatedAt(updatedAt);
        } catch (SQLException | UserEntityNotFoundException e) {
            conexao.rollback();
            throw e;
        } catch (EntityNotFoundException e) {
            conexao.rollback();
            throw new SQLException("Dados relacionados do usuário não encontrados: " + e.getMessage(), e);
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public void delete(String id) throws SQLException, UserEntityNotFoundException {
        conexao.setAutoCommit(false);
        try {
            String idSettings;
            String idNotifications;
            String idIdentity;

            try (PreparedStatement stm = prepare(
                    "SELECT id_settings, id_notifications, id_identity FROM t_axii_user WHERE id_user = ?")) {
                stm.setString(1, id);
                try (ResultSet result = stm.executeQuery()) {
                    if (!result.next()) {
                        throw new UserEntityNotFoundException("Usuário não encontrado: " + id);
                    }
                    idSettings = result.getString("id_settings");
                    idNotifications = result.getString("id_notifications");
                    idIdentity = result.getString("id_identity");
                }
            }

            deleteById("DELETE FROM t_axii_user WHERE id_user = ?", id);

            deleteById("DELETE FROM t_axii_settings WHERE id_settings = ?", idSettings);
            deleteById("DELETE FROM t_axii_notifications WHERE id_notifications = ?", idNotifications);
            deleteById("DELETE FROM t_axii_identity WHERE id_identity = ?", idIdentity);

            conexao.commit();
        } catch (SQLException | UserEntityNotFoundException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public int count() throws SQLException {
        try (PreparedStatement stm = prepare("SELECT COUNT(*) AS total FROM t_axii_user");
             ResultSet result = stm.executeQuery()) {
            return result.next() ? result.getInt("total") : 0;
        }
    }

    private void deleteById(String sql, String id) throws SQLException {
        try (PreparedStatement stm = prepare(sql)) {
            stm.setString(1, id);
            stm.executeUpdate();
        }
    }

    private User parseUser(ResultSet result) throws SQLException {
        Settings settings = new Settings(
                result.getString("id_settings"),
                result.getString("idioma"),
                toBoolean(result.getInt("modo_escuro")));

        Notifications notifications = new Notifications(
                result.getString("id_notifications"),
                toBoolean(result.getInt("transacao")),
                toBoolean(result.getInt("variacao_preco")),
                toBoolean(result.getInt("marketing")));

        Identity identity = new Identity(
                result.getString("id_identity"),
                result.getString("telefone"),
                result.getString("cpf"));

        return new User(
                result.getString("id_user"),
                result.getString("nome"),
                result.getString("email"),
                result.getString("senha"),
                toLocalDateTime(result.getTimestamp("data_atualizacao")),
                toLocalDateTime(result.getTimestamp("data_criacao")),
                notifications, settings, identity);
    }

    private void validateRelations(User user) {
        if (user.getSettings() == null || user.getNotifications() == null || user.getIdentity() == null) {
            throw new IllegalArgumentException(
                    "Settings, Notifications e Identity são obrigatórios para gravar o usuário.");
        }
    }
}
