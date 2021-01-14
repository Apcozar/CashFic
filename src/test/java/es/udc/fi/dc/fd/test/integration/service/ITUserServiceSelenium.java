/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.udc.fi.dc.fd.test.integration.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
import org.openqa.selenium.JavascriptExecutor;
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

import es.udc.fi.dc.fd.model.persistence.DefaultRateUserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.model.persistence.RateUserKey;
import es.udc.fi.dc.fd.repository.RateUserRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import io.github.bonigarcia.wdm.WebDriverManager;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:context/service.xml", "classpath:context/persistence.xml",
		"classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITUserServiceSelenium {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userDao;

	@Autowired
	private RateUserRepository rateUserDao;

	/** The driver. */
	private static WebDriver driver;

	/** The Constant BASE_URL. */
	private static final String BASE_URL = "http://localhost:7080/CashFIC/";

	/** The non existent email. */
	private final String INCORRECT_EMAIL = "asd";

	/** The non existent user name. */
	private final String INCORRECT_LOGIN = "a";

	/** The login2. */
	private final String LOGIN = "loginUser";

	/** The login user 1. */
	private final String LOGIN_USER_1 = "santiago.paz";

	/** The login user 2. */
	private final String LOGIN_USER_2 = "adrian.ulla";

	/** The login user 3. */
	private final String LOGIN_USER_3 = "user.example";

	/** The email. */
	private final String EMAIL = "example@udc.es";

	/** The email2. */
	private final String EMAIL2 = "user2@udc.es";

	/** The password. */
	private final String PASSWORD = "password";

	/** The incorrect password. */
	private final String INCORRECT_PASSWORD = "pass";

	/** The name. */
	private final String NAME = "userName";

	/** The last name. */
	private final String LAST_NAME = "userLastName";

	/** The city. */
	private final String CITY = "userCity";

	private ITUserServiceSelenium() {
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
	void testSignUpAndRateUser() throws Exception {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 40);

			driver.get(BASE_URL);

			WebElement element = driver.findElement(By.id("menuAnonymous"));
			assertEquals("Account", element.getText());
			element.click();
			element = driver.findElement(By.id("signUp"));
			assertEquals(" Sign Up", element.getText());
			element.click();

			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginError")));

			element = driver.findElement(By.id("loginError"));
			assertNotNull(element);
			element = driver.findElement(By.id("passwordError"));
			assertNotNull(element);
			element = driver.findElement(By.id("nameError"));
			assertNotNull(element);
			element = driver.findElement(By.id("lastNameError"));
			assertNotNull(element);
			element = driver.findElement(By.id("emailError"));
			assertNotNull(element);
			element = driver.findElement(By.id("cityError"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.sendKeys(INCORRECT_LOGIN);
			element = driver.findElement(By.id("password"));
			element.sendKeys(INCORRECT_PASSWORD);
			element = driver.findElement(By.id("name"));
			element.sendKeys(NAME);
			element = driver.findElement(By.id("lastName"));
			element.sendKeys(LAST_NAME);
			element = driver.findElement(By.id("email"));
			element.sendKeys(INCORRECT_EMAIL);
			element = driver.findElement(By.id("city"));
			element.sendKeys(CITY);
			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginError")));

			element = driver.findElement(By.id("loginError"));
			assertNotNull(element);
			element = driver.findElement(By.id("passwordError"));
			assertNotNull(element);
			element = driver.findElement(By.id("emailError"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN_USER_3);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL);
			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorLoginExists")));

			element = driver.findElement(By.id("errorLoginExists"));
			assertNotNull(element);
			element = driver.findElement(By.id("errorEmailExist"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN_USER_3);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL2);
			element = driver.findElement(By.id("submit"));
			element.submit();

			Thread.sleep(500);

			element = driver.findElement(By.id("errorLoginExists"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL);
			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorEmailExist")));

			element = driver.findElement(By.id("errorEmailExist"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN_USER_3);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL);
			element = driver.findElement(By.id("name"));
			element.clear();
			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorEmailExist")));

			element = driver.findElement(By.id("errorEmailExist"));
			assertNotNull(element);
			element = driver.findElement(By.id("errorLoginExists"));
			assertNotNull(element);
			element = driver.findElement(By.id("nameError"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL);
			element = driver.findElement(By.id("name"));
			element.clear();
			element = driver.findElement(By.id("submit"));
			element.submit();

			Thread.sleep(500);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorEmailExist")));

			element = driver.findElement(By.id("errorEmailExist"));
			assertNotNull(element);
			element = driver.findElement(By.id("nameError"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN_USER_3);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL2);
			element = driver.findElement(By.id("name"));
			element.clear();
			element = driver.findElement(By.id("submit"));
			element.submit();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorLoginExists")));

			element = driver.findElement(By.id("errorLoginExists"));
			assertNotNull(element);
			element = driver.findElement(By.id("nameError"));
			assertNotNull(element);

			element = driver.findElement(By.id("login"));
			element.clear();
			element.sendKeys(LOGIN);
			element = driver.findElement(By.id("password"));
			element.clear();
			element.sendKeys(PASSWORD);
			element = driver.findElement(By.id("email"));
			element.clear();
			element.sendKeys(EMAIL2);
			element = driver.findElement(By.id("name"));
			element.clear();
			element.sendKeys(NAME);
			element = driver.findElement(By.id("submit"));
			element.submit();

			Thread.sleep(500);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuLogged")));

			element = driver.findElement(By.id("menuLogged"));
			assertEquals(LOGIN, element.getText());
			element.click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("profile")));

			element = driver.findElement(By.id("profile"));
			assertEquals("Profile", element.getText());
			element.click();

			element = driver.findElement(By.linkText(LOGIN));
			assertEquals(LOGIN, element.getText());
			element.click();

			DefaultUserEntity userToRate = userService.findByLogin(LOGIN_USER_2);
			Integer idUserToRate = userToRate.getId();

			driver.get(BASE_URL + "profile/" + idUserToRate.toString());

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitRate")));

			element = driver.findElement(By.id("star3"));
			assertNotNull(element);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", element);

			element = driver.findElement(By.id("submitRate"));
			assertNotNull(element);
			element.click();

			Thread.sleep(500);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ratedValue")));

			element = driver.findElement(By.id("ratedValue"));
			assertNotNull(element);

			DefaultUserEntity user2 = userService.findByLogin(LOGIN);

			RateUserKey rateIdentifier = new RateUserKey(user2.getId(), userToRate.getId());

			if (rateUserDao.existsById(rateIdentifier)) {
				Optional<DefaultRateUserEntity> rateEntityToDelete = rateUserDao.findById(rateIdentifier);
				rateUserDao.delete(rateEntityToDelete.get());
			}

			userDao.delete(user2);
		} catch (UserNotFoundException e) {

		}
	}

	@Test
	void testSignInAndBecamePremium() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		driver.get(BASE_URL);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuAnonymous")));

		WebElement element = driver.findElement(By.id("menuAnonymous"));
		assertEquals("Account", element.getText());
		element.click();
		element = driver.findElement(By.id("signIn"));
		assertEquals(" Sign In", element.getText());
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));

		element = driver.findElement(By.id("login"));
		element.clear();
		element.sendKeys(INCORRECT_LOGIN);
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(PASSWORD);
		element = driver.findElement(By.id("submit"));
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("signInError")));

		element = driver.findElement(By.id("signInError"));
		assertNotNull(element);
		assertEquals("Login or password incorrect", element.getText());

		element = driver.findElement(By.id("login"));
		element.clear();
		element.sendKeys(LOGIN_USER_3);
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(INCORRECT_PASSWORD);
		element = driver.findElement(By.id("submit"));
		element.submit();

		Thread.sleep(500);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));

		element = driver.findElement(By.id("signInError"));
		assertNotNull(element);
		assertEquals("Login or password incorrect", element.getText());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login")));
		element = driver.findElement(By.id("login"));
		element.clear();
		element.sendKeys(LOGIN_USER_3);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
		element = driver.findElement(By.id("password"));
		element.clear();
		element.sendKeys(PASSWORD);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));
		element = driver.findElement(By.id("submit"));
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("menuLogged")));

		element = driver.findElement(By.id("menuLogged"));
		assertEquals(LOGIN_USER_3, element.getText());
		element.click();

		element = driver.findElement(By.id("profile"));
		assertEquals("Profile", element.getText());
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("becamePremium")));

		element = driver.findElement(By.id("becamePremium"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));

		element = driver.findElement(By.id("submit"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("becameUser")));

		element = driver.findElement(By.id("becameUser"));
		assertNotNull(element);
		element.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit")));

		element = driver.findElement(By.id("submit"));
		assertNotNull(element);
		element.submit();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("becamePremium")));

		element = driver.findElement(By.id("becamePremium"));
		assertNotNull(element);

	}

	@Test
	void testFollowAndUnfollowUser() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 40);

		signIn(LOGIN_USER_3, PASSWORD);

		try {
			DefaultUserEntity user = userService.findByLogin(LOGIN_USER_3);
			Integer idUser = user.getId();

			DefaultUserEntity userToFollow = userService.findByLogin(LOGIN_USER_1);
			Integer idUserToFollow = userToFollow.getId();

			driver.get(BASE_URL + "profile/" + idUserToFollow);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("followUser")));

			WebElement element = driver.findElement(By.id("followUser"));
			assertNotNull(element);
			element.click();

			driver.get(BASE_URL + "profile/followers/" + idUserToFollow);
			List<WebElement> elements = driver.findElements(By.linkText(LOGIN_USER_3));
			assertNotNull(element);
			assertEquals(2, elements.size());

			driver.get(BASE_URL + "profile/followed/" + idUser);
			element = driver.findElement(By.linkText(LOGIN_USER_1));
			assertNotNull(element);
			element.click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("unfollowUser")));

			element = driver.findElement(By.id("unfollowUser"));
			assertNotNull(element);
			element.click();

		} catch (UserNotFoundException e) {

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
