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
    private String expectedLanguageLevel = "intermediate";

    @BeforeAll
    public void setUpAll() {
        baseUrl = System.getProperty("test.url", "https://otus.home.kartushin.su/form.html");
    }

    @BeforeEach
    public void setUp() {
        driver = DriverFactory.createDriver();
        registrationPage = new RegistrationPage(driver);
        registrationPage.open(baseUrl);
    }

    @Test
    @DisplayName("Тест успешной регистрации пользователя")
    public void testSuccessfulRegistration() {

        String testUsername = "Shlykova Angelina";
        String testEmail = "angelinaslykova67065@gmail.com";
        String testPassword = "SecurePass123";
        String testConfirmPassword = "SecurePass123";
        String testBirthDate = "23-01-1996";
        String testLanguageLevel = "Средний";

        assertEquals(testPassword, testConfirmPassword, "Пароли должны совпадать");

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

        assertEquals(testUsername.toLowerCase().replace(" ", ""),
                registrationPage.getResultUsername().toLowerCase().replace(" ", ""),
                "Имя пользователя должно совпадать");

        assertEquals(testEmail.toLowerCase(),
                registrationPage.getResultEmail().toLowerCase(),
                "Email должен совпадать");

        assertEquals("1996-01-23",
                registrationPage.getResultBirthDate(),
                "Дата рождения должна совпадать");

        assertEquals(expectedLanguageLevel,
                registrationPage.getResultLanguageLevel().toLowerCase(),
                "Уровень языка должен совпадать");

        logger.info("Тест пройден");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            logger.info("Закрытие браузера");
            DriverFactory.quitDriver(driver);
            driver = null;
        }
    }

    @AfterAll
    public void tearDownAll() {
        logger.info("Тестирование завершено");
    }
}