package br.com.fiap.view;

import br.com.fiap.dao.CryptoAssetDao;
import br.com.fiap.dao.UserDao;
import br.com.fiap.model.User;
import br.com.fiap.records.ResumoCarteira;
import br.com.fiap.records.TempoDecorrido;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatorioView {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void patrimonioPorUsuario() throws SQLException {
        Console.titulo("Relatório - patrimônio em cripto por usuário");

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            Instant inicio = Instant.now();
            List<ResumoCarteira> resumos = dao.resumoPorUsuario();
            Duration duracao = Duration.between(inicio, Instant.now());

            Console.info("Gerado em " + LocalDate.now().format(DATA)
                    + " | consulta executada em " + duracao.toMillis() + " ms");
            Console.separador();

            double totalGeral = 0;
            for (ResumoCarteira resumo : resumos) {
                System.out.println("  " + resumo);
                totalGeral += resumo.valorTotal();
            }

            Console.separador();
            Console.campo("Usuários no relatório", resumos.size());
            Console.campo("Patrimônio somado", Console.moeda(totalGeral));

            for (ResumoCarteira resumo : resumos) {
                if (resumo.temAtivos()) {
                    Console.campo("Maior carteira",
                            resumo.nomeUsuario() + " com " + resumo.valorFormatado());
                    break;
                }
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void tempoDeCadaConta() throws SQLException {
        Console.titulo("Relatório - tempo de cada conta");

        UserDao dao = new UserDao();
        try {
            List<User> usuarios = dao.findAll();
            Console.info("Data de referência: "
                    + LocalDateTime.now().format(Console.DATA_HORA));
            Console.separador();

            int contasNovas = 0;
            for (User user : usuarios) {
                TempoDecorrido tempo = user.getAccountAge();
                if (tempo == null) {
                    continue;
                }
                if (tempo.isRecente()) {
                    contasNovas++;
                }
                String quando = tempo.totalDeDias() == 0
                        ? "hoje"
                        : "há " + tempo.descricao();

                System.out.printf("  %-28s criada em %s | %s%n",
                        user.getName(),
                        user.getCreatedAt().format(DATA),
                        quando);
            }

            Console.separador();
            Console.campo("Total de contas", usuarios.size());
            Console.campo("Contas novas (30 dias)", contasNovas);
        } finally {
            dao.closeConnection();
        }
    }
}
