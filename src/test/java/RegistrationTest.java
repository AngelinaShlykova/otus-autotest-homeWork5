import driver.DriverFactory;
import pages.RegistrationPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistrationTest {

    private static final Logger logger = LogManager.getLogger(RegistrationTest.class);

    private WebDriver driver;
    private RegistrationPage registrationPage;

    private String baseUrl;
    private String testUsername;
    private String testEmail;
    private String testPassword;
    private String testConfirmPassword;
    private String testBirthDate;
    private String testLanguageLevel;
    private String expectedLanguageLevel;

    @BeforeAll
    public void setUpAll() {
        logger.info("Начало тестирования");

        baseUrl = System.getProperty("test.url", "https://otus.home.kartushin.su/form.html");
        testUsername = System.getProperty("test.username", "Shlykova Angelina");
        testEmail = System.getProperty("test.email", "angelinaslykova67065@gmail.com");
        testPassword = System.getProperty("test.password", "SecurePass123");
        testConfirmPassword = System.getProperty("test.confirmPassword", testPassword);
        testBirthDate = System.getProperty("test.birthdate", "23-01-1996");
        testLanguageLevel = System.getProperty("test.languagelevel", "Средний");
        expectedLanguageLevel = "intermediate";

        logger.info("Базовый URL: {}", baseUrl);
        logger.info("Имя пользователя: {}", testUsername);
        logger.info("Email: {}", testEmail);
        logger.info("Дата рождения: {}", testBirthDate);
        logger.info("Уровень языка (ввод): {}", testLanguageLevel);
        logger.info("Уровень языка (ожидаемый): {}", expectedLanguageLevel);
        logger.info("Тестовые данные загружены из системных свойств");
    }

    @BeforeEach
    public void setUp() {
        logger.info("Инициализация драйвера и страницы");
        driver = DriverFactory.createDriver();
        registrationPage = new RegistrationPage(driver);
        registrationPage.open(baseUrl);
    }

    @Test
    @DisplayName("Тест успешной регистрации пользователя")
    public void testSuccessfulRegistration() {
        logger.info("Запуск теста: успешная регистрация");

        assertTrue(testPassword.equals(testConfirmPassword),
                "Пароли должны совпадать");

        registrationPage.fillForm(
                testUsername,
                testEmail,
                testPassword,
                testConfirmPassword,
                testBirthDate,
                testLanguageLevel
        );

        registrationPage.submitForm();
        assertTrue(registrationPage.isResultDisplayed(),
                "Результат должен отобразиться после отправки формы");

        String resultUsername = registrationPage.getResultUsername();
        String resultEmail = registrationPage.getResultEmail();
        String resultBirthDate = registrationPage.getResultBirthDate();
        String resultLanguageLevel = registrationPage.getResultLanguageLevel();

        logger.info("Результаты:");
        logger.info("  Имя: '{}'", resultUsername);
        logger.info("  Email: '{}'", resultEmail);
        logger.info("  Дата рождения: '{}'", resultBirthDate);
        logger.info("  Уровень языка: '{}'", resultLanguageLevel);

        assertEquals(testUsername.toLowerCase().replace(" ", ""),
                resultUsername.toLowerCase().replace(" ", ""),
                "Имя пользователя должно совпадать");

        assertEquals(testEmail.toLowerCase(),
                resultEmail.toLowerCase(),
                "Email должен совпадать");

        assertEquals("1996-01-23",
                resultBirthDate,
                "Дата рождения должна совпадать");

        assertEquals(expectedLanguageLevel,
                resultLanguageLevel.toLowerCase(),
                "Уровень языка должен совпадать");

        logger.info("Тест пройден");
    }

    @Test
    @DisplayName("Тест проверки несовпадения паролей")
    public void testPasswordMismatch() {
        logger.info("Запуск теста: проверка несовпадения паролей");

        String password1 = "Password123";
        String password2 = "Password456";

        assertFalse(password1.equals(password2),
                "Пароли не должны совпадать");

        logger.info("Тест пройден");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            logger.info("Закрытие браузера");
            DriverFactory.quitDriver();
        }
    }

    @AfterAll
    public void tearDownAll() {
        logger.info("Тестирование завершено");
    }
}
