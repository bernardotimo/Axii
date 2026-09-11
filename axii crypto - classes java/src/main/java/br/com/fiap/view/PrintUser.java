package br.com.fiap.view;

import br.com.fiap.model.User;

import java.time.format.DateTimeFormatter;

public class PrintUser {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void title(String title) {
        System.out.println();
        System.out.println("============ " + title + " ============");
    }

    public static void print(User user) {
        System.out.println("----------------------------------------------");
        System.out.println("ID ................: " + user.getId());
        System.out.println("Nome ..............: " + user.getName());
        System.out.println("E-mail ............: " + user.getEmail());
        System.out.println("Senha .............: " + "*".repeat(user.getPassword().length()));
        System.out.println("Criado em .........: " + user.getCreatedAt().format(DATE_FORMAT));
        System.out.println("Atualizado em .....: " + user.getUpdatedAt().format(DATE_FORMAT));
        System.out.println("CPF ...............: " + user.getIdentity().getCpf());
        System.out.println("Telefone ..........: " + user.getIdentity().getPhone());
        System.out.println("Modo escuro .......: " + yesNo(user.getSettings().isDarkMode()));
        System.out.println("Idioma ............: " + user.getSettings().getLanguage());
        System.out.println("Notif. transação ..: " + yesNo(user.getNotifications().isTransaction()));
        System.out.println("Notif. variação ...: " + yesNo(user.getNotifications().isPriceVariation()));
        System.out.println("Notif. marketing ..: " + yesNo(user.getNotifications().isMarketing()));
    }

    private static String yesNo(boolean value) {
        return value ? "Sim" : "Não";
    }
}
