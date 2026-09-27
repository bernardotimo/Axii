package br.com.fiap.view;

import br.com.fiap.dao.CryptoAssetDao;
import br.com.fiap.enums.TipoAtivo;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Coin;
import br.com.fiap.model.CryptoAsset;
import br.com.fiap.model.Stablecoin;
import br.com.fiap.records.TempoDecorrido;
import br.com.fiap.security.AntiInjecaoSql;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CryptoAssetView {

    public static void insertCoin(String id, String asset, double quantity,
                                  LocalDateTime acquired, String source, String userId,
                                  String symbol, String blockchain, double marketPrice)
            throws SQLException {
        Console.titulo("Cadastrar moeda (Coin)");

        id = AntiInjecaoSql.uuid("id", id);
        asset = AntiInjecaoSql.texto("ativo", asset, 100);
        quantity = AntiInjecaoSql.numeroPositivo("quantidade", quantity);
        source = AntiInjecaoSql.texto("origem", source, 50);
        userId = AntiInjecaoSql.uuid("id do usuário", userId);
        symbol = AntiInjecaoSql.texto("símbolo", symbol, 10);
        blockchain = AntiInjecaoSql.texto("blockchain", blockchain, 50);
        marketPrice = AntiInjecaoSql.numeroPositivo("preço de mercado", marketPrice);

        Coin coin = new Coin(id, asset, quantity, acquired, source, userId,
                symbol, blockchain, marketPrice);

        gravar(coin);
    }

    public static void insertStablecoin(String id, String asset, double quantity,
                                        LocalDateTime acquired, String source, String userId,
                                        String currency, double conversionRate)
            throws SQLException {
        Console.titulo("Cadastrar moeda estável (Stablecoin)");

        id = AntiInjecaoSql.uuid("id", id);
        asset = AntiInjecaoSql.texto("ativo", asset, 100);
        quantity = AntiInjecaoSql.numeroPositivo("quantidade", quantity);
        source = AntiInjecaoSql.texto("origem", source, 50);
        userId = AntiInjecaoSql.uuid("id do usuário", userId);
        currency = AntiInjecaoSql.texto("moeda de referência", currency, 10);
        conversionRate = AntiInjecaoSql.numeroPositivo("taxa de conversão", conversionRate);

        Stablecoin stablecoin = new Stablecoin(id, asset, quantity, acquired, source, userId,
                currency, conversionRate);

        gravar(stablecoin);
    }

    public static void list() throws SQLException {
        Console.titulo("Listar todos os ativos");

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            List<CryptoAsset> ativos = dao.findAll();
            Console.info("Total de ativos: " + ativos.size());
            for (CryptoAsset ativo : ativos) {
                print(ativo);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void listByUser(String userId) throws SQLException {
        Console.titulo("Ativos do usuário");
        userId = AntiInjecaoSql.uuid("id do usuário", userId);

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            List<CryptoAsset> ativos = dao.findByUser(userId);
            Console.info("Total de ativos: " + ativos.size());

            double total = 0;
            for (CryptoAsset ativo : ativos) {
                print(ativo);
                total += ativo.getTotalValue();
            }
            Console.separador();
            Console.campo("Patrimônio total", Console.moeda(total));
        } finally {
            dao.closeConnection();
        }
    }

    public static void listByType(TipoAtivo tipo) throws SQLException {
        Console.titulo("Ativos do tipo " + tipo.getLabel());

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            List<CryptoAsset> ativos = dao.findByType(tipo);
            Console.info("Total de ativos: " + ativos.size());
            for (CryptoAsset ativo : ativos) {
                print(ativo);
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void search(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Pesquisar ativo por ID");
        id = AntiInjecaoSql.uuid("id", id);

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void update(String id, String asset, double quantity, String source,
                              String symbolOrCurrency, String blockchain, double priceOrRate)
            throws SQLException, EntityNotFoundException {
        Console.titulo("Atualizar ativo");

        id = AntiInjecaoSql.uuid("id", id);
        asset = AntiInjecaoSql.texto("ativo", asset, 100);
        quantity = AntiInjecaoSql.numeroPositivo("quantidade", quantity);
        source = AntiInjecaoSql.texto("origem", source, 50);
        priceOrRate = AntiInjecaoSql.numeroPositivo("preço/taxa", priceOrRate);

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            CryptoAsset ativo = dao.findById(id);

            ativo.setAsset(asset);
            ativo.setQuantity(quantity);
            ativo.setSource(source);

            if (ativo instanceof Coin coin) {
                coin.setSymbol(AntiInjecaoSql.texto("símbolo", symbolOrCurrency, 10));
                coin.setBlockchain(AntiInjecaoSql.texto("blockchain", blockchain, 50));
                coin.setMarketPrice(priceOrRate);
            } else if (ativo instanceof Stablecoin stablecoin) {
                stablecoin.setCurrency(AntiInjecaoSql.texto("moeda de referência", symbolOrCurrency, 10));
                stablecoin.setConversionRate(priceOrRate);
            }

            dao.update(ativo);
            Console.sucesso("Ativo atualizado! Dados relidos do banco:");
            print(dao.findById(id));
        } finally {
            dao.closeConnection();
        }
    }

    public static void delete(String id) throws SQLException, EntityNotFoundException {
        Console.titulo("Remover ativo");
        id = AntiInjecaoSql.uuid("id", id);

        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            dao.delete(id);
            Console.sucesso("Ativo removido com sucesso! "
                    + "A linha do subtipo saiu junto pelo ON DELETE CASCADE.");
            try {
                dao.findById(id);
                Console.erro("Falha: o ativo ainda existe no banco.");
            } catch (EntityNotFoundException e) {
                Console.sucesso("Remoção confirmada. " + e.getMessage());
            }
        } finally {
            dao.closeConnection();
        }
    }

    public static void print(CryptoAsset ativo) {
        Console.separador();
        Console.campo("ID", ativo.getId());
        Console.campo("Ativo", ativo.getAsset());
        Console.campo("Tipo", ativo.getAssetType().getLabel()
                + " (" + ativo.getAssetType().getCodigo() + ")");
        Console.campo("Quantidade", Console.quantidade(ativo.getQuantity()));
        Console.campo("Adquirido em", Console.data(ativo.getAcquired()));

        TempoDecorrido posse = ativo.getHoldingTime();
        if (posse != null) {
            Console.campo("Na carteira há", posse.descricao());
        }

        Console.campo("Origem", ativo.getSource());
        Console.campo("Aceita staking", Console.simNao(ativo.canStaking()));

        if (ativo instanceof Coin coin) {
            Console.campo("Símbolo", coin.getSymbol());
            Console.campo("Blockchain", coin.getBlockchain());
            Console.campo("Preço de mercado", Console.moeda(coin.getMarketPrice()));
        } else if (ativo instanceof Stablecoin stablecoin) {
            Console.campo("Moeda de referência", stablecoin.getCurrency());
            Console.campo("Taxa de conversão", Console.moeda(stablecoin.getConversionRate()));
        }

        Console.campo("Valor total", Console.moeda(ativo.getTotalValue()));
        Console.campo("ID do usuário", ativo.getUserId());
    }

    public static void printResumo(CryptoAsset ativo) {
        System.out.printf("    - %-14s %-12s %18s  (%s)%n",
                ativo.getAsset(),
                ativo.getAssetType().getCodigo(),
                Console.moeda(ativo.getTotalValue()),
                ativo.getHoldingDays() + " dias na carteira");
    }

    private static void gravar(CryptoAsset ativo) throws SQLException {
        CryptoAssetDao dao = new CryptoAssetDao();
        try {
            dao.insert(ativo);
            Console.sucesso("Ativo cadastrado com sucesso!");
            print(ativo);
        } finally {
            dao.closeConnection();
        }
    }
}
