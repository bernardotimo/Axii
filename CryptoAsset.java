import java.time.LocalDateTime;

public class CryptoAsset {
    private String id;
    private String asset;
    private double quantity;
    private LocalDateTime acquired;
    private String source;
    private String type;

    public CryptoAsset() {
    }

    public CryptoAsset(String id, String asset, double quantity,
                       LocalDateTime acquired, String source, String type) {
        this.id = id;
        this.asset = asset;
        this.quantity = quantity;
        this.acquired = acquired;
        this.source = source;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}