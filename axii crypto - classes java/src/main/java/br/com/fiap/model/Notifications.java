package br.com.fiap.model;

public class Notifications {
    private String id;
    private boolean transaction;
    private boolean priceVariation;
    private boolean marketing;

    public Notifications() {
    }

    public Notifications(boolean transaction, boolean priceVariation, boolean marketing) {
        this.transaction = transaction;
        this.priceVariation = priceVariation;
        this.marketing = marketing;
    }

    public Notifications(String id, boolean transaction, boolean priceVariation, boolean marketing) {
        this.id = id;
        this.transaction = transaction;
        this.priceVariation = priceVariation;
        this.marketing = marketing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTransaction() {
        return transaction;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public boolean isPriceVariation() {
        return priceVariation;
    }

    public void setPriceVariation(boolean priceVariation) {
        this.priceVariation = priceVariation;
    }

    public boolean isMarketing() {
        return marketing;
    }

    public void setMarketing(boolean marketing) {
        this.marketing = marketing;
    }
}
