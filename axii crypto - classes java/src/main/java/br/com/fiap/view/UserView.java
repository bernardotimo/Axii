package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exception.UserEntityNotFoundException;
import br.com.fiap.model.*;
import br.com.fiap.records.TempoDecorrido;
import br.com.fiap.security.AntiInjecaoSql;

import java.sql.SQLException;
import java.util.List;

public class UserView {

    public static void insert(String id, String name, String email, String password,
                              String language, boolean darkMode,
                              boolean transaction, boolean priceVariation, boolean marketing,
                              String phone, String cpf) throws SQLException {
        Console.titulo("Cadastrar usuário");

        id = AntiInjecaoSql.uuid("id", id);
        name = AntiInjecaoSql.texto("nome", name, 100);
        email = AntiInjecaoSql.email("e-mail", email);
        password = AntiInjecaoSql.senha("senha", password);
        language = AntiInjecaoSql.texto("idioma", language, 10);
        phone = AntiInjecaoSql.telefone("telefone", phone);
        cpf = AntiInjecaoSql.cpf("CPF", cpf);

        User user = new User(id, name, email, password, null, null,
                new Notifications(transaction, priceVariation, marketing),
                new Settings(language, darkMode),
                new Identity(null, phone, cpf));

        UserDao dao = new UserDao();
        try {
            dao.insert(user);
            Console.sucesso("Usuário cadastrado com sucesso!");
            print(user);
        } finally {
            dao.closeConnection();
        }
    }

    public static void list() throws SQLException {
        Console.titulo("Listar todos os usuários");

        UserDao dao = new UserDao();
        try {
            List<User> users = dao.findAll();
            Console.info("Total de usuários: " + users.size());
            for (User user : users) {
                print(user);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void search(String id) throws SQLException, UserEntityNotFoundException {
        Console.titulo("Pesquisar usuário por ID");
        id = AntiInjecaoSql.uuid("id", id);

        UserDao dao = new UserDao();
        try {
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void searchByEmail(String email) throws SQLException, UserEntityNotFoundException {
        Console.titulo("Pesquisar usuário por e-mail");
        email = AntiInjecaoSql.email("e-mail", email);

        UserDao dao = new UserDao();
        try {
            print(dao.findByEmail(email));
        } finally {
            dao.closeConnection();
        }
    }

    public static void showComplete(String id) throws SQLException, UserEntityNotFoundException {
        Console.titulo("Ficha completa do usuário");
        id = AntiInjecaoSql.uuid("id", id);

        UserDao dao = new UserDao();
        try {
            User user = dao.findByIdWithRelations(id);
            print(user);

            Console.info("\n  Contas bancárias (" + user.getBanks().size() + "):");
            for (Bank bank : user.getBanks()) {
                BankView.printResumo(bank);
            }

            Console.info("\n  Chaves Pix (" + user.getPixKeys().size() + "):");
            for (PixKey pixKey : user.getPixKeys()) {
                PixKeyView.printResumo(pixKey);
            }

            Console.info("\n  Ativos de cripto (" + user.getCryptoAssets().size() + "):");
            for (CryptoAsset ativo : user.getCryptoAssets()) {
                CryptoAssetView.printResumo(ativo);
            }

            Console.separador();
            Console.campo("Patrimônio em cripto", Console.moeda(user.getPortfolioValue()));
        } finally {
            dao.closeConnection();
        }
    }

    public static void update(String id, String name, String email, String password,
                              String language, boolean darkMode,
                              boolean transaction, boolean priceVariation, boolean marketing,
                              String phone, String cpf) throws SQLException, UserEntityNotFoundException {
        Console.titulo("Atualizar usuário");

        id = AntiInjecaoSql.uuid("id", id);
        name = AntiInjecaoSql.texto("nome", name, 100);
        email = AntiInjecaoSql.email("e-mail", email);
        password = AntiInjecaoSql.senha("senha", password);
        language = AntiInjecaoSql.texto("idioma", language, 10);
        phone = AntiInjecaoSql.telefone("telefone", phone);
        cpf = AntiInjecaoSql.cpf("CPF", cpf);

        UserDao dao = new UserDao();
        try {
            User user = dao.findById(id);

            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.getSettings().setLanguage(language);
            user.getSettings().setDarkMode(darkMode);
            user.getNotifications().setTransaction(transaction);
            user.getNotifications().setPriceVariation(priceVariation);
            user.getNotifications().setMarketing(marketing);
            user.getIdentity().setPhone(phone);
            user.getIdentity().setCpf(cpf);

            dao.update(user);
            Console.sucesso("Usuário atualizado! Dados relidos do banco:");
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void delete(String id) throws SQLException, UserEntityNotFoundException {
        Console.titulo("Remover usuário");
        id = AntiInjecaoSql.uuid("id", id);

        UserDao dao = new UserDao();
        try {
            dao.delete(id);
            Console.sucesso("Usuário removido com sucesso!");
            try {
                dao.findById(id);
                Console.erro("Falha: o usuário ainda existe no banco.");
            } catch (UserEntityNotFoundException e) {
                Console.sucesso("Remoção confirmada. " + e.getMessage());
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void print(User user) {
        Console.separador();
        Console.campo("ID", user.getId());
        Console.campo("Nome", user.getName());
        Console.campo("E-mail", user.getEmail());
        Console.campo("Senha", Console.mascarar(user.getPassword()));
        Console.campo("Criado em", Console.data(user.getCreatedAt()));
        Console.campo("Atualizado em", Console.data(user.getUpdatedAt()));

        TempoDecorrido idadeDaConta = user.getAccountAge();
        if (idadeDaConta != null) {
            Console.campo("Conta criada há", idadeDaConta.descricao()
                    + (idadeDaConta.isRecente() ? " (conta nova)" : ""));
        }

        Console.campo("CPF", user.getIdentity().getCpf());
        Console.campo("Telefone", user.getIdentity().getPhone());
        Console.campo("Modo escuro", Console.simNao(user.getSettings().isDarkMode()));
        Console.campo("Idioma", user.getSettings().getLanguage());
        Console.campo("Notif. transação", Console.simNao(user.getNotifications().isTransaction()));
        Console.campo("Notif. variação", Console.simNao(user.getNotifications().isPriceVariation()));
        Console.campo("Notif. marketing", Console.simNao(user.getNotifications().isMarketing()));
    }
}
