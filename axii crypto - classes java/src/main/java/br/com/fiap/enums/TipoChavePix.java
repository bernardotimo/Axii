package br.com.fiap.enums;

import java.util.regex.Pattern;

public enum TipoChavePix {

    CELULAR("celular", "Celular", "\\(\\d{2}\\) \\d{4,5}-\\d{4}|\\+55\\d{10,11}"),
    EMAIL("e-mail", "E-mail", "[^@\\s]+@[^@\\s]+\\.[A-Za-z]{2,}"),
    CPF("cpf", "CPF", "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"),
    CNPJ("cnpj", "CNPJ", "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}"),
    ALEATORIA("aleatoria", "Aleatória", "[0-9a-fA-F-]{36}");

    private final String codigo;
    private final String label;
    private final Pattern formato;

    TipoChavePix(String codigo, String label, String formato) {
        this.codigo = codigo;
        this.label = label;
        this.formato = Pattern.compile(formato);
    }

    public static TipoChavePix fromCodigo(String codigo) {
        for (TipoChavePix tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de chave Pix inválido: " + codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLabel() {
        return label;
    }

    public boolean aceita(String chave) {
        return chave != null && formato.matcher(chave).matches();
    }

    public String getExemplo() {
        return switch (this) {
            case CELULAR -> "(11) 91234-5678";
            case EMAIL -> "usuario@email.com";
            case CPF -> "123.456.789-01";
            case CNPJ -> "12.345.678/0001-90";
            case ALEATORIA -> "7d9f1c2e-3b4a-4f6d-9e8c-1a2b3c4d5e6f";
        };
    }

    @Override
    public String toString() {
        return label;
    }
}
