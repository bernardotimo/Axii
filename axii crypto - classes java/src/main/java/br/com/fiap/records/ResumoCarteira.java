package br.com.fiap.records;

import java.util.Locale;

public record ResumoCarteira(String idUsuario, String nomeUsuario,
                             int quantidadeAtivos, double valorTotal) {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    public ResumoCarteira {
        if (idUsuario == null || idUsuario.isBlank()) {
            throw new IllegalArgumentException("O id do usuário é obrigatório");
        }
        if (quantidadeAtivos < 0) {
            throw new IllegalArgumentException("A quantidade de ativos não pode ser negativa");
        }
        if (valorTotal < 0) {
            throw new IllegalArgumentException("O valor total não pode ser negativo");
        }
    }

    public boolean temAtivos() {
        return quantidadeAtivos > 0;
    }

    public double valorMedioPorAtivo() {
        return temAtivos() ? valorTotal / quantidadeAtivos : 0;
    }

    public String valorFormatado() {
        return String.format(BRASIL, "R$ %,.2f", valorTotal);
    }

    @Override
    public String toString() {
        return String.format("%-28s | %2d ativo(s) | %15s",
                nomeUsuario, quantidadeAtivos, valorFormatado());
    }
}
