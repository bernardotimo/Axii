package br.com.fiap.view;

import br.com.fiap.dao.IdentityDao;
import br.com.fiap.dao.NotificationsDao;
import br.com.fiap.dao.SettingsDao;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Identity;
import br.com.fiap.model.Notifications;
import br.com.fiap.model.Settings;
import br.com.fiap.security.AntiInjecaoSql;

import java.sql.SQLException;
import java.util.List;

public class ProfileView {

    public static void listSettings() throws SQLException {
        Console.titulo("Listar configurações");

        SettingsDao dao = new SettingsDao();
        try {
            List<Settings> lista = dao.findAll();
            Console.info("Total de registros: " + lista.size());
            for (Settings settings : lista) {
                printSettings(settings);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void searchSettings(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar configuração por ID");
        id = AntiInjecaoSql.uuid("id", id);

        SettingsDao dao = new SettingsDao();
        try {
            printSettings(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void updateSettings(String id, String language, boolean darkMode)
            throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar configuração");

        id = AntiInjecaoSql.uuid("id", id);
        language = AntiInjecaoSql.texto("idioma", language, 10);

        SettingsDao dao = new SettingsDao();
        try {
            Settings settings = dao.findById(id);
            settings.setLanguage(language);
            settings.setDarkMode(darkMode);

            dao.update(settings);
            Console.sucesso("Configuração atualizada! Dados relidos do banco:");
            printSettings(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void listNotifications() throws SQLException {
        Console.titulo("Listar preferências de notificação");

        NotificationsDao dao = new NotificationsDao();
        try {
            List<Notifications> lista = dao.findAll();
            Console.info("Total de registros: " + lista.size());
            for (Notifications notifications : lista) {
                printNotifications(notifications);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void searchNotifications(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar notificação por ID");
        id = AntiInjecaoSql.uuid("id", id);

        NotificationsDao dao = new NotificationsDao();
        try {
            printNotifications(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void updateNotifications(String id, boolean transaction,
                                           boolean priceVariation, boolean marketing)
            throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar preferências de notificação");
        id = AntiInjecaoSql.uuid("id", id);

        NotificationsDao dao = new NotificationsDao();
        try {
            Notifications notifications = dao.findById(id);
            notifications.setTransaction(transaction);
            notifications.setPriceVariation(priceVariation);
            notifications.setMarketing(marketing);

            dao.update(notifications);
            Console.sucesso("Preferências atualizadas! Dados relidos do banco:");
            printNotifications(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void listIdentities() throws SQLException {
        Console.titulo("Listar identidades");

        IdentityDao dao = new IdentityDao();
        try {
            List<Identity> lista = dao.findAll();
            Console.info("Total de registros: " + lista.size());
            for (Identity identity : lista) {
                printIdentity(identity);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void searchIdentity(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar identidade por ID");
        id = AntiInjecaoSql.uuid("id", id);

        IdentityDao dao = new IdentityDao();
        try {
            printIdentity(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void searchIdentityByCpf(String cpf) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar identidade por CPF");
        cpf = AntiInjecaoSql.cpf("CPF", cpf);

        IdentityDao dao = new IdentityDao();
        try {
            printIdentity(dao.findByCpf(cpf));
        } finally {
            dao.closeConnection();
        }
    }

    public static void updateIdentity(String id, String phone, String cpf)
            throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar identidade");

        id = AntiInjecaoSql.uuid("id", id);
        phone = AntiInjecaoSql.telefone("telefone", phone);
        cpf = AntiInjecaoSql.cpf("CPF", cpf);

        IdentityDao dao = new IdentityDao();
        try {
            Identity identity = dao.findById(id);
            identity.setPhone(phone);
            identity.setCpf(cpf);

            dao.update(identity);
            Console.sucesso("Identidade atualizada! Dados relidos do banco:");
            printIdentity(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void printSettings(Settings settings) {
        Console.separador();
        Console.campo("ID", settings.getId());
        Console.campo("Idioma", settings.getLanguage());
        Console.campo("Modo escuro", Console.simNao(settings.isDarkMode()));
    }

    public static void printNotifications(Notifications notifications) {
        Console.separador();
        Console.campo("ID", notifications.getId());
        Console.campo("Transação", Console.simNao(notifications.isTransaction()));
        Console.campo("Variação de preço", Console.simNao(notifications.isPriceVariation()));
        Console.campo("Marketing", Console.simNao(notifications.isMarketing()));
    }

    public static void printIdentity(Identity identity) {
        Console.separador();
        Console.campo("ID", identity.getId());
        Console.campo("Telefone", identity.getPhone());
        Console.campo("CPF", identity.getCpf());
    }
}
