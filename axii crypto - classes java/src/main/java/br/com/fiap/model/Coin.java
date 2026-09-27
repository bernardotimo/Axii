package br.com.fiap.model;

import br.com.fiap.enums.TipoAtivo;

import java.time.LocalDateTime;

public class Coin extends CryptoAsset {
    private String symbol;
    private String blockchain;
    private double marketPrice;

    public Coin() {
    }

    public Coin(String id, String asset, double quantity,
                LocalDateTime acquired, String source,
                String symbol, String blockchain, double marketPrice) {
        super(id, asset, quantity, acquired, source);
        this.symbol = symbol;
        this.blockchain = blockchain;
        this.marketPrice = marketPrice;
    }

    public Coin(String id, String asset, double quantity,
                LocalDateTime acquired, String source, String userId,
                String symbol, String blockchain, double marketPrice) {
        super(id, asset, quantity, acquired, source, userId);
        this.symbol = symbol;
        this.blockchain = blockchain;
        this.marketPrice = marketPrice;
    }

    @Override
    public double getCurrentValue() {
        return marketPrice;
    }

    @Override
    public boolean canStaking() {
        return true;
    }

    @Override
    public TipoAtivo getAssetType() {
        return TipoAtivo.COIN;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(String blockchain) {
        this.blockchain = blockchain;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }
}
