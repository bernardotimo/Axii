package br.com.fiap.model;

import java.time.LocalDateTime;

public abstract class CryptoAsset {
    protected String id;
    protected String asset;
    protected double quantity;
    protected LocalDateTime acquired;
    protected String source;

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

    public abstract double getCurrentValue();

    public abstract boolean canStaking();

    public abstract String getAssetType();

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
}