package br.com.fiap.model;

import br.com.fiap.enums.TipoAtivo;
import br.com.fiap.records.TempoDecorrido;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public abstract class CryptoAsset {
    protected String id;
    protected String asset;
    protected double quantity;
    protected LocalDateTime acquired;
    protected String source;
    protected String userId;

    public CryptoAsset() {
    }

    public CryptoAsset(String id, String asset, double quantity,
                       LocalDateTime acquired, String source) {
        this.id = id;
        this.asset = asset;
        this.quantity = quantity;
        this.acquired = acquired;
        this.source = source;
    }

    public CryptoAsset(String id, String asset, double quantity,
                       LocalDateTime acquired, String source, String userId) {
        this(id, asset, quantity, acquired, source);
        this.userId = userId;
    }

    public abstract double getCurrentValue();

    public abstract boolean canStaking();

    public abstract TipoAtivo getAssetType();

    public double getTotalValue() {
        return this.quantity * getCurrentValue();
    }

    public double getTotalValue(double exchangeRate) {
        return this.quantity * exchangeRate * getCurrentValue();
    }

    public double getTotalValue(double exchangeRate, double feePercent) {
        double total = this.quantity * getCurrentValue() * exchangeRate;
        return total - (total * feePercent / 100);
    }

    // Há quanto tempo o ativo está na carteira (anos, meses e dias)
    public TempoDecorrido getHoldingTime() {
        return acquired == null ? null : TempoDecorrido.desde(acquired);
    }

    // Quantidade de dias completos desde a aquisição
    public long getHoldingDays() {
        return acquired == null ? 0 : ChronoUnit.DAYS.between(acquired, LocalDateTime.now());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getAcquired() {
        return acquired;
    }

    public void setAcquired(LocalDateTime acquired) {
        this.acquired = acquired;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
