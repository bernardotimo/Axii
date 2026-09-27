package br.com.fiap.model;

public class Bank {
    private String id;
    private String bankName;
    private boolean active;
    private String accountNumber;
    private int agency;
    private String userId;

    public Bank() {
    }

    public Bank(String id, String bankName, boolean active, String accountNumber, int agency) {
        this.id = id;
        this.bankName = bankName;
        this.active = active;
        this.accountNumber = accountNumber;
        this.agency = agency;
    }

    public Bank(String id, String bankName, boolean active, String accountNumber,
                int agency, String userId) {
        this(id, bankName, active, accountNumber, agency);
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAgency() {
        return agency;
    }

    public void setAgency(int agency) {
        this.agency = agency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
