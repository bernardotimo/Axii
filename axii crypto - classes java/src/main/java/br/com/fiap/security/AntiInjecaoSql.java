package br.com.fiap.security;

import br.com.fiap.exception.SqlInjectionException;

import java.util.regex.Pattern;

public final class AntiInjecaoSql {

    private static final Pattern CARACTERES_PERIGOSOS =
            Pattern.compile("['\"`;]|--|/\\*|\\*/");

    private static final Pattern COMANDOS_SQL = Pattern.compile(
            "(?i)\\b(select|insert|update|delete|drop|alter|create|truncate|"
                    + "union|exec|execute|merge|grant|revoke|declare|shutdown)\\b");

    private static final Pattern TAUTOLOGIA =
            Pattern.compile("(?i)\\b(or|and)\\b\\s*[\\w'\"]+\\s*(=|<>|like)\\s*[\\w'\"]+");

    private static final Pattern FORMATO_UUID =
            Pattern.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

    private static final Pattern FORMATO_EMAIL =
            Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    private static final Pattern FORMATO_CPF =
            Pattern.compile("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");

    private static final Pattern FORMATO_TELEFONE =
            Pattern.compile("\\(\\d{2}\\) \\d{4,5}-\\d{4}");

    private AntiInjecaoSql() {
    }

    public static String texto(String campo, String valor, int tamanhoMaximo) {
        if (valor == null || valor.isBlank()) {
            throw new SqlInjectionException("O campo '" + campo + "' é obrigatório.");
        }
        String limpo = valor.trim();

        if (limpo.length() > tamanhoMaximo) {
            throw new SqlInjectionException("O campo '" + campo + "' deve ter no máximo "
                    + tamanhoMaximo + " caracteres.");
        }
        if (CARACTERES_PERIGOSOS.matcher(limpo).find()) {
            throw new SqlInjectionException(bloqueio(campo, "caracteres não permitidos (aspas, ; ou comentário SQL)"));
        }
        if (COMANDOS_SQL.matcher(limpo).find()) {
            throw new SqlInjectionException(bloqueio(campo, "comando SQL"));
        }
        if (TAUTOLOGIA.matcher(limpo).find()) {
            throw new SqlInjectionException(bloqueio(campo, "condição sempre verdadeira"));
        }
        return limpo;
    }

    public static String senha(String campo, String valor) {
        if (valor == null || valor.length() < 6) {
            throw new SqlInjectionException("O campo '" + campo + "' deve ter no mínimo 6 caracteres.");
        }
        if (valor.length() > 255) {
            throw new SqlInjectionException("O campo '" + campo + "' deve ter no máximo 255 caracteres.");
        }
        if (CARACTERES_PERIGOSOS.matcher(valor).find()) {
            throw new SqlInjectionException(bloqueio(campo, "caracteres não permitidos (aspas, ; ou comentário SQL)"));
        }
        if (COMANDOS_SQL.matcher(valor).find()) {
            throw new SqlInjectionException(bloqueio(campo, "comando SQL"));
        }
        return valor;
    }

    public static String uuid(String campo, String valor) {
        return comFormato(campo, valor, FORMATO_UUID, "um identificador UUID");
    }

    public static String email(String campo, String valor) {
        return comFormato(campo, valor, FORMATO_EMAIL, "um e-mail (ex.: usuario@email.com)");
    }

    public static String cpf(String campo, String valor) {
        return comFormato(campo, valor, FORMATO_CPF, "um CPF no formato 000.000.000-00");
    }

    public static String telefone(String campo, String valor) {
        return comFormato(campo, valor, FORMATO_TELEFONE, "um telefone no formato (00) 00000-0000");
    }

    public static double numeroPositivo(String campo, double valor) {
        if (valor < 0) {
            throw new SqlInjectionException("O campo '" + campo + "' não pode ser negativo.");
        }
        return valor;
    }

    public static int inteiroPositivo(String campo, int valor) {
        if (valor <= 0) {
            throw new SqlInjectionException("O campo '" + campo + "' deve ser maior que zero.");
        }
        return valor;
    }

    private static String comFormato(String campo, String valor, Pattern formato, String esperado) {
        if (valor == null || valor.isBlank()) {
            throw new SqlInjectionException("O campo '" + campo + "' é obrigatório.");
        }
        String limpo = valor.trim();
        if (!formato.matcher(limpo).matches()) {
            throw new SqlInjectionException("O campo '" + campo + "' deve ser " + esperado + ".");
        }
        return limpo;
    }

    private static String bloqueio(String campo, String motivo) {
        return "Entrada bloqueada no campo '" + campo + "': o texto contém " + motivo + ".";
    }
}
