package br.com.fiap.view;

import br.com.fiap.dao.BankDao;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Bank;
import br.com.fiap.security.AntiInjecaoSql;

import java.sql.SQLException;
import java.util.List;

public class BankView {

    public static void insert(String id, String bankName, boolean active,
                              String accountNumber, int agency, String userId) throws SQLException {
        Console.titulo("Cadastrar conta bancária");

        id = AntiInjecaoSql.uuid("id", id);
        bankName = AntiInjecaoSql.texto("nome do banco", bankName, 100);
        accountNumber = AntiInjecaoSql.texto("número da conta", accountNumber, 20);
        agency = AntiInjecaoSql.inteiroPositivo("agência", agency);
        userId = AntiInjecaoSql.uuid("id do usuário", userId);

        Bank bank = new Bank(id, bankName, active, accountNumber, agency, userId);

        BankDao dao = new BankDao();
        try {
            dao.insert(bank);
            Console.sucesso("Conta bancária cadastrada com sucesso!");
            print(bank);
        } finally {
            dao.closeConnection();
        }
    }

    public static void list() throws SQLException {
        Console.titulo("Listar todas as contas bancárias");

        BankDao dao = new BankDao();
        try {
            List<Bank> contas = dao.findAll();
            Console.info("Total de contas: " + contas.size());
            for (Bank bank : contas) {
                print(bank);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void listByUser(String userId) throws SQLException {
        Console.titulo("Contas bancárias do usuário");
        userId = AntiInjecaoSql.uuid("id do usuário", userId);

        BankDao dao = new BankDao();
        try {
            List<Bank> contas = dao.findByUser(userId);
            Console.info("Total de contas: " + contas.size());
            for (Bank bank : contas) {
                print(bank);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void search(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar conta bancária por ID");
        id = AntiInjecaoSql.uuid("id", id);

        BankDao dao = new BankDao();
        try {
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void update(String id, String bankName, boolean active,
                              String accountNumber, int agency) throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar conta bancária");

        id = AntiInjecaoSql.uuid("id", id);
        bankName = AntiInjecaoSql.texto("nome do banco", bankName, 100);
        accountNumber = AntiInjecaoSql.texto("número da conta", accountNumber, 20);
        agency = AntiInjecaoSql.inteiroPositivo("agência", agency);

        BankDao dao = new BankDao();
        try {
            Bank bank = dao.findById(id);

            bank.setBankName(bankName);
            bank.setActive(active);
            bank.setAccountNumber(accountNumber);
            bank.setAgency(agency);

            dao.update(bank);
            Console.sucesso("Conta bancária atualizada! Dados relidos do banco:");
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void delete(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Remover conta bancária");
        id = AntiInjecaoSql.uuid("id", id);

        BankDao dao = new BankDao();
        try {
            dao.delete(id);
            Console.sucesso("Conta bancária removida com sucesso!");
            try {
                dao.findById(id);
                Console.erro("Falha: a conta ainda existe no banco.");
            } catch (EntityNotFoundException e) {
                Console.sucesso("Remoção confirmada. " + e.getMessage());
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void print(Bank bank) {
        Console.separador();
        Console.campo("ID", bank.getId());
        Console.campo("Banco", bank.getBankName());
        Console.campo("Agência", bank.getAgency());
        Console.campo("Conta", bank.getAccountNumber());
        Console.campo("Ativa", Console.simNao(bank.isActive()));
        Console.campo("ID do usuário", bank.getUserId());
    }

    public static void printResumo(Bank bank) {
        System.out.printf("    - %-20s ag. %-8d conta %-12s %s%n",
                bank.getBankName(), bank.getAgency(), bank.getAccountNumber(),
                bank.isActive() ? "(ativa)" : "(inativa)");
    }
}
