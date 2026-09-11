package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;

import java.sql.SQLException;
import java.util.List;

public class ListAllUsers {

    public static void list() throws SQLException {
        PrintUser.title("LISTAR TODOS OS USUÁRIOS");

        UserDao dao = new UserDao();
        try {
            List<User> users = dao.findAll();
            System.out.println("Total de usuários: " + users.size());
            for (User user : users) {
                PrintUser.print(user);
            }
        } finally {
            dao.closeConnection();
        }
    }
}
