package br.com.fiap.view;

import br.com.fiap.dao.UserDao;
import br.com.fiap.exception.UserEntityNotFoundException;

import java.sql.SQLException;

public class DeleteUser {

    public static void delete(String id) throws SQLException, UserEntityNotFoundException {
        PrintUser.title("REMOVER USUÁRIO");

        UserDao dao = new UserDao();
        try {
            dao.delete(id);
            System.out.println("Usuário removido com sucesso!");
            try {
                dao.findById(id);
                System.err.println("Falha: o usuário ainda existe no banco.");
            } catch (UserEntityNotFoundException e) {
                System.out.println("Remoção confirmada. " + e.getMessage());
            }
        } finally {
            dao.closeConnection();
        }
    }
}
