package br.com.fiap.model;

import br.com.fiap.enums.TipoAtivo;

import java.time.LocalDateTime;

public class Stablecoin extends CryptoAsset {
    private String currency;
    private double conversionRate;

    public Stablecoin() {
    }

    public Stablecoin(String id, String asset, double quantity,
                      LocalDateTime acquired, String source, String currency, double conversionRate) {
        super(id, asset, quantity, acquired, source);
        this.currency = currency;
        this.conversionRate = conversionRate;
    }

    public Stablecoin(String id, String asset, double quantity,
                      LocalDateTime acquired, String source, String userId,
                      String currency, double conversionRate) {
        super(id, asset, quantity, acquired, source, userId);
        this.currency = currency;
        this.conversionRate = conversionRate;
    }

    @Override
    public double getCurrentValue() {
        return conversionRate;
    }

    @Override
    public boolean canStaking() {
        return true;
    }

    @Override
    public TipoAtivo getAssetType() {
        return TipoAtivo.STABLECOIN;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(double conversionRate) {
        this.conversionRate = conversionRate;
    }
}
