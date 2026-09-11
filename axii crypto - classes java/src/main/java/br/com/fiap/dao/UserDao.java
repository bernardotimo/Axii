package br.com.fiap.dao;

import br.com.fiap.exception.UserEntityNotFoundException;
import br.com.fiap.factory.ConnectionFactory;
import br.com.fiap.model.Identity;
import br.com.fiap.model.Notifications;
import br.com.fiap.model.Settings;
import br.com.fiap.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDao {

    private static final int TEMPO_LIMITE_SEGUNDOS = 15;

    private static final String SELECT_USER =
            "SELECT u.id_user, u.nome, u.email, u.senha, u.data_criacao, u.data_atualizacao, " +
                    "       s.modo_escuro, s.idioma, " +
                    "       n.transacao, n.variacao_preco, n.marketing, " +
                    "       i.id_identity, i.telefone, i.cpf " +
                    "  FROM t_axii_user u " +
                    "  JOIN t_axii_settings s ON s.id_settings = u.id_settings " +
                    "  JOIN t_axii_notifications n ON n.id_notifications = u.id_notifications " +
                    "  JOIN t_axii_identity i ON i.id_identity = u.id_identity";

    private Connection conexao;

    public UserDao() throws SQLException {
        conexao = ConnectionFactory.getConnection();
    }

    public void insert(User user) throws SQLException {
        validateRelations(user);

        if (user.getId() == null) {
            user.setId(UUID.randomUUID().toString());
        }
        if (user.getIdentity().getId() == null) {
            user.getIdentity().setId(UUID.randomUUID().toString());
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        if (user.getUpdatedAt() == null) {
            user.setUpdatedAt(user.getCreatedAt());
        }

        String idSettings = UUID.randomUUID().toString();
        String idNotifications = UUID.randomUUID().toString();

        conexao.setAutoCommit(false);
        try {
            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_settings (id_settings, modo_escuro, idioma) VALUES (?, ?, ?)")) {
                stm.setString(1, idSettings);
                stm.setInt(2, toNumber(user.getSettings().isDarkMode()));
                stm.setString(3, user.getSettings().getLanguage());
                stm.executeUpdate();
            }

            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_notifications (id_notifications, transacao, variacao_preco, marketing) " +
                            "VALUES (?, ?, ?, ?)")) {
                stm.setString(1, idNotifications);
                stm.setInt(2, toNumber(user.getNotifications().isTransaction()));
                stm.setInt(3, toNumber(user.getNotifications().isPriceVariation()));
                stm.setInt(4, toNumber(user.getNotifications().isMarketing()));
                stm.executeUpdate();
            }

            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_identity (id_identity, telefone, cpf) VALUES (?, ?, ?)")) {
                stm.setString(1, user.getIdentity().getId());
                stm.setString(2, user.getIdentity().getPhone());
                stm.setString(3, user.getIdentity().getCpf());
                stm.executeUpdate();
            }

            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_user (id_user, nome, email, senha, data_criacao, data_atualizacao, " +
                            "id_settings, id_notifications, id_identity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                stm.setString(1, user.getId());
                stm.setString(2, user.getName());
                stm.setString(3, user.getEmail());
                stm.setString(4, user.getPassword());
                stm.setTimestamp(5, Timestamp.valueOf(user.getCreatedAt()));
                stm.setTimestamp(6, Timestamp.valueOf(user.getUpdatedAt()));
                stm.setString(7, idSettings);
                stm.setString(8, idNotifications);
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

    public void update(User user) throws SQLException, UserEntityNotFoundException {
        validateRelations(user);
        LocalDateTime updatedAt = LocalDateTime.now();

        conexao.setAutoCommit(false);
        try {
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_user SET nome = ?, email = ?, senha = ?, data_atualizacao = ? " +
                            "WHERE id_user = ?")) {
                stm.setString(1, user.getName());
                stm.setString(2, user.getEmail());
                stm.setString(3, user.getPassword());
                stm.setTimestamp(4, Timestamp.valueOf(updatedAt));
                stm.setString(5, user.getId());
                if (stm.executeUpdate() == 0) {
                    throw new UserEntityNotFoundException("Usuário não encontrado: " + user.getId());
                }
            }

            // Os registros 1:1 são localizados pela FK gravada no próprio usuário
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_settings SET modo_escuro = ?, idioma = ? " +
                            "WHERE id_settings = (SELECT id_settings FROM t_axii_user WHERE id_user = ?)")) {
                stm.setInt(1, toNumber(user.getSettings().isDarkMode()));
                stm.setString(2, user.getSettings().getLanguage());
                stm.setString(3, user.getId());
                stm.executeUpdate();
            }

            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_notifications SET transacao = ?, variacao_preco = ?, marketing = ? " +
                            "WHERE id_notifications = (SELECT id_notifications FROM t_axii_user WHERE id_user = ?)")) {
                stm.setInt(1, toNumber(user.getNotifications().isTransaction()));
                stm.setInt(2, toNumber(user.getNotifications().isPriceVariation()));
                stm.setInt(3, toNumber(user.getNotifications().isMarketing()));
                stm.setString(4, user.getId());
                stm.executeUpdate();
            }

            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_identity SET telefone = ?, cpf = ? " +
                            "WHERE id_identity = (SELECT id_identity FROM t_axii_user WHERE id_user = ?)")) {
                stm.setString(1, user.getIdentity().getPhone());
                stm.setString(2, user.getIdentity().getCpf());
                stm.setString(3, user.getId());
                stm.executeUpdate();
            }

            conexao.commit();
            user.setUpdatedAt(updatedAt);
        } catch (SQLException | UserEntityNotFoundException e) {
            conexao.rollback();
            throw e;
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

            // Contas, chaves Pix e ativos cripto são removidos pelo ON DELETE CASCADE
            deleteById("DELETE FROM t_axii_user WHERE id_user = ?", id);

            // O usuário referencia estas tabelas, por isso elas só podem ser removidas depois dele
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

    public void closeConnection() throws SQLException {
        conexao.close();
    }

    // Prepara o comando com um tempo limite. Sem isso, se outra sessão (por exemplo,
    // o SQL Developer com um DML sem COMMIT) estiver com a linha bloqueada, o programa
    // ficaria travado para sempre esperando o lock, sem exibir nada no console.
    // Estourando o tempo, o Oracle devolve o erro ORA-01013.
    private PreparedStatement prepare(String sql) throws SQLException {
        PreparedStatement stm = conexao.prepareStatement(sql);
        stm.setQueryTimeout(TEMPO_LIMITE_SEGUNDOS);
        return stm;
    }

    private void deleteById(String sql, String id) throws SQLException {
        try (PreparedStatement stm = prepare(sql)) {
            stm.setString(1, id);
            stm.executeUpdate();
        }
    }

    // Converte a linha do ResultSet em um objeto User
    private User parseUser(ResultSet result) throws SQLException {
        Settings settings = new Settings(
                result.getString("idioma"),
                result.getInt("modo_escuro") == 1);

        Notifications notifications = new Notifications(
                result.getInt("transacao") == 1,
                result.getInt("variacao_preco") == 1,
                result.getInt("marketing") == 1);

        Identity identity = new Identity(
                result.getString("id_identity"),
                result.getString("telefone"),
                result.getString("cpf"));

        return new User(
                result.getString("id_user"),
                result.getString("nome"),
                result.getString("email"),
                result.getString("senha"),
                result.getTimestamp("data_atualizacao").toLocalDateTime(),
                result.getTimestamp("data_criacao").toLocalDateTime(),
                notifications, settings, identity);
    }

    private void validateRelations(User user) {
        if (user.getSettings() == null || user.getNotifications() == null || user.getIdentity() == null) {
            throw new IllegalArgumentException(
                    "Settings, Notifications e Identity são obrigatórios para gravar o usuário.");
        }
    }

    private int toNumber(boolean value) {
        return value ? 1 : 0;
    }
}
