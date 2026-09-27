package br.com.fiap.dao;

import br.com.fiap.enums.TipoAtivo;
import br.com.fiap.exception.EntityNotFoundException;
import br.com.fiap.model.Coin;
import br.com.fiap.model.CryptoAsset;
import br.com.fiap.model.Stablecoin;
import br.com.fiap.records.ResumoCarteira;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CryptoAssetDao extends Dao {

    private static final String SELECT =
            "SELECT a.id_crypto_asset, a.ativo, a.quantidade, a.data_aquisicao, a.origem, "
                    + "       a.tipo_ativo, a.id_user, "
                    + "       c.simbolo, c.blockchain, c.preco_mercado, "
                    + "       s.moeda_referencia, s.taxa_conversao "
                    + "  FROM t_axii_crypto_asset a "
                    + "  LEFT JOIN t_axii_coin c       ON c.id_coin       = a.id_crypto_asset "
                    + "  LEFT JOIN t_axii_stablecoin s ON s.id_stablecoin = a.id_crypto_asset";

    public CryptoAssetDao() throws SQLException {
        super();
    }

    CryptoAssetDao(Connection conexao) {
        super(conexao);
    }

    public void insert(CryptoAsset ativo) throws SQLException {
        if (ativo.getId() == null) {
            ativo.setId(UUID.randomUUID().toString());
        }
        if (ativo.getAcquired() == null) {
            ativo.setAcquired(LocalDateTime.now());
        }

        conexao.setAutoCommit(false);
        try {
            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_crypto_asset (id_crypto_asset, ativo, quantidade, "
                            + "data_aquisicao, origem, tipo_ativo, id_user) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                stm.setString(1, ativo.getId());
                stm.setString(2, ativo.getAsset());
                stm.setDouble(3, ativo.getQuantity());
                stm.setTimestamp(4, toTimestamp(ativo.getAcquired()));
                stm.setString(5, ativo.getSource());
                stm.setString(6, ativo.getAssetType().getCodigo());
                stm.setString(7, ativo.getUserId());
                stm.executeUpdate();
            }

            inserirSubtipo(ativo);

            conexao.commit();
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public CryptoAsset findById(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(SELECT + " WHERE a.id_crypto_asset = ?")) {
            stm.setString(1, id);
            try (ResultSet result = stm.executeQuery()) {
                if (!result.next()) {
                    throw new EntityNotFoundException("Ativo não encontrado: " + id);
                }
                return parse(result);
            }
        }
    }

    public List<CryptoAsset> findAll() throws SQLException {
        return query(SELECT + " ORDER BY a.data_aquisicao DESC", null);
    }

    public List<CryptoAsset> findByUser(String userId) throws SQLException {
        return query(SELECT + " WHERE a.id_user = ? ORDER BY a.data_aquisicao DESC", userId);
    }

    public List<CryptoAsset> findByType(TipoAtivo tipo) throws SQLException {
        return query(SELECT + " WHERE a.tipo_ativo = ? ORDER BY a.ativo", tipo.getCodigo());
    }

    public void update(CryptoAsset ativo) throws SQLException, EntityNotFoundException {
        conexao.setAutoCommit(false);
        try {
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_crypto_asset SET ativo = ?, quantidade = ?, data_aquisicao = ?, "
                            + "origem = ? WHERE id_crypto_asset = ?")) {
                stm.setString(1, ativo.getAsset());
                stm.setDouble(2, ativo.getQuantity());
                stm.setTimestamp(3, toTimestamp(ativo.getAcquired()));
                stm.setString(4, ativo.getSource());
                stm.setString(5, ativo.getId());
                if (stm.executeUpdate() == 0) {
                    throw new EntityNotFoundException("Ativo não encontrado: " + ativo.getId());
                }
            }

            atualizarSubtipo(ativo);

            conexao.commit();
        } catch (SQLException | EntityNotFoundException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public void delete(String id) throws SQLException, EntityNotFoundException {
        try (PreparedStatement stm = prepare(
                "DELETE FROM t_axii_crypto_asset WHERE id_crypto_asset = ?")) {
            stm.setString(1, id);
            if (stm.executeUpdate() == 0) {
                throw new EntityNotFoundException("Ativo não encontrado: " + id);
            }
        }
    }

    public List<ResumoCarteira> resumoPorUsuario() throws SQLException {
        String sql = "SELECT u.id_user, u.nome, "
                + "       COUNT(a.id_crypto_asset) AS qtd_ativos, "
                + "       NVL(SUM(a.quantidade * NVL(c.preco_mercado, s.taxa_conversao)), 0) AS valor_total "
                + "  FROM t_axii_user u "
                + "  LEFT JOIN t_axii_crypto_asset a ON a.id_user       = u.id_user "
                + "  LEFT JOIN t_axii_coin c         ON c.id_coin       = a.id_crypto_asset "
                + "  LEFT JOIN t_axii_stablecoin s   ON s.id_stablecoin = a.id_crypto_asset "
                + " GROUP BY u.id_user, u.nome "
                + " ORDER BY valor_total DESC, u.nome";

        List<ResumoCarteira> resumos = new ArrayList<>();
        try (PreparedStatement stm = prepare(sql);
             ResultSet result = stm.executeQuery()) {
            while (result.next()) {
                resumos.add(new ResumoCarteira(
                        result.getString("id_user"),
                        result.getString("nome"),
                        result.getInt("qtd_ativos"),
                        result.getDouble("valor_total")));
            }
        }
        return resumos;
    }

    private void inserirSubtipo(CryptoAsset ativo) throws SQLException {
        if (ativo instanceof Coin coin) {
            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_coin (id_coin, simbolo, blockchain, preco_mercado) "
                            + "VALUES (?, ?, ?, ?)")) {
                stm.setString(1, coin.getId());
                stm.setString(2, coin.getSymbol());
                stm.setString(3, coin.getBlockchain());
                stm.setDouble(4, coin.getMarketPrice());
                stm.executeUpdate();
            }
        } else if (ativo instanceof Stablecoin stablecoin) {
            try (PreparedStatement stm = prepare(
                    "INSERT INTO t_axii_stablecoin (id_stablecoin, moeda_referencia, taxa_conversao) "
                            + "VALUES (?, ?, ?)")) {
                stm.setString(1, stablecoin.getId());
                stm.setString(2, stablecoin.getCurrency());
                stm.setDouble(3, stablecoin.getConversionRate());
                stm.executeUpdate();
            }
        }
    }

    private void atualizarSubtipo(CryptoAsset ativo) throws SQLException {
        if (ativo instanceof Coin coin) {
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_coin SET simbolo = ?, blockchain = ?, preco_mercado = ? "
                            + "WHERE id_coin = ?")) {
                stm.setString(1, coin.getSymbol());
                stm.setString(2, coin.getBlockchain());
                stm.setDouble(3, coin.getMarketPrice());
                stm.setString(4, coin.getId());
                stm.executeUpdate();
            }
        } else if (ativo instanceof Stablecoin stablecoin) {
            try (PreparedStatement stm = prepare(
                    "UPDATE t_axii_stablecoin SET moeda_referencia = ?, taxa_conversao = ? "
                            + "WHERE id_stablecoin = ?")) {
                stm.setString(1, stablecoin.getCurrency());
                stm.setDouble(2, stablecoin.getConversionRate());
                stm.setString(3, stablecoin.getId());
                stm.executeUpdate();
            }
        }
    }

    private List<CryptoAsset> query(String sql, String parametro) throws SQLException {
        List<CryptoAsset> lista = new ArrayList<>();
        try (PreparedStatement stm = prepare(sql)) {
            if (parametro != null) {
                stm.setString(1, parametro);
            }
            try (ResultSet result = stm.executeQuery()) {
                while (result.next()) {
                    lista.add(parse(result));
                }
            }
        }
        return lista;
    }

    private CryptoAsset parse(ResultSet result) throws SQLException {
        TipoAtivo tipo = TipoAtivo.fromCodigo(result.getString("tipo_ativo"));

        String id = result.getString("id_crypto_asset");
        String ativo = result.getString("ativo");
        double quantidade = result.getDouble("quantidade");
        LocalDateTime aquisicao = toLocalDateTime(result.getTimestamp("data_aquisicao"));
        String origem = result.getString("origem");
        String idUser = result.getString("id_user");

        return switch (tipo) {
            case COIN -> new Coin(id, ativo, quantidade, aquisicao, origem, idUser,
                    result.getString("simbolo"),
                    result.getString("blockchain"),
                    result.getDouble("preco_mercado"));
            case STABLECOIN -> new Stablecoin(id, ativo, quantidade, aquisicao, origem, idUser,
                    result.getString("moeda_referencia"),
                    result.getDouble("taxa_conversao"));
        };
    }
}
