package br.com.fiap.enums;

public enum TipoAtivo {

    COIN("COIN", "Moeda volátil"),
    STABLECOIN("STABLECOIN", "Moeda estável");

    private final String codigo;
    private final String label;

    TipoAtivo(String codigo, String label) {
        this.codigo = codigo;
        this.label = label;
    }

    public static TipoAtivo fromCodigo(String codigo) {
        for (TipoAtivo tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de ativo inválido: " + codigo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLabel() {
        return label;
    }

    public boolean isLastreada() {
        return this == STABLECOIN;
    }

    @Override
    public String toString() {
        return label;
    }
}
