package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Optional;
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

import es.udc.fi.dc.fd.model.persistence.BuyTransactionId;
import es.udc.fi.dc.fd.model.persistence.DefaultBuyTransactionEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.BuyTransactionRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.service.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITSaleAdvertisementServiceSelenium {
	@Autowired
	private UserService userService;

	@Autowired
	private SaleAdvertisementRepository saleAdvertisementRepository;

	@Autowired
	private BuyTransactionRepository buyTransactionRepository;

	/** The driver. */
	private static WebDriver driver;

	/** The Constant BASE_URL. */
	private static final String BASE_URL = "http://localhost:7080/CashFIC/";

	/** The login user 1. */
	private final String LOGIN_USER_1 = "santiago.paz";

	/** The login user 2. */
	private final String LOGIN_USER_2 = "adrian.ulla";

	/** The login user 3. */
	private final String LOGIN_USER_3 = "user.example";

	/** The password. */
	private final String PASSWORD = "password";

	private final String SALE_NAME = "selenium";

	private final String SALE_DESCRIPTION = "sale for testing selenium";

	private final String SALE_PRICE = "500";

	private ITSaleAdvertisementServiceSelenium() {
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
	void testCreateAndDeleteSaleAdvertisement() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		DefaultUserEntity user = userService.findByLogin(LOGIN_USER_3);
		String userId = user.getId().toString();
		signIn(LOGIN_USER_3, PASSWORD);

		driver.get(BASE_URL);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add")));

		WebElement element = driver.findElement(By.id("add"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSaleAdvertisement")));

		element = driver.findElement(By.id("submitSaleAdvertisement"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorProductTitle")));

		element = driver.findElement(By.id("errorProductTitle"));
		assertNotNull(element);
		element = driver.findElement(By.id("errorProductDescription"));
		assertNotNull(element);
		element = driver.findElement(By.id("errorPrice"));
		assertNotNull(element);

		element = driver.findElement(By.id("productTitle"));
		element.sendKeys(SALE_NAME);
		element = driver.findElement(By.id("productDescription"));
		element.sendKeys(SALE_DESCRIPTION);
		element = driver.findElement(By.id("price"));
		element.sendKeys(SALE_PRICE);

		element = driver.findElement(By.id("submitSaleAdvertisement"));
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("changeSaleState")));

		element = driver.findElement(By.id("changeSaleState"));
		assertNotNull(element);
		element.click();

		Thread.sleep(500);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmChangeSaleState")));

		element = driver.findElement(By.id("confirmChangeSaleState"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("onHoldInfo")));

		element = driver.findElement(By.id("onHoldInfo"));
		assertNotNull(element);

		element = driver.findElement(By.id("changeSaleState"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmChangeSaleState")));

		element = driver.findElement(By.id("confirmChangeSaleState"));
		assertNotNull(element);
		element.submit();

		Thread.sleep(500);

		element = driver.findElement(By.id("list"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSearchCriteria")));

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);

		LocalDate time = LocalDate.now();

		element = driver.findElement(By.id("keywords"));
		element.sendKeys(SALE_NAME);
		element = driver.findElement(By.id("city"));
		element.sendKeys(user.getCity());
		element = driver.findElement(By.id("minDate"));
		element.sendKeys(time.toString());
		element = driver.findElement(By.id("maxDate"));
		element.sendKeys(time.toString());
		element = driver.findElement(By.id("minPrice"));
		element.sendKeys(SALE_PRICE);
		element = driver.findElement(By.id("maxPrice"));
		element.sendKeys(SALE_PRICE);
		element = driver.findElement(By.id("submitSearchCriteria"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSearchCriteria")));

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);

		driver.get(BASE_URL + "profile/" + userId + "/advertisements");

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("removeSaleAdvertisement")));

		element = driver.findElement(By.id("removeSaleAdvertisement"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmRemove")));

		element = driver.findElement(By.id("confirmRemove"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addSaleAdvertisementRemove")));

		element = driver.findElement(By.id("addSaleAdvertisementRemove"));
		assertNotNull(element);
	}

	@Test
	void testLikeAndBuySaleAdvertisement() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		DefaultUserEntity userOwnerSaleAdvertisement = userService.findByLogin(LOGIN_USER_1);
		String userOwnerSaleAdvertisementId = userOwnerSaleAdvertisement.getId().toString();
		signIn(LOGIN_USER_1, PASSWORD);

		driver.get(BASE_URL);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add")));

		WebElement element = driver.findElement(By.id("add"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSaleAdvertisement")));

		element = driver.findElement(By.id("productTitle"));
		element.sendKeys(SALE_NAME);
		element = driver.findElement(By.id("productDescription"));
		element.sendKeys(SALE_DESCRIPTION);
		element = driver.findElement(By.id("price"));
		element.sendKeys(SALE_PRICE);

		element = driver.findElement(By.id("submitSaleAdvertisement"));
		element.submit();

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);
		element.click();

		String saleAdvertisementID = driver.getCurrentUrl();
		int index = saleAdvertisementID.lastIndexOf("/");
		saleAdvertisementID = saleAdvertisementID.substring(index + 1);

		Thread.sleep(500);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuLogged")));

		element = driver.findElement(By.id("menuLogged"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logOut")));

		element = driver.findElement(By.id("logOut"));
		assertNotNull(element);
		element.click();

		DefaultUserEntity userLogged = userService.findByLogin(LOGIN_USER_2);
		String userLoggedId = userLogged.getId().toString();
		signIn(LOGIN_USER_2, PASSWORD);

		driver.get(BASE_URL + "profile/" + userOwnerSaleAdvertisementId);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("followUser")));

		element = driver.findElement(By.id("followUser"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("followedList")));

		element = driver.findElement(By.id("followedList"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSearchCriteria")));

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);

		LocalDate time = LocalDate.now();

		element = driver.findElement(By.id("keywords"));
		element.sendKeys(SALE_NAME);
		element = driver.findElement(By.id("city"));
		element.sendKeys(userOwnerSaleAdvertisement.getCity());
		element = driver.findElement(By.id("minDate"));
		element.sendKeys(time.toString());
		element = driver.findElement(By.id("maxDate"));
		element.sendKeys(time.toString());
		element = driver.findElement(By.id("minPrice"));
		element.sendKeys(SALE_PRICE);
		element = driver.findElement(By.id("maxPrice"));
		element.sendKeys(SALE_PRICE);
		element = driver.findElement(By.id("submitSearchCriteria"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitSearchCriteria")));

		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);

		driver.get(BASE_URL + "saleAdvertisement/" + saleAdvertisementID);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("likeSaleAdvertisement")));

		element = driver.findElement(By.id("likeSaleAdvertisement"));
		assertNotNull(element);
		element.click();

		driver.get(BASE_URL + "profile/" + userLoggedId + "/likes");
		element = driver.findElement(By.linkText(SALE_NAME));
		assertNotNull(element);

		driver.get(BASE_URL + "saleAdvertisement/" + saleAdvertisementID);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("unlikeSaleAdvertisement")));

		element = driver.findElement(By.id("unlikeSaleAdvertisement"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buySaleAdvertisement")));

		element = driver.findElement(By.id("buySaleAdvertisement"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saleAdvertisementSoldInfo")));

		element = driver.findElement(By.id("saleAdvertisementSoldInfo"));
		assertNotNull(element);

		element = driver.findElement(By.id("menuLogged"));
		assertNotNull(element);
		element.click();

		element = driver.findElement(By.id("history"));
		assertNotNull(element);
		element.click();

		driver.get(BASE_URL + "profile/" + userOwnerSaleAdvertisementId);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("unfollowUser")));

		element = driver.findElement(By.id("unfollowUser"));
		assertNotNull(element);
		element.click();

		BuyTransactionId transactionIdentifier = new BuyTransactionId(userLogged.getId(),
				Integer.parseInt(saleAdvertisementID));

		if (buyTransactionRepository.existsById(transactionIdentifier)) {
			Optional<DefaultBuyTransactionEntity> buyTransacionToDelete = buyTransactionRepository
					.findById(transactionIdentifier);
			buyTransactionRepository.delete(buyTransacionToDelete.get());
		}

		Optional<DefaultSaleAdvertisementEntity> saleAdvertisementToDelete = saleAdvertisementRepository
				.findById(Integer.parseInt(saleAdvertisementID));
		if (saleAdvertisementToDelete.isPresent()) {
			saleAdvertisementRepository.delete(saleAdvertisementToDelete.get());
		}
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
