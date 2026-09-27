package br.com.fiap.view;

import br.com.fiap.dao.PixKeyDao;
import br.com.fiap.enums.TipoChavePix;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.exception.SqlInjectionException;
import br.com.fiap.model.PixKey;
import br.com.fiap.security.AntiInjecaoSql;

import java.sql.SQLException;
import java.util.List;

public class PixKeyView {

    public static void insert(String id, String key, TipoChavePix type, String userId)
            throws SQLException {
        Console.titulo("Cadastrar chave Pix");

        id = AntiInjecaoSql.uuid("id", id);
        key = AntiInjecaoSql.texto("chave", key, 150);
        userId = AntiInjecaoSql.uuid("id do usuário", userId);

        PixKey pixKey = new PixKey(id, key, type, userId);
        validarFormato(pixKey);

        PixKeyDao dao = new PixKeyDao();
        try {
            dao.insert(pixKey);
            Console.sucesso("Chave Pix cadastrada com sucesso!");
            print(pixKey);
        } finally {
            dao.closeConnection();
        }
    }

    public static void list() throws SQLException {
        Console.titulo("Listar todas as chaves Pix");

        PixKeyDao dao = new PixKeyDao();
        try {
            List<PixKey> chaves = dao.findAll();
            Console.info("Total de chaves: " + chaves.size());
            for (PixKey pixKey : chaves) {
                print(pixKey);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void listByUser(String userId) throws SQLException {
        Console.titulo("Chaves Pix do usuário");
        userId = AntiInjecaoSql.uuid("id do usuário", userId);

        PixKeyDao dao = new PixKeyDao();
        try {
            List<PixKey> chaves = dao.findByUser(userId);
            Console.info("Total de chaves: " + chaves.size());
            for (PixKey pixKey : chaves) {
                print(pixKey);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void listByType(TipoChavePix type) throws SQLException {
        Console.titulo("Chaves Pix do tipo " + type.getLabel());

        PixKeyDao dao = new PixKeyDao();
        try {
            List<PixKey> chaves = dao.findByType(type);
            Console.info("Total de chaves: " + chaves.size());
            for (PixKey pixKey : chaves) {
                print(pixKey);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void search(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar chave Pix por ID");
        id = AntiInjecaoSql.uuid("id", id);

        PixKeyDao dao = new PixKeyDao();
        try {
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void update(String id, String key, TipoChavePix type)
            throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar chave Pix");

        id = AntiInjecaoSql.uuid("id", id);
        key = AntiInjecaoSql.texto("chave", key, 150);

        PixKeyDao dao = new PixKeyDao();
        try {
            PixKey pixKey = dao.findById(id);

            pixKey.setKey(key);
            pixKey.setType(type);
            validarFormato(pixKey);

            dao.update(pixKey);
            Console.sucesso("Chave Pix atualizada! Dados relidos do banco:");
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void delete(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Remover chave Pix");
        id = AntiInjecaoSql.uuid("id", id);

        PixKeyDao dao = new PixKeyDao();
        try {
            dao.delete(id);
            Console.sucesso("Chave Pix removida com sucesso!");
            try {
                dao.findById(id);
                Console.erro("Falha: a chave ainda existe no banco.");
            } catch (EntityNotFoundException e) {
                Console.sucesso("Remoção confirmada. " + e.getMessage());
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void print(PixKey pixKey) {
        Console.separador();
        Console.campo("ID", pixKey.getId());
        Console.campo("Tipo", pixKey.getType().getLabel()
                + " (" + pixKey.getType().getCodigo() + ")");
        Console.campo("Chave", pixKey.getKey());
        Console.campo("ID do usuário", pixKey.getUserId());
    }

    public static void printResumo(PixKey pixKey) {
        System.out.printf("    - %-10s %s%n", pixKey.getType().getLabel(), pixKey.getKey());
    }

    private static void validarFormato(PixKey pixKey) {
        if (!pixKey.isKeyValid()) {
            throw new SqlInjectionException("A chave não está no formato de "
                    + pixKey.getType().getLabel() + ". Exemplo: " + pixKey.getType().getExemplo());
        }
    }
}
