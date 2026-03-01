package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Фабрика для создания и настройки WebDriver
 * Драйвер НЕ статический — создаётся новый экземпляр для каждого теста
 */
public class DriverFactory {

    /**
     * Создание нового экземпляра драйвера
     * @return настроенный WebDriver
     */
    public static WebDriver createDriver() {
        BrowserType browserType = BrowserType.fromProperty();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                WebDriverManager.chromedriver()
                        .browserVersion(System.getProperty("browser.version", "116"))
                        .setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");

                if (headless) {
                    chromeOptions.addArguments("--headless=new");
                }
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");

                String chromiumPath = System.getProperty("chromium.path");
                if (chromiumPath != null && !chromiumPath.isEmpty()) {
                    chromeOptions.setBinary(chromiumPath);
                }

                driver = new ChromeDriver(chromeOptions);
                break;

            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Не поддерживаемый браузер: " + browserType);
        }

        // Устанавливаем ТОЛЬКО необходимые таймауты:
        // 1. Implicit wait — для ожидания появления элементов (защита от "мигания" элементов)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 2. Page load timeout — для защиты от зависания при загрузке страницы
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // scriptTimeout НЕ устанавливаем — не используем executeAsyncScript

        return driver;
    }

    /**
     * Закрытие драйвера и освобождение ресурсов
     */
    public static void quitDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}