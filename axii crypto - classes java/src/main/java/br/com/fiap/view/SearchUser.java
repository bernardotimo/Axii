package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exception.UserEntityNotFoundException;

import java.sql.SQLException;

public class SearchUser {

    public static void search(String id) throws SQLException, UserEntityNotFoundException {
        PrintUser.title("PESQUISAR USUÁRIO POR ID");

        UserDao dao = new UserDao();
        try {
            PrintUser.print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }
}
