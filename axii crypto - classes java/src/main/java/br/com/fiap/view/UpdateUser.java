package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exception.UserEntityNotFoundException;
import br.com.fiap.model.User;

import java.sql.SQLException;

public class UpdateUser {

    public static void update(String id, String name, String email, String password,
                              String language, boolean darkMode,
                              boolean transaction, boolean priceVariation, boolean marketing,
                              String phone, String cpf) throws SQLException, UserEntityNotFoundException {
        PrintUser.title("ATUALIZAR USUÁRIO");

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
            System.out.println("Usuário atualizado com sucesso! Dados gravados no banco:");
            PrintUser.print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }
}
