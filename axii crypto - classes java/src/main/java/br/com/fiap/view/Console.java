package br.com.fiap.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public final class Console {

    public static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter DATA_HORA_CURTA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Scanner ENTRADA = new Scanner(System.in);
    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    private Console() {
    }

    public static void titulo(String titulo) {
        System.out.println();
        System.out.println("=".repeat(62));
        System.out.println("  " + titulo.toUpperCase());
        System.out.println("=".repeat(62));
    }

    public static void separador() {
        System.out.println("-".repeat(62));
    }

    public static void sucesso(String mensagem) {
        System.out.println("[OK] " + mensagem);
    }

    public static void erro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    public static void aviso(String mensagem) {
        System.out.println("[!] " + mensagem);
    }

    public static void info(String mensagem) {
        System.out.println(mensagem);
    }

    public static void campo(String rotulo, Object valor) {
        System.out.printf("  %-22s %s%n", rotulo + " ", valor);
    }

    public static String moeda(double valor) {
        return String.format(BRASIL, "R$ %,.2f", valor);
    }

    public static String quantidade(double valor) {
        return String.format(BRASIL, "%,.8f", valor);
    }

    public static String simNao(boolean valor) {
        return valor ? "Sim" : "Não";
    }

    public static String mascarar(String senha) {
        return senha == null ? "" : "*".repeat(senha.length());
    }

    public static String data(LocalDateTime data) {
        return data == null ? "-" : data.format(DATA_HORA);
    }

    public static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        lerLinha();
    }

    private static String lerLinha() {
        return ENTRADA.hasNextLine() ? ENTRADA.nextLine() : "";
    }

    public static String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        return lerLinha().trim();
    }

    public static String lerTexto(String rotulo, String valorAtual) {
        System.out.print(rotulo + " [" + valorAtual + "]: ");
        String digitado = lerLinha().trim();
        return digitado.isEmpty() ? valorAtual : digitado;
    }

    public static int lerInteiro(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String digitado = lerLinha().trim();
            try {
                return Integer.parseInt(digitado);
            } catch (NumberFormatException e) {
                erro("Digite um número inteiro válido.");
            }
        }
    }

    public static int lerInteiro(String rotulo, int valorAtual) {
        System.out.print(rotulo + " [" + valorAtual + "]: ");
        String digitado = lerLinha().trim();
        if (digitado.isEmpty()) {
            return valorAtual;
        }
        try {
            return Integer.parseInt(digitado);
        } catch (NumberFormatException e) {
            erro("Valor inválido, mantendo " + valorAtual + ".");
            return valorAtual;
        }
    }

    public static double lerDecimal(String rotulo) {
        while (true) {
            System.out.print(rotulo + ": ");
            String digitado = lerLinha().trim().replace(",", ".");
            try {
                return Double.parseDouble(digitado);
            } catch (NumberFormatException e) {
                erro("Digite um número válido (ex.: 1500.50).");
            }
        }
    }

    public static double lerDecimal(String rotulo, double valorAtual) {
        System.out.print(rotulo + " [" + valorAtual + "]: ");
        String digitado = lerLinha().trim().replace(",", ".");
        if (digitado.isEmpty()) {
            return valorAtual;
        }
        try {
            return Double.parseDouble(digitado);
        } catch (NumberFormatException e) {
            erro("Valor inválido, mantendo " + valorAtual + ".");
            return valorAtual;
        }
    }

    public static boolean lerSimNao(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (s/n): ");
            String digitado = lerLinha().trim().toLowerCase();
            if (digitado.equals("s") || digitado.equals("sim")) {
                return true;
            }
            if (digitado.equals("n") || digitado.equals("nao") || digitado.equals("não")) {
                return false;
            }
            erro("Responda com s ou n.");
        }
    }

    public static boolean lerSimNao(String rotulo, boolean valorAtual) {
        System.out.print(rotulo + " (s/n) [" + simNao(valorAtual) + "]: ");
        String digitado = lerLinha().trim().toLowerCase();
        if (digitado.isEmpty()) {
            return valorAtual;
        }
        return digitado.startsWith("s");
    }

    public static LocalDateTime lerDataHora(String rotulo) {
        while (true) {
            System.out.print(rotulo + " (dd/MM/aaaa HH:mm, ENTER = agora): ");
            String digitado = lerLinha().trim();
            if (digitado.isEmpty()) {
                return LocalDateTime.now();
            }
            try {
                return LocalDateTime.parse(digitado, DATA_HORA_CURTA);
            } catch (DateTimeParseException e) {
                erro("Data inválida. Exemplo esperado: 15/01/2026 09:30");
            }
        }
    }

    public static <T extends Enum<T>> T lerEnum(String rotulo, T[] valores) {
        System.out.println(rotulo + ":");
        for (int i = 0; i < valores.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, valores[i]);
        }
        while (true) {
            int escolha = lerInteiro("Escolha");
            if (escolha >= 1 && escolha <= valores.length) {
                return valores[escolha - 1];
            }
            erro("Escolha entre 1 e " + valores.length + ".");
        }
    }
}
