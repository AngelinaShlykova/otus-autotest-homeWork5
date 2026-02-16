package driver;

public enum BrowserType {
    CHROME,
    FIREFOX,
    EDGE;

    public static BrowserType fromProperty() {
        String browserName = System.getProperty("browser", "chrome").toUpperCase();
        try {
            return BrowserType.valueOf(browserName);
        } catch (IllegalArgumentException e) {
            System.err.println("Неизвестный браузер: " + browserName + ". Используется CHROME по умолчанию.");
            return CHROME;
        }
    }
}
