package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITChatMessageServiceSelenium {
	@Autowired
	private UserService userService;

	/** The driver. */
	private static WebDriver driver;

	/** The Constant BASE_URL. */
	private static final String BASE_URL = "http://localhost:7080/CashFIC/";

	/** The login user 1. */
	private final String LOGIN_USER_1 = "santiago.paz";

	/** The login user 2. */
	private final String LOGIN_USER_2 = "adrian.ulla";

	/** The password. */
	private final String PASSWORD = "password";

	/** The message. */
	private final String MESSAGE = "message";

	private ITChatMessageServiceSelenium() {
		super();
	}

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.firefoxdriver().setup();
	}

	@BeforeEach
	public void setUpTest() {
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
	}

	@AfterEach
	public void tearDownTest() {
		driver.close();
	}

	@Test
	void testChat() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		DefaultUserEntity userToChat = userService.findByLogin(LOGIN_USER_2);
		String userToChatId = userToChat.getId().toString();
		signIn(LOGIN_USER_1, PASSWORD);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuLogged")));

		WebElement element = driver.findElement(By.id("menuLogged"));
		assertNotNull(element);
		element.click();

		element = driver.findElement(By.id("chat"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitMessage")));

		element = driver.findElement(By.id("submitMessage"));
		assertNotNull(element);
		assertFalse(element.isEnabled());

		driver.get(BASE_URL + "chat/" + userToChatId);

		Thread.sleep(500);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitMessage")));

		element = driver.findElement(By.id("submitMessage"));
		assertNotNull(element);
		assertTrue(element.isEnabled());

		element = driver.findElement(By.id("titleChat"));
		assertNotNull(element);

		element = driver.findElement(By.linkText(LOGIN_USER_2));
		assertNotNull(element);

		element = driver.findElement(By.id("message"));
		element.sendKeys(MESSAGE);

		element = driver.findElement(By.id("submitMessage"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitMessage")));

		element = driver.findElement(By.id("message"));
		element.sendKeys(MESSAGE);
		element = driver.findElement(By.id("submitMessage"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitMessage")));

		element = driver.findElement(By.id("message"));
		element.sendKeys(MESSAGE);
		element = driver.findElement(By.id("submitMessage"));
		assertNotNull(element);
		element.submit();
	}

	private void signIn(String login, String password) throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		driver.get(BASE_URL);

		WebElement element = driver.findElement(By.id("menuAnonymous"));
		assertEquals("Account", element.getText());
		element.click();
		element = driver.findElement(By.id("signIn"));
		assertEquals(" Sign In", element.getText());
		element.click();

		element = driver.findElement(By.id("login"));
		element.clear();
		element.sendKeys(login);
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(password);
		element = driver.findElement(By.id("submit"));
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuLogged")));

		element = driver.findElement(By.id("menuLogged"));
		assertNotNull(element);
	}
}
