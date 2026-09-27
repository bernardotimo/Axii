package br.com.fiap;

import br.com.fiap.dao.UserDao;
import br.com.fiap.enums.Idioma;
import br.com.fiap.enums.OrigemAtivo;
import br.com.fiap.enums.TipoAtivo;
import br.com.fiap.enums.TipoChavePix;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.exception.SqlInjectionException;
import br.com.fiap.model.User;
import br.com.fiap.view.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class App {

    public static void main(String[] args) {
        Console.titulo("Axii Crypto");
        Console.info("  1 - Menu interativo");
        Console.info("  2 - Roteiro automático de testes (todas as classes)");
        Console.info("  0 - Sair");
        System.out.println();

        int escolha = Console.lerInteiro("Opção");

        switch (escolha) {
            case 1:
                Menu.iniciar();
                break;
            case 2:
                testarTodasAsClasses();
                break;
            case 0:
                Console.info("Até logo!");
                break;
            default:
                Console.erro("Opção inexistente.");
        }
    }

    public static void testarTodasAsClasses() {
        String idUsuario = UUID.randomUUID().toString();
        String idConta = UUID.randomUUID().toString();
        String idChavePix = UUID.randomUUID().toString();
        String idCoin = UUID.randomUUID().toString();
        String idStablecoin = UUID.randomUUID().toString();

        long marca = System.currentTimeMillis();
        String email = "teste." + marca + "@axii.com";
        String cpf = gerarCpf();
        String chavePix = "teste.pix." + marca + "@axii.com";

        try {
            testarUser(idUsuario, email, cpf);
            testarBank(idConta, idUsuario);
            testarPixKey(idChavePix, chavePix, idUsuario);
            testarCryptoAsset(idCoin, idStablecoin, idUsuario);
            testarProfile(idUsuario);
            testarRelatorios();
            limpar(idUsuario);

            Console.titulo("Roteiro concluído");
            Console.sucesso("Todas as classes foram testadas e os dados de teste foram removidos.");

        } catch (SqlInjectionException e) {
            Console.erro(e.getMessage());
        } catch (EntityNotFoundException e) {
            Console.erro(e.getMessage());
        } catch (SQLException e) {
            Console.erro("Erro no banco de dados: " + e.getMessage());
        }
    }

    private static void testarUser(String id, String email, String cpf)
            throws SQLException, EntityNotFoundException {
        UserView.insert(id,
                "Usuário Teste",
                email,
                "Teste@123",
                Idioma.PT_BR.getCodigo(),
                false,
                true, false, false,
                "(11) 91234-5678", cpf);

        UserView.list();
        UserView.search(id);
        UserView.searchByEmail(email);

        UserView.update(id,
                "Usuário Teste Atualizado",
                "teste.atualizado." + System.currentTimeMillis() + "@axii.com",
                "NovaSenha@456",
                Idioma.EN_US.getCodigo(),
                true,
                true, true, true,
                "(11) 99999-0000", gerarCpf());
    }

    private static void testarBank(String idConta, String idUsuario)
            throws SQLException, EntityNotFoundException {
        BankView.insert(idConta, "Banco Teste", true, "12345-6", 1234, idUsuario);
        BankView.listByUser(idUsuario);
        BankView.search(idConta);
        BankView.update(idConta, "Banco Teste Atualizado", false, "65432-1", 4321);
        BankView.delete(idConta);
    }

    private static void testarPixKey(String idChave, String chave, String idUsuario)
            throws SQLException, EntityNotFoundException {
        PixKeyView.insert(idChave, chave, TipoChavePix.EMAIL, idUsuario);
        PixKeyView.listByUser(idUsuario);
        PixKeyView.listByType(TipoChavePix.EMAIL);
        PixKeyView.search(idChave);

        PixKeyView.update(idChave, "(11) 98888-7777", TipoChavePix.CELULAR);
        PixKeyView.delete(idChave);
    }

    private static void testarCryptoAsset(String idCoin, String idStablecoin, String idUsuario)
            throws SQLException, EntityNotFoundException {
        LocalDateTime aquisicao = LocalDateTime.now().minusMonths(3);

        CryptoAssetView.insertCoin(idCoin, "Bitcoin", 0.05234100, aquisicao,
                OrigemAtivo.BINANCE.getCodigo(), idUsuario, "BTC", "Bitcoin", 350000.00);

        CryptoAssetView.insertStablecoin(idStablecoin, "Tether", 1500.00000000,
                aquisicao.plusDays(10), OrigemAtivo.COINBASE.getCodigo(), idUsuario,
                "USD", 5.4321);

        CryptoAssetView.listByUser(idUsuario);
        CryptoAssetView.listByType(TipoAtivo.COIN);
        CryptoAssetView.listByType(TipoAtivo.STABLECOIN);
        CryptoAssetView.search(idCoin);

        CryptoAssetView.update(idCoin, "Bitcoin", 0.10000000,
                OrigemAtivo.MANUAL.getCodigo(), "BTC", "Bitcoin", 380000.00);
        CryptoAssetView.update(idStablecoin, "Tether", 2000.00000000,
                OrigemAtivo.MANUAL.getCodigo(), "USD", "-", 5.5000);

        UserView.showComplete(idUsuario);

        CryptoAssetView.delete(idCoin);
    }

    private static void testarProfile(String idUsuario)
            throws SQLException, EntityNotFoundException {
        ProfileView.listSettings();
        ProfileView.listNotifications();
        ProfileView.listIdentities();

        UserDao dao = new UserDao();
        try {
            User usuario = dao.findById(idUsuario);

            ProfileView.searchSettings(usuario.getSettings().getId());
            ProfileView.updateSettings(usuario.getSettings().getId(),
                    Idioma.ES_ES.getCodigo(), false);

            ProfileView.searchNotifications(usuario.getNotifications().getId());
            ProfileView.updateNotifications(usuario.getNotifications().getId(),
                    false, true, false);

            ProfileView.searchIdentity(usuario.getIdentity().getId());
            ProfileView.searchIdentityByCpf(usuario.getIdentity().getCpf());
            ProfileView.updateIdentity(usuario.getIdentity().getId(),
                    "(21) 97777-1234", gerarCpf());
        } finally {
            dao.closeConnection();
        }
    }

    private static void testarRelatorios() throws SQLException {
        RelatorioView.patrimonioPorUsuario();
        RelatorioView.tempoDeCadaConta();
    }

    private static void limpar(String idUsuario) throws SQLException, EntityNotFoundException {
        UserView.delete(idUsuario);
    }

    private static String gerarCpf() {
        String digitos = String.format("%011d",
                ThreadLocalRandom.current().nextLong(100_000_000_000L));
        return digitos.substring(0, 3) + "." + digitos.substring(3, 6) + "."
                + digitos.substring(6, 9) + "-" + digitos.substring(9);
    }
}
