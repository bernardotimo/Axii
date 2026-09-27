package br.com.fiap.enums;

public enum OrigemAtivo {

    MANUAL("Manual", "Lançamento manual do usuário"),
    BINANCE("Binance", "Importado da corretora Binance"),
    COINBASE("Coinbase", "Importado da corretora Coinbase"),
    MERCADO_BITCOIN("Mercado Bitcoin", "Importado do Mercado Bitcoin");

    private final String codigo;
    private final String descricao;

    OrigemAtivo(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return codigo;
    }
}
