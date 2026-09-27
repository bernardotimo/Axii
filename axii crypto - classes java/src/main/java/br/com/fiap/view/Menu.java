package br.com.fiap.view;

import br.com.fiap.enums.Idioma;
import br.com.fiap.enums.OrigemAtivo;
import br.com.fiap.enums.TipoAtivo;
import br.com.fiap.enums.TipoChavePix;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.exception.SqlInjectionException;
import br.com.fiap.records.OpcaoMenu;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class Menu {

    private static final OpcaoMenu[] PRINCIPAL = {
            new OpcaoMenu(1, "Usuários"),
            new OpcaoMenu(2, "Contas bancárias"),
            new OpcaoMenu(3, "Chaves Pix"),
            new OpcaoMenu(4, "Ativos de cripto"),
            new OpcaoMenu(5, "Perfil (configurações, notificações, identidade)"),
            new OpcaoMenu(6, "Relatórios"),
            new OpcaoMenu(0, "Sair")
    };

    private static final OpcaoMenu[] CRUD_USUARIO = {
            new OpcaoMenu(1, "Cadastrar usuário"),
            new OpcaoMenu(2, "Listar todos"),
            new OpcaoMenu(3, "Pesquisar por ID"),
            new OpcaoMenu(4, "Pesquisar por e-mail"),
            new OpcaoMenu(5, "Ficha completa (com contas, Pix e ativos)"),
            new OpcaoMenu(6, "Atualizar"),
            new OpcaoMenu(7, "Excluir"),
            new OpcaoMenu(0, "Voltar")
    };

    private static final OpcaoMenu[] CRUD_BANCO = {
            new OpcaoMenu(1, "Cadastrar conta"),
            new OpcaoMenu(2, "Listar todas"),
            new OpcaoMenu(3, "Listar por usuário"),
            new OpcaoMenu(4, "Pesquisar por ID"),
            new OpcaoMenu(5, "Atualizar"),
            new OpcaoMenu(6, "Excluir"),
            new OpcaoMenu(0, "Voltar")
    };

    private static final OpcaoMenu[] CRUD_PIX = {
            new OpcaoMenu(1, "Cadastrar chave"),
            new OpcaoMenu(2, "Listar todas"),
            new OpcaoMenu(3, "Listar por usuário"),
            new OpcaoMenu(4, "Listar por tipo"),
            new OpcaoMenu(5, "Pesquisar por ID"),
            new OpcaoMenu(6, "Atualizar"),
            new OpcaoMenu(7, "Excluir"),
            new OpcaoMenu(0, "Voltar")
    };

    private static final OpcaoMenu[] CRUD_ATIVO = {
            new OpcaoMenu(1, "Cadastrar moeda (Coin)"),
            new OpcaoMenu(2, "Cadastrar moeda estável (Stablecoin)"),
            new OpcaoMenu(3, "Listar todos"),
            new OpcaoMenu(4, "Listar por usuário"),
            new OpcaoMenu(5, "Listar por tipo"),
            new OpcaoMenu(6, "Pesquisar por ID"),
            new OpcaoMenu(7, "Atualizar"),
            new OpcaoMenu(8, "Excluir"),
            new OpcaoMenu(0, "Voltar")
    };

    private static final OpcaoMenu[] CRUD_PERFIL = {
            new OpcaoMenu(1, "Listar configurações"),
            new OpcaoMenu(2, "Atualizar configuração"),
            new OpcaoMenu(3, "Listar preferências de notificação"),
            new OpcaoMenu(4, "Atualizar preferências de notificação"),
            new OpcaoMenu(5, "Listar identidades"),
            new OpcaoMenu(6, "Pesquisar identidade por CPF"),
            new OpcaoMenu(7, "Atualizar identidade"),
            new OpcaoMenu(0, "Voltar")
    };

    private static final OpcaoMenu[] RELATORIOS = {
            new OpcaoMenu(1, "Patrimônio em cripto por usuário"),
            new OpcaoMenu(2, "Tempo de cada conta"),
            new OpcaoMenu(0, "Voltar")
    };

    public static void iniciar() {
        int opcao;
        do {
            exibir("Axii Crypto - menu principal", PRINCIPAL);
            opcao = Console.lerInteiro("Opção");

            switch (opcao) {
                case 1:
                    menuUsuario();
                    break;
                case 2:
                    menuBanco();
                    break;
                case 3:
                    menuPix();
                    break;
                case 4:
                    menuAtivo();
                    break;
                case 5:
                    menuPerfil();
                    break;
                case 6:
                    menuRelatorio();
                    break;
                case 0:
                    Console.info("\nAté logo!");
                    break;
                default:
                    Console.erro("Opção inexistente.");
            }
        } while (opcao != 0);
    }

    private static void menuUsuario() {
        int opcao;
        do {
            exibir("Usuários", CRUD_USUARIO);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarUsuario();
                        break;
                    case 2:
                        UserView.list();
                        break;
                    case 3:
                        UserView.search(Console.lerTexto("ID do usuário"));
                        break;
                    case 4:
                        UserView.searchByEmail(Console.lerTexto("E-mail"));
                        break;
                    case 5:
                        UserView.showComplete(Console.lerTexto("ID do usuário"));
                        break;
                    case 6:
                        atualizarUsuario();
                        break;
                    case 7:
                        UserView.delete(Console.lerTexto("ID do usuário"));
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void cadastrarUsuario() throws SQLException {
        String id = UUID.randomUUID().toString();
        Console.info("ID gerado para o novo usuário: " + id);

        String nome = Console.lerTexto("Nome");
        String email = Console.lerTexto("E-mail");
        String senha = Console.lerTexto("Senha (mínimo 6 caracteres)");
        Idioma idioma = Console.lerEnum("Idioma", Idioma.values());
        boolean modoEscuro = Console.lerSimNao("Modo escuro?");
        boolean notifTransacao = Console.lerSimNao("Notificar transações?");
        boolean notifVariacao = Console.lerSimNao("Notificar variação de preço?");
        boolean notifMarketing = Console.lerSimNao("Receber marketing?");
        String telefone = Console.lerTexto("Telefone (00) 00000-0000");
        String cpf = Console.lerTexto("CPF 000.000.000-00");

        UserView.insert(id, nome, email, senha, idioma.getCodigo(), modoEscuro,
                notifTransacao, notifVariacao, notifMarketing, telefone, cpf);
    }

    private static void atualizarUsuario() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID do usuário");
        Console.info("Informe os novos dados:");

        String nome = Console.lerTexto("Nome");
        String email = Console.lerTexto("E-mail");
        String senha = Console.lerTexto("Senha");
        Idioma idioma = Console.lerEnum("Idioma", Idioma.values());
        boolean modoEscuro = Console.lerSimNao("Modo escuro?");
        boolean notifTransacao = Console.lerSimNao("Notificar transações?");
        boolean notifVariacao = Console.lerSimNao("Notificar variação de preço?");
        boolean notifMarketing = Console.lerSimNao("Receber marketing?");
        String telefone = Console.lerTexto("Telefone");
        String cpf = Console.lerTexto("CPF");

        UserView.update(id, nome, email, senha, idioma.getCodigo(), modoEscuro,
                notifTransacao, notifVariacao, notifMarketing, telefone, cpf);
    }

    private static void menuBanco() {
        int opcao;
        do {
            exibir("Contas bancárias", CRUD_BANCO);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarBanco();
                        break;
                    case 2:
                        BankView.list();
                        break;
                    case 3:
                        BankView.listByUser(Console.lerTexto("ID do usuário"));
                        break;
                    case 4:
                        BankView.search(Console.lerTexto("ID da conta"));
                        break;
                    case 5:
                        atualizarBanco();
                        break;
                    case 6:
                        BankView.delete(Console.lerTexto("ID da conta"));
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void cadastrarBanco() throws SQLException {
        String id = UUID.randomUUID().toString();
        Console.info("ID gerado para a nova conta: " + id);

        String idUsuario = Console.lerTexto("ID do usuário dono da conta");
        String nomeBanco = Console.lerTexto("Nome do banco");
        int agencia = Console.lerInteiro("Agência (só números)");
        String numeroConta = Console.lerTexto("Número da conta");
        boolean ativa = Console.lerSimNao("Conta ativa?");

        BankView.insert(id, nomeBanco, ativa, numeroConta, agencia, idUsuario);
    }

    private static void atualizarBanco() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID da conta");
        String nomeBanco = Console.lerTexto("Nome do banco");
        int agencia = Console.lerInteiro("Agência");
        String numeroConta = Console.lerTexto("Número da conta");
        boolean ativa = Console.lerSimNao("Conta ativa?");

        BankView.update(id, nomeBanco, ativa, numeroConta, agencia);
    }

    private static void menuPix() {
        int opcao;
        do {
            exibir("Chaves Pix", CRUD_PIX);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarPix();
                        break;
                    case 2:
                        PixKeyView.list();
                        break;
                    case 3:
                        PixKeyView.listByUser(Console.lerTexto("ID do usuário"));
                        break;
                    case 4:
                        PixKeyView.listByType(
                                Console.lerEnum("Tipo da chave", TipoChavePix.values()));
                        break;
                    case 5:
                        PixKeyView.search(Console.lerTexto("ID da chave"));
                        break;
                    case 6:
                        atualizarPix();
                        break;
                    case 7:
                        PixKeyView.delete(Console.lerTexto("ID da chave"));
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void cadastrarPix() throws SQLException {
        String id = UUID.randomUUID().toString();
        Console.info("ID gerado para a nova chave: " + id);

        String idUsuario = Console.lerTexto("ID do usuário dono da chave");
        TipoChavePix tipo = Console.lerEnum("Tipo da chave", TipoChavePix.values());
        Console.info("Formato esperado: " + tipo.getExemplo());
        String chave = Console.lerTexto("Chave");

        PixKeyView.insert(id, chave, tipo, idUsuario);
    }

    private static void atualizarPix() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID da chave");
        TipoChavePix tipo = Console.lerEnum("Tipo da chave", TipoChavePix.values());
        Console.info("Formato esperado: " + tipo.getExemplo());
        String chave = Console.lerTexto("Chave");

        PixKeyView.update(id, chave, tipo);
    }

    private static void menuAtivo() {
        int opcao;
        do {
            exibir("Ativos de cripto", CRUD_ATIVO);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        cadastrarCoin();
                        break;
                    case 2:
                        cadastrarStablecoin();
                        break;
                    case 3:
                        CryptoAssetView.list();
                        break;
                    case 4:
                        CryptoAssetView.listByUser(Console.lerTexto("ID do usuário"));
                        break;
                    case 5:
                        CryptoAssetView.listByType(
                                Console.lerEnum("Tipo do ativo", TipoAtivo.values()));
                        break;
                    case 6:
                        CryptoAssetView.search(Console.lerTexto("ID do ativo"));
                        break;
                    case 7:
                        atualizarAtivo();
                        break;
                    case 8:
                        CryptoAssetView.delete(Console.lerTexto("ID do ativo"));
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void cadastrarCoin() throws SQLException {
        String id = UUID.randomUUID().toString();
        Console.info("ID gerado para o novo ativo: " + id);

        String idUsuario = Console.lerTexto("ID do usuário dono do ativo");
        String ativo = Console.lerTexto("Nome do ativo (ex.: Bitcoin)");
        String simbolo = Console.lerTexto("Símbolo (ex.: BTC)");
        String blockchain = Console.lerTexto("Blockchain (ex.: Bitcoin)");
        double quantidade = Console.lerDecimal("Quantidade");
        double preco = Console.lerDecimal("Preço de mercado em R$");
        OrigemAtivo origem = Console.lerEnum("Origem", OrigemAtivo.values());
        LocalDateTime aquisicao = Console.lerDataHora("Data da aquisição");

        CryptoAssetView.insertCoin(id, ativo, quantidade, aquisicao, origem.getCodigo(),
                idUsuario, simbolo, blockchain, preco);
    }

    private static void cadastrarStablecoin() throws SQLException {
        String id = UUID.randomUUID().toString();
        Console.info("ID gerado para o novo ativo: " + id);

        String idUsuario = Console.lerTexto("ID do usuário dono do ativo");
        String ativo = Console.lerTexto("Nome do ativo (ex.: Tether)");
        String moeda = Console.lerTexto("Moeda de referência (ex.: USD)");
        double quantidade = Console.lerDecimal("Quantidade");
        double taxa = Console.lerDecimal("Taxa de conversão em R$");
        OrigemAtivo origem = Console.lerEnum("Origem", OrigemAtivo.values());
        LocalDateTime aquisicao = Console.lerDataHora("Data da aquisição");

        CryptoAssetView.insertStablecoin(id, ativo, quantidade, aquisicao, origem.getCodigo(),
                idUsuario, moeda, taxa);
    }

    private static void atualizarAtivo() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID do ativo");
        String ativo = Console.lerTexto("Nome do ativo");
        double quantidade = Console.lerDecimal("Quantidade");
        OrigemAtivo origem = Console.lerEnum("Origem", OrigemAtivo.values());

        Console.info("Se for Coin informe o símbolo; se for Stablecoin, a moeda de referência.");
        String simboloOuMoeda = Console.lerTexto("Símbolo / moeda de referência");
        String blockchain = Console.lerTexto("Blockchain (deixe qualquer valor se for Stablecoin)");
        double precoOuTaxa = Console.lerDecimal("Preço de mercado / taxa de conversão");

        CryptoAssetView.update(id, ativo, quantidade, origem.getCodigo(),
                simboloOuMoeda, blockchain, precoOuTaxa);
    }

    private static void menuPerfil() {
        int opcao;
        do {
            exibir("Perfil do usuário", CRUD_PERFIL);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        ProfileView.listSettings();
                        break;
                    case 2:
                        atualizarSettings();
                        break;
                    case 3:
                        ProfileView.listNotifications();
                        break;
                    case 4:
                        atualizarNotifications();
                        break;
                    case 5:
                        ProfileView.listIdentities();
                        break;
                    case 6:
                        ProfileView.searchIdentityByCpf(Console.lerTexto("CPF"));
                        break;
                    case 7:
                        atualizarIdentity();
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void atualizarSettings() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID da configuração");
        Idioma idioma = Console.lerEnum("Idioma", Idioma.values());
        boolean modoEscuro = Console.lerSimNao("Modo escuro?");

        ProfileView.updateSettings(id, idioma.getCodigo(), modoEscuro);
    }

    private static void atualizarNotifications() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID das notificações");
        boolean transacao = Console.lerSimNao("Notificar transações?");
        boolean variacao = Console.lerSimNao("Notificar variação de preço?");
        boolean marketing = Console.lerSimNao("Receber marketing?");

        ProfileView.updateNotifications(id, transacao, variacao, marketing);
    }

    private static void atualizarIdentity() throws SQLException, EntityNotFoundException {
        String id = Console.lerTexto("ID da identidade");
        String telefone = Console.lerTexto("Telefone (00) 00000-0000");
        String cpf = Console.lerTexto("CPF 000.000.000-00");

        ProfileView.updateIdentity(id, telefone, cpf);
    }

    private static void menuRelatorio() {
        int opcao;
        do {
            exibir("Relatórios", RELATORIOS);
            opcao = Console.lerInteiro("Opção");
            try {
                switch (opcao) {
                    case 1:
                        RelatorioView.patrimonioPorUsuario();
                        break;
                    case 2:
                        RelatorioView.tempoDeCadaConta();
                        break;
                    case 0:
                        break;
                    default:
                        Console.erro("Opção inexistente.");
                }
            } catch (Exception e) {
                tratar(e);
            }
            if (opcao != 0) {
                Console.pausar();
            }
        } while (opcao != 0);
    }

    private static void exibir(String titulo, OpcaoMenu[] opcoes) {
        Console.titulo(titulo);
        for (OpcaoMenu opcao : opcoes) {
            System.out.println(opcao);
        }
        System.out.println();
    }

    private static void tratar(Exception e) {
        if (e instanceof SqlInjectionException) {
            Console.erro(e.getMessage());
        } else if (e instanceof EntityNotFoundException) {
            Console.erro(e.getMessage());
        } else if (e instanceof SQLException) {
            Console.erro("Erro no banco de dados: " + e.getMessage());
        } else if (e instanceof IllegalArgumentException) {
            Console.erro(e.getMessage());
        } else {
            Console.erro("Erro inesperado: " + e);
        }
    }
}
