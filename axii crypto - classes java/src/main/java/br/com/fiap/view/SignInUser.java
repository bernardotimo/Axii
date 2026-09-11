package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.Identity;
import br.com.fiap.model.Notifications;
import br.com.fiap.model.Settings;
import br.com.fiap.model.User;

import java.sql.SQLException;

public class SignInUser {

    public static void insert(String id, String name, String email, String password,
                              String language, boolean darkMode,
                              boolean transaction, boolean priceVariation, boolean marketing,
                              String phone, String cpf) throws SQLException {
        PrintUser.title("CADASTRAR USUÁRIO");

        User user = new User(id, name, email, password, null, null,
                new Notifications(transaction, priceVariation, marketing),
                new Settings(language, darkMode),
                new Identity(null, phone, cpf));

        UserDao dao = new UserDao();
        try {
            dao.insert(user);
            System.out.println("Usuário cadastrado com sucesso!");
            PrintUser.print(user);
        } finally {
            dao.closeConnection();
        }
    }
}
