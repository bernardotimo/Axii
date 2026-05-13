public class Settings {
    private boolean darkMode;
    private String language;

    public Settings() {
    }

    public Settings(String language, boolean darkMode) {
        this.language = language;
        this.darkMode = darkMode;
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