package br.com.fiap;

import br.com.fiap.exception.UserEntityNotFoundException;
import br.com.fiap.view.DeleteUser;
import br.com.fiap.view.ListAllUsers;
import br.com.fiap.view.SearchUser;
import br.com.fiap.view.SignInUser;
import br.com.fiap.view.UpdateUser;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    public static void main(String[] args) {
        String id = UUID.randomUUID().toString();

        try {
            SignInUser.insert(id,
                    "Usuário Teste",
                    "teste." + System.currentTimeMillis() + "@axii.com",
                    "Teste@123",
                    "pt-BR",
                    false,
                    true,
                    false,
                    false,
                    "(11) 91234-5678", generateCpf());

            ListAllUsers.list();

            SearchUser.search(id);

            UpdateUser.update(id,
                    "Usuário Teste Atualizado",
                    "teste.atualizado." + System.currentTimeMillis() + "@axii.com",
                    "NovaSenha@456",
                    "en-US", true,
                    true, true, true,
                    "(11) 99999-0000", generateCpf());

            DeleteUser.delete(id);
        } catch (SQLException e) {
            System.err.println("Erro no banco de dados: " + e.getMessage());
        } catch (UserEntityNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    private static String generateCpf() {
        String digits = String.format("%011d", ThreadLocalRandom.current().nextLong(100_000_000_000L));
        return digits.substring(0, 3) + "." + digits.substring(3, 6) + "."
                + digits.substring(6, 9) + "-" + digits.substring(9);
    }
}
