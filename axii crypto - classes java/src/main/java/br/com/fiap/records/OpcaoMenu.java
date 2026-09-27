package br.com.fiap.records;

public record OpcaoMenu(int codigo, String descricao) {

    public OpcaoMenu {
        if (codigo < 0) {
            throw new IllegalArgumentException("O código da opção não pode ser negativo");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição da opção é obrigatória");
        }
    }

    public boolean isSair() {
        return codigo == 0;
    }

    @Override
    public String toString() {
        return String.format("  %d - %s", codigo, descricao);
    }
}
