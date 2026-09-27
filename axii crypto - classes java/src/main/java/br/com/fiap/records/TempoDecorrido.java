package br.com.fiap.records;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

public record TempoDecorrido(int anos, int meses, int dias, long totalDeDias) {

    public TempoDecorrido {
        if (totalDeDias < 0) {
            throw new IllegalArgumentException("A data informada está no futuro");
        }
    }

    public static TempoDecorrido desde(LocalDateTime inicio) {
        LocalDateTime agora = LocalDateTime.now();

        Period periodo = Period.between(inicio.toLocalDate(), agora.toLocalDate());
        Duration duracao = Duration.between(inicio, agora);

        return new TempoDecorrido(periodo.getYears(), periodo.getMonths(),
                periodo.getDays(), duracao.toDays());
    }

    public boolean isRecente() {
        return totalDeDias <= 30;
    }

    public String descricao() {
        if (totalDeDias == 0) {
            return "hoje";
        }
        StringBuilder texto = new StringBuilder();
        if (anos > 0) {
            texto.append(anos).append(anos == 1 ? " ano" : " anos");
        }
        if (meses > 0) {
            if (texto.length() > 0) {
                texto.append(", ");
            }
            texto.append(meses).append(meses == 1 ? " mês" : " meses");
        }
        if (dias > 0) {
            if (texto.length() > 0) {
                texto.append(" e ");
            }
            texto.append(dias).append(dias == 1 ? " dia" : " dias");
        }
        return texto.toString();
    }

    @Override
    public String toString() {
        return descricao() + " (" + totalDeDias + " dias no total)";
    }
}
