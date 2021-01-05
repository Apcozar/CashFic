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

package es.udc.fi.dc.fd.test.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.RateUserRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.DefaultUserService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserIncorrectLoginException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * Unit tests for the {@link SaleAdvertisementService}.
 * <p>
 * As this service doesn't contain any actual business logic, and it just wraps
 * the example entities repository, these tests are for verifying everything is
 * set up correctly and working.
 */
@RunWith(JUnitPlatform.class)
final class TestUserService {

	@Mock
	private UserRepository userRepository;

	@Mock
	private SaleAdvertisementRepository saleAdvertisementRepository;

	@Mock
	private RateUserRepository rateUserRepository;
	/**
	 * Service being tested.
	 */
	@InjectMocks
	private DefaultUserService userService;

	/**
	 * Default constructor.
	 */
	public TestUserService() {
		super();

	}

	@BeforeEach
	public void initialize() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void signUpUserNullThrowException() {
		assertThrows(NullPointerException.class, () -> {
			userService.signUp(null);
		});
	}

	@Test
	void signUpUserLoginAndEmailExistsThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userEmail = "testUserEmail";
		user.setId(userId);
		user.setLogin(userLogin);
		user.setEmail(userEmail);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(true);
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(true);
		assertThrows(UserLoginAndEmailExistsException.class, () -> {
			userService.signUp(user);
		});
	}

	@Test
	void signUpUserLoginExistsThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userEmail = "testUserEmail";
		user.setId(userId);
		user.setLogin(userLogin);
		user.setEmail(userEmail);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(true);
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(false);
		assertThrows(UserLoginExistsException.class, () -> {
			userService.signUp(user);
		});
	}

	@Test
	void signUpUserEmailExistsThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userEmail = "testUserEmail";
		user.setId(userId);
		user.setLogin(userLogin);
		user.setEmail(userEmail);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(false);
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(true);
		assertThrows(UserEmailExistsException.class, () -> {
			userService.signUp(user);
		});
	}

	@Test
	void testSignUpUser() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userEmail = "testUserEmail";
		user.setId(userId);
		user.setLogin(userLogin);
		user.setEmail(userEmail);
		user.setPassword("passwordToEnconde");
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(false);
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(false);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		try {
			userService.signUp(user);
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	void loginUserNotFoundThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userPassword = "userPassword";
		user.setId(userId);
		user.setLogin(userLogin);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.login(userLogin, userPassword);
		});
	}

	@Test
	void loginUserBadPasswordThrowException() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedBadPassword = passwordEncoder.encode("badPassword");
		user.setId(userId);
		user.setLogin(userLogin);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(true);
		Mockito.when(userRepository.findByLogin(userLogin)).thenReturn(user);
		assertThrows(UserIncorrectLoginException.class, () -> {
			userService.login(userLogin, encodedBadPassword);
		});
	}

	@Test
	void loginUser() {
		DefaultUserEntity user = new DefaultUserEntity();
		int userId = 1;
		String userLogin = "testUserLogin";
		String userPassword = "userPassword";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(userPassword);
		user.setId(userId);
		user.setLogin(userLogin);
		user.setPassword(encodedPassword);
		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(true);
		Mockito.when(userRepository.findByLogin(userLogin)).thenReturn(user);

		try {
			DefaultUserEntity userReturned = userService.login(userLogin, userPassword);
			assertEquals(userReturned, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void findByLoginNotFoundThrowException() {
		int identifier = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(identifier);
		String userLogin = "userLogin";
		user.setLogin(userLogin);

		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.findByLogin(userLogin);
		});
	}

	@Test
	void findByLogin() {
		int identifier = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(identifier);
		String userLogin = "userLogin";
		user.setLogin(userLogin);

		Mockito.when(userRepository.existsByLogin(userLogin)).thenReturn(true);
		Mockito.when(userRepository.findByLogin(userLogin)).thenReturn(user);
		DefaultUserEntity userReturn;
		try {
			userReturn = userService.findByLogin(userLogin);
			assertEquals(userReturn, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void findByEmailNotFoundThrowException() {
		String userEmail = "userEmail";
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(false);
		assertThrows(UserEmailNotFoundException.class, () -> {
			userService.findByEmail(userEmail);
		});
	}

	@Test
	void findByEmailReturnUser() {
		int identifier = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(identifier);
		String userEmail = "userEmail";
		Mockito.when(userRepository.existsByEmail(userEmail)).thenReturn(true);
		Mockito.when(userRepository.findByEmail(userEmail)).thenReturn(user);
		try {
			assertEquals(userService.findByEmail(userEmail), user);
		} catch (UserEmailNotFoundException e) {
			e.printStackTrace();
		}
	}
}
