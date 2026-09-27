package br.com.fiap.model;

public class Identity {
    private String id;
    private String phone;
    private String cpf;

    public Identity() {
    }

    public Identity(String id, String phone, String cpf) {
        this.id = id;
        this.phone = phone;
        this.cpf = cpf;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
