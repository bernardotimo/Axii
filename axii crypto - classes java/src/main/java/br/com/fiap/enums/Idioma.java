package br.com.fiap.enums;

public enum Idioma {

    PT_BR("pt-BR", "Português (Brasil)"),
    EN_US("en-US", "Inglês (EUA)"),
    ES_ES("es-ES", "Espanhol (Espanha)");

    private final String codigo;
    private final String label;

    Idioma(String codigo, String label) {
        this.codigo = codigo;
        this.label = label;
    }

    public static Idioma fromCodigo(String codigo) {
        for (Idioma idioma : values()) {
            if (idioma.codigo.equalsIgnoreCase(codigo)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Idioma inválido: " + codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label + " (" + codigo + ")";
    }
}
