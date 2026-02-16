package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
public class RegistrationPage {

    private static final Logger logger = LogManager.getLogger(RegistrationPage.class);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "confirm_password")
    private WebElement confirmPasswordField;

    @FindBy(id = "birthdate")
    private WebElement birthDateField;

    @FindBy(id = "language_level")
    private WebElement languageLevelField;

    @FindBy(css = "input[type='submit'], button[type='submit']")
    private WebElement submitButton;

    @FindBy(id = "output")
    private WebElement outputBlock;

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.urlContains("form"));
    }

    public void setBirthDate(String birthDate) {
        logger.info("Заполнение поля 'Дата рождения': {}", birthDate);
        wait.until(ExpectedConditions.visibilityOf(birthDateField)).clear();
        birthDateField.sendKeys(birthDate);
    }

    public void submitForm() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", button);
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", button);
        logger.info("Форма отправлена");
    }

    public boolean isResultDisplayed() {
        wait.until(ExpectedConditions.visibilityOf(outputBlock));
        String outputText = outputBlock.getText().trim();
        return !outputText.isEmpty();
    }

    public String getResultUsername() {
        return extractValue("Имя пользователя:");
    }

    public String getResultEmail() {
        return extractValue("Электронная почта:");
    }

    public String getResultBirthDate() {
        return extractValue("Дата рождения:");
    }

    public String getResultLanguageLevel() {
        return extractValue("Уровень языка:");
    }

    /**
     * извлечение результатов
     */
    private String extractValue(String prefix) {
        String outputText = outputBlock.getText().trim();
        String[] lines = outputText.split("\\r?\\n|<br\\s*/?>");

        for (String line : lines) {
            line = line.trim();
            if (line.contains(prefix)) {
                return line.substring(line.indexOf(prefix) + prefix.length()).trim();
            }
        }
        return "";
    }

    public void setUsername(String username) {
        logger.info("Заполнение поля 'Имя пользователя': {}", username);
        wait.until(ExpectedConditions.visibilityOf(usernameField)).clear();
        usernameField.sendKeys(username);
    }

    public void setEmail(String email) {
        logger.info("Заполнение поля 'Email': {}", email);
        wait.until(ExpectedConditions.visibilityOf(emailField)).clear();
        emailField.sendKeys(email);
    }

    public void setPassword(String password) {
        logger.info("Заполнение поля 'Пароль'");
        wait.until(ExpectedConditions.visibilityOf(passwordField)).clear();
        passwordField.sendKeys(password);
    }

    public void setConfirmPassword(String confirmPassword) {
        logger.info("Заполнение поля 'Подтверждение пароля'");
        wait.until(ExpectedConditions.visibilityOf(confirmPasswordField)).clear();
        confirmPasswordField.sendKeys(confirmPassword);
    }

    public void setLanguageLevel(String level) {
        logger.info("Выбор уровня языка: {}", level);

        WebElement element = wait.until(ExpectedConditions.visibilityOf(languageLevelField));
        Select select = new Select(element);

        select.selectByVisibleText(level);
        logger.info("Уровень языка '{}' выбран", level);
    }

    /**
     * Заполнение всей формы
     */
    public void fillForm(String username, String email, String password,
                         String confirmPassword, String birthDate, String languageLevel) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setConfirmPassword(confirmPassword);
        setBirthDate(birthDate);
        setLanguageLevel(languageLevel);
    }
}
