public class Notifications {
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

    public boolean isTransaction() {
        return transaction;
    }

    public boolean isPriceVariation() {
        return priceVariation;
    }

    public boolean isMarketing() {
        return marketing;
    }

    public void setTransaction(boolean transaction) {
        this.transaction = transaction;
    }

    public void setPriceVariation(boolean priceVariation) {
        this.priceVariation = priceVariation;
    }

    public void setMarketing(boolean marketing) {
        this.marketing = marketing;
    }
}