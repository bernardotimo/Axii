package br.com.fiap.model;

import br.com.fiap.enums.TipoChavePix;

public class PixKey {
    private String id;
    private String key;
    private TipoChavePix type;
    private String userId;

    public PixKey() {
    }

    public PixKey(String id, String key, TipoChavePix type) {
        this.id = id;
        this.key = key;
        this.type = type;
    }

    public PixKey(String id, String key, TipoChavePix type, String userId) {
        this(id, key, type);
        this.userId = userId;
    }

    public boolean isKeyValid() {
        return type != null && type.aceita(key);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TipoChavePix getType() {
        return type;
    }

    public void setType(TipoChavePix type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
