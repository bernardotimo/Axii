package br.com.fiap.model;

public class Settings {
    private String id;
    private boolean darkMode;
    private String language;

    public Settings() {
    }

    public Settings(String language, boolean darkMode) {
        this.language = language;
        this.darkMode = darkMode;
    }

    public Settings(String id, String language, boolean darkMode) {
        this.id = id;
        this.language = language;
        this.darkMode = darkMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
