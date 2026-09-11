package br.com.fiap.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    private Settings settings;
    private Notifications notifications;
    private Identity identity;
    private List<CryptoAsset> cryptoAssets;
    private List<PixKey> pixKeys;
    private List<Bank> banks;

    public User(String id, String name, String email, String password,
                LocalDateTime updatedAt, LocalDateTime createdAt,
                Notifications notifications, Settings settings, Identity identity) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.notifications = notifications;
        this.settings = settings;
        this.identity = identity;
        this.cryptoAssets = new ArrayList<>();
        this.pixKeys = new ArrayList<>();
        this.banks = new ArrayList<>();
    }

    public void signUp() {
        System.out.println("Cadastrando");
    }

    public void signIn() {
        System.out.println("Logando");
    }

    public void signOut() {
        System.out.println("Saindo");
    }

    public void addCryptoAsset(CryptoAsset cryptoAsset) {
        this.cryptoAssets.add(cryptoAsset);
    }

    public void addCryptoAsset(String id, String asset, double quantity,
                               String symbol, String blockchain, double marketprice) {
        Coin coin = new Coin(id, asset, quantity, LocalDateTime.now(),
                "Manual", symbol, blockchain, marketprice);
        this.cryptoAssets.add(coin);
    }

    public void addCryptoAsset(List<CryptoAsset> cryptoAssets) {
        this.cryptoAssets.addAll(cryptoAssets);
    }

    public void addPixKey(PixKey pixKey) {
        this.pixKeys.add(pixKey);
    }

    public void addBank(Bank bank) {
        this.banks.add(bank);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Notifications getNotifications() {
        return notifications;
    }

    public void setNotifications(Notifications notifications) {
        this.notifications = notifications;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public List<CryptoAsset> getCryptoAssets() {
        return cryptoAssets;
    }

    public void setCryptoAssets(List<CryptoAsset> cryptoAssets) {
        this.cryptoAssets = cryptoAssets;
    }

    public List<PixKey> getPixKeys() {
        return pixKeys;
    }

    public void setPixKeys(List<PixKey> pixKeys) {
        this.pixKeys = pixKeys;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}