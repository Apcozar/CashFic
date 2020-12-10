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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.HighRatingException;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.AlreadyPremiumUserException;
import es.udc.fi.dc.fd.service.user.exceptions.LowRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserAlreadyGiveRatingToUserToRate;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserIncorrectLoginException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNoRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserToFollowExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserToUnfollowNotFoundException;

/**
 * Integration tests for the {@link UserService}.
 * 
 */
@WebAppConfiguration
@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITUserService {

	/** The non existent id. */
	private final Integer NON_EXISTENT_ID = -1;

	/** The non existent email. */
	private final String NON_EXISTENT_EMAIL = "-1";

	/** The non existent user name. */
	private final String NON_EXISTENT_LOGIN = "-1";

	/** The login. */
	private final String LOGIN = "login";

	/** The login2. */
	private final String LOGIN2 = "login2";

	/** The email. */
	private final String EMAIL = "user@udc.es";

	/** The email2. */
	private final String EMAIL2 = "user2@udc.es";

	/** The password. */
	private final String PASSWORD = "password";

	/** The incorrect password. */
	private final String INCORRECT_PASSWORD = "password2";

	/**
	 * Service being tested.
	 */
	@Autowired
	private UserService userService;

	@Autowired
	private SaleAdvertisementService saleAdvertisementService;

	/**
	 * Default constructor.
	 */
	public ITUserService() {
		super();
	}

	/**
	 * Creates the user.
	 *
	 * @param userName the user name
	 * @param email    the email
	 * @return the default user entity
	 */
	private DefaultUserEntity createUser(String login, String email) {
		return new DefaultUserEntity(login, PASSWORD, "userName", "userLastName", email, "city");
	}

	/**
	 * Creates the sale advertisement.
	 *
	 * @param user the user
	 * @return the default sale advertisement entity
	 */
	private DefaultSaleAdvertisementEntity createSaleAdvertisement(DefaultUserEntity user) {
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		saleAdvertisement.setProductDescription("sale advertisement product description test");
		saleAdvertisement.setProductTitle("sale advertisement title test");
		saleAdvertisement.setUser(user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(10));
		return saleAdvertisement;
	}

	/**
	 * Sign up and find by login name test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 */
	@Test
	void signUpAndFindByLoginNameTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		DefaultUserEntity actual = userService.findByLogin(LOGIN);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Sign up and find by id test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 */
	@Test
	void signUpAndFindByIdTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		DefaultUserEntity actual = userService.findById(expected.getId());

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Sign up and find by email test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserEmailNotFoundException       the email not found exception
	 */
	@Test
	void signUpAndFindByEmailTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException, UserEmailNotFoundException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		DefaultUserEntity actual = userService.findByEmail(EMAIL);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Sign up and login test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserEmailNotFoundException       the email not found exception
	 * @throws UserIncorrectLoginException      the incorrect login exception
	 */
	@Test
	void signUpAndLoginTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, UserEmailNotFoundException, UserIncorrectLoginException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		DefaultUserEntity actual = userService.login(LOGIN, PASSWORD);

		Assert.assertEquals(expected, actual);
	}

	/**
	 * Sign up existent login test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void signUpExistentLoginTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		Assertions.assertThrows(UserLoginExistsException.class, () -> {
			userService.signUp(createUser(LOGIN, EMAIL2));
		});
	}

	/**
	 * Sign up existent email test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void signUpExistentEmailTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		Assertions.assertThrows(UserEmailExistsException.class, () -> {
			userService.signUp(createUser(LOGIN2, EMAIL));
		});
	}

	/**
	 * Sign up existent login and email test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void signUpExistentLoginAndEmailTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		Assertions.assertThrows(UserLoginAndEmailExistsException.class, () -> {
			userService.signUp(createUser(LOGIN, EMAIL));
		});
	}

	/**
	 * Find by non existent id test.
	 *
	 * @throws UserNotFoundException the user not found exception
	 */
	@Test
	void FindByNonExistentIdTest() throws UserNotFoundException {
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.findById(NON_EXISTENT_ID);
		});
	}

	/**
	 * Find by non existent login test.
	 *
	 * @throws UserNotFoundException the user not found exception
	 */
	@Test
	void FindByNonExistentLoginTest() throws UserNotFoundException {
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.findByLogin(NON_EXISTENT_LOGIN);
		});
	}

	/**
	 * Find by non existent EMAIL test.
	 *
	 * @throws UserEmailNotFoundException the email not found exception
	 */
	@Test
	void FindByNonExistentEMAILTest() throws UserEmailNotFoundException {
		Assertions.assertThrows(UserEmailNotFoundException.class, () -> {
			userService.findByEmail(NON_EXISTENT_EMAIL);
		});
	}

	/**
	 * Sign up and login non existent login test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserIncorrectLoginException      the incorrect login exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void signUpAndLoginNonExistentLoginTest() throws UserNotFoundException, UserIncorrectLoginException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.login(NON_EXISTENT_LOGIN, PASSWORD);
		});
	}

	/**
	 * Sign up and login incorrect password test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserIncorrectLoginException      the incorrect login exception
	 */
	@Test
	void signUpAndLoginIncorrectPasswordTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException, UserIncorrectLoginException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		Assertions.assertThrows(UserIncorrectLoginException.class, () -> {
			userService.login(LOGIN, INCORRECT_PASSWORD);
		});
	}

	/**
	 * Check service updates user entity with sale advertisement added to likes list
	 * persist it and adds in sale advertisement entity the user in likes list Check
	 * both sides of relationship
	 */
	@Test
	void userAddLikeSaleAdvertisementsTest()
			throws UserNotFoundException, SaleAdvertisementAlreadyExistsException, SaleAdvertisementNotFoundException {
		// Get user
		DefaultUserEntity user = userService.findById(1);
		// User no have likes
		Assert.assertTrue(user.getLikes().isEmpty());

		// Now create and store sale advertisement
		DefaultSaleAdvertisementEntity firstSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		firstSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		firstSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		firstSaleAdvertisement.setProductTitle("first saleAdv title test");
		firstSaleAdvertisement.setUser(user);
		firstSaleAdvertisement.setPrice(BigDecimal.valueOf(10));

		SaleAdvertisementEntity storedFirstSaleAdvertisement = saleAdvertisementService.add(firstSaleAdvertisement);

		// user like sale advertisement
		UserEntity updatedUser = userService.like(user, storedFirstSaleAdvertisement);
		// Check if user have sale advertisement in likes
		Assert.assertEquals(Integer.valueOf(updatedUser.getLikes().size()), Integer.valueOf(1));
		SaleAdvertisementEntity updatedSaleAdvertisement = saleAdvertisementService
				.findById(storedFirstSaleAdvertisement.getId());
		Set<DefaultSaleAdvertisementEntity> userLikes = updatedUser.getLikes();
		Assert.assertEquals(userLikes.iterator().next(), updatedSaleAdvertisement);
		// Check if sale advertisement have user in likes
		Assert.assertEquals(updatedSaleAdvertisement.getLikes().iterator().next(), updatedUser);
	}

	/**
	 * Not existent user add like sale advertisements test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void notExistentUserAddLikeSaleAdvertisementsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		// Create users
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);

		// Sign up first user
		userService.signUp(user);

		// Assign non existent id to second user
		user2.setId(NON_EXISTENT_ID);

		// Create sale advertisement
		DefaultSaleAdvertisementEntity saleAdvertisement = createSaleAdvertisement(user);

		// Non existent user likes the sale advertisement
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.like(user2, saleAdvertisement));
	}

	/**
	 * User add like not existent sale advertisements test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void userAddLikeNotExistentSaleAdvertisementsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		// Create users
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		// Sign up user
		userService.signUp(user);

		// Create sale advertisement
		DefaultSaleAdvertisementEntity saleAdvertisement = createSaleAdvertisement(user);

		// User likes the non stored sale advertisement
		Assertions.assertThrows(SaleAdvertisementNotFoundException.class,
				() -> userService.like(user, saleAdvertisement));
	}

	/**
	 * Check service updates user entity with sale advertisement added to likes list
	 * and removed from, check if remove the relationship from both sides
	 */
	@Test
	void userRemoveLikeSaleAdvertisementsTest()
			throws UserNotFoundException, SaleAdvertisementAlreadyExistsException, SaleAdvertisementNotFoundException {
		// Get user
		DefaultUserEntity user = userService.findById(1);
		// User no have likes
		Assert.assertTrue(user.getLikes().isEmpty());

		// Create and store sale advertisement
		DefaultSaleAdvertisementEntity firstSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		firstSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		firstSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		firstSaleAdvertisement.setProductTitle("first saleAdv title test");
		firstSaleAdvertisement.setUser(user);
		firstSaleAdvertisement.setPrice(BigDecimal.valueOf(10));

		SaleAdvertisementEntity storedFirstSaleAdvertisement = saleAdvertisementService.add(firstSaleAdvertisement);
		// User like sale advertisement
		UserEntity updatedUser = userService.like(user, storedFirstSaleAdvertisement);
		// Here is same as userAddLikeSaleAdvertisementsTest so avoid checks

		SaleAdvertisementEntity updatedSaleAdvertisement = saleAdvertisementService
				.findById(storedFirstSaleAdvertisement.getId());

		UserEntity updateAfterRemoveUser = userService.unlike(updatedUser, updatedSaleAdvertisement);

		// Check if user have 0 likes
		Assert.assertTrue(updateAfterRemoveUser.getLikes().isEmpty());

		// get sale advertisement with like removed Check if have 0 likes
		SaleAdvertisementEntity updatedAfterRemoveLikeSaleAdvertisement = saleAdvertisementService
				.findById(storedFirstSaleAdvertisement.getId());
		Assert.assertTrue(updatedAfterRemoveLikeSaleAdvertisement.getLikes().isEmpty());
	}

	/**
	 * Not existent user remove like sale advertisements test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void notExistentUserRemoveLikeSaleAdvertisementsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		// Create users
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);

		// Sign up first user
		userService.signUp(user);

		// Assign non existent id to second user
		user2.setId(NON_EXISTENT_ID);

		// Create sale advertisement
		DefaultSaleAdvertisementEntity saleAdvertisement = createSaleAdvertisement(user);

		// Non existent user likes the sale advertisement
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.unlike(user2, saleAdvertisement));
	}

	@Test
	void userRemoveLikeNotExistentSaleAdvertisementsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		// Create users
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		// Sign up user
		userService.signUp(user);

		// Create sale advertisement
		DefaultSaleAdvertisementEntity saleAdvertisement = createSaleAdvertisement(user);

		// User likes the non stored sale advertisement
		Assertions.assertThrows(SaleAdvertisementNotFoundException.class,
				() -> userService.unlike(user, saleAdvertisement));
	}

	/**
	 * Create two users and three sale advertisements. First user likes all sale
	 * advertisements second user only like second sale advertisement Check that can
	 * store several sale advertisement liked by user and a sale advertisement can
	 * store several users who liked it
	 */
	@Test
	void usersLikeSeveralSaleAdvertisements()
			throws UserNotFoundException, SaleAdvertisementAlreadyExistsException, SaleAdvertisementNotFoundException {
		// Get users
		DefaultUserEntity firstUser = userService.findById(1);
		DefaultUserEntity secondUser = userService.findById(2);

		// Create and store three sale advertisement
		DefaultSaleAdvertisementEntity firstSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		firstSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		firstSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		firstSaleAdvertisement.setProductTitle("first saleAdv title test");
		firstSaleAdvertisement.setUser(firstUser);
		firstSaleAdvertisement.setPrice(BigDecimal.valueOf(10));

		SaleAdvertisementEntity storedFirstSaleAdvertisement = saleAdvertisementService.add(firstSaleAdvertisement);

		// Create and store sale advertisement
		DefaultSaleAdvertisementEntity secondSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		secondSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		secondSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		secondSaleAdvertisement.setProductTitle("first saleAdv title test");
		secondSaleAdvertisement.setUser(firstUser);
		secondSaleAdvertisement.setPrice(BigDecimal.valueOf(10));

		SaleAdvertisementEntity storedSecondSaleAdvertisement = saleAdvertisementService.add(secondSaleAdvertisement);

		// Create and store sale advertisement
		DefaultSaleAdvertisementEntity thirthSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		thirthSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		thirthSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		thirthSaleAdvertisement.setProductTitle("first saleAdv title test");
		thirthSaleAdvertisement.setUser(firstUser);
		thirthSaleAdvertisement.setPrice(BigDecimal.valueOf(10));

		SaleAdvertisementEntity storedThirthSaleAdvertisement = saleAdvertisementService.add(thirthSaleAdvertisement);

		// First user likes all sale advertisements
		UserEntity firstUpdatedUser = userService.like(firstUser, storedFirstSaleAdvertisement);
		firstUpdatedUser = userService.like(firstUpdatedUser, storedSecondSaleAdvertisement);
		firstUpdatedUser = userService.like(firstUpdatedUser, storedThirthSaleAdvertisement);
		// Second user likes second sale advertisement
		UserEntity secondUpdatedUser = userService.like(secondUser, storedSecondSaleAdvertisement);

		// check that first user have 3 liked sale advertisements
		Assert.assertEquals(Integer.valueOf(firstUpdatedUser.getLikes().size()), Integer.valueOf(3));
		// Check that sale advertisements are same as liked
		Assert.assertTrue(firstUpdatedUser.getLikes()
				.contains(saleAdvertisementService.findById(storedFirstSaleAdvertisement.getId())));
		Assert.assertTrue(firstUpdatedUser.getLikes()
				.contains(saleAdvertisementService.findById(storedSecondSaleAdvertisement.getId())));
		Assert.assertTrue(firstUpdatedUser.getLikes()
				.contains(saleAdvertisementService.findById(storedThirthSaleAdvertisement.getId())));

		// check that second sale advertisement have 2 users who liked it
		Assert.assertTrue(saleAdvertisementService.findById(storedSecondSaleAdvertisement.getId()).getLikes()
				.contains(secondUpdatedUser));
		Assert.assertTrue(saleAdvertisementService.findById(storedSecondSaleAdvertisement.getId()).getLikes()
				.contains(firstUpdatedUser));
	}

	/**
	 * Follow user user not found test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToFollowExistsException      the user to follow exists exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void followUserUserNotFoundTest() throws UserNotFoundException, UserToFollowExistsException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToFollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(userToFollow);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	/**
	 * Follow user user to follow not found test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToFollowExistsException      the user to follow exists exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void followUserUserToFollowNotFoundTest() throws UserNotFoundException, UserToFollowExistsException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToFollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);

		userToFollow.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	/**
	 * Follow user user to follow exists test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToFollowExistsException      the user to follow exists exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void followUserUserToFollowExistsTest() throws UserNotFoundException, UserToFollowExistsException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToFollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToFollow);

		userService.followUser(user, userToFollow);

		Assertions.assertThrows(UserToFollowExistsException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	/**
	 * Follow user test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToFollowExistsException      the user to follow exists exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void followUserTest() throws UserNotFoundException, UserToFollowExistsException, UserLoginExistsException,
			UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);
		DefaultUserEntity user3 = createUser("login3", "user3@udc.es");
		DefaultUserEntity user4 = createUser("login4", "user4@udc.es");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);

		// Check if one user follows only one user in both directions
		Assert.assertEquals(0, user.getFollowed().size());
		userService.followUser(user, user2);
		Assert.assertTrue(user.getFollowed().contains(user2));
		Assert.assertTrue(user2.getFollowers().contains(user));
		Assert.assertEquals(1, user2.getFollowers().size());

		// Check if one user follows many users in both directions
		userService.followUser(user, user3);
		userService.followUser(user, user4);
		Assert.assertEquals(3, user.getFollowed().size());
		Assert.assertTrue(user.getFollowed().contains(user3));
		Assert.assertTrue(user3.getFollowers().contains(user));
		Assert.assertEquals(1, user3.getFollowers().size());
		Assert.assertTrue(user.getFollowed().contains(user4));
		Assert.assertTrue(user4.getFollowers().contains(user));
		Assert.assertEquals(1, user4.getFollowers().size());
	}

	/**
	 * Unfollow user user not found test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToUnfollowNotFoundException  the user to unfollow not found
	 *                                          exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void unfollowUserUserNotFoundTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToUnfollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(userToUnfollow);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnfollow);
		});
	}

	/**
	 * Unfollow user user to unfollow test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToUnfollowNotFoundException  the user to unfollow not found
	 *                                          exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void unfollowUserUserToUnfollowNotFoundTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToUnfollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);

		userToUnfollow.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnfollow);
		});
	}

	/**
	 * Unfollow user user to follow not found test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToUnfollowNotFoundException  the user to unfollow not found
	 *                                          exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	@Test
	void unfollowUserUserToFollowNotExistsTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToUnfollow = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToUnfollow);

		Assertions.assertThrows(UserToUnfollowNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnfollow);
		});
	}

	/**
	 * Unfollow user test.
	 *
	 * @throws UserNotFoundException            the user not found exception
	 * @throws UserToUnfollowNotFoundException  the user to unfollow not found
	 *                                          exception
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserToFollowExistsException      the user to follow exists exception
	 */
	@Test
	void unfollowUserTest() throws UserNotFoundException, UserToUnfollowNotFoundException, UserLoginExistsException,
			UserEmailExistsException, UserLoginAndEmailExistsException, UserToFollowExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);
		DefaultUserEntity user3 = createUser("login3", "user3@udc.es");
		DefaultUserEntity user4 = createUser("login4", "user4@udc.es");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);

		// Check if one user unfollows only one user in both directions
		userService.followUser(user, user2);
		userService.unfollowUser(user, user2);
		Assert.assertEquals(0, user.getFollowed().size());
		Assert.assertFalse(user.getFollowed().contains(user2));
		Assert.assertFalse(user2.getFollowers().contains(user));

		// Check if one user unfollows many users in both directions
		userService.followUser(user, user2);
		userService.followUser(user, user3);
		userService.followUser(user, user4);
		userService.unfollowUser(user, user2);
		Assert.assertEquals(2, user.getFollowed().size());
		Assert.assertFalse(user.getFollowed().contains(user2));
		Assert.assertFalse(user2.getFollowers().contains(user));
		Assert.assertEquals(0, user2.getFollowers().size());
		userService.unfollowUser(user, user3);
		Assert.assertEquals(1, user.getFollowed().size());
		Assert.assertFalse(user.getFollowed().contains(user3));
		Assert.assertFalse(user3.getFollowers().contains(user));
		Assert.assertEquals(0, user3.getFollowers().size());
		userService.unfollowUser(user, user4);
		Assert.assertEquals(0, user.getFollowed().size());
		Assert.assertFalse(user.getFollowed().contains(user3));
		Assert.assertFalse(user4.getFollowers().contains(user));
		Assert.assertEquals(0, user4.getFollowers().size());
	}

	/**
	 * Become premium test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 * @throws AlreadyPremiumUserException      the already premium user exception
	 */
	/*
	 * @Test void becomePremiumTest() throws UserLoginExistsException,
	 * UserEmailExistsException, UserLoginAndEmailExistsException,
	 * UserNotFoundException, AlreadyPremiumUserException { // Create user
	 * DefaultUserEntity user = createUser(LOGIN, EMAIL);
	 * 
	 * // Sign up user userService.signUp(user);
	 * 
	 * // Become premium user userService.premiumUser(user);
	 * 
	 * // Check data Assert.assertEquals(Role.ROLE_PREMIUM, user.getRole()); }
	 */

	@Test
	void existsRatingFromUserToRateUserUserNotFoundTest() throws UserNotFoundException, UserLoginExistsException,
			UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity ratedUser = createUser(LOGIN2, EMAIL2);

		userService.signUp(ratedUser);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingFromUserToRateUser(user, ratedUser);
		});
	}

	@Test
	void existsRatingFromUserToRateUserRatedUserNotFoundTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity ratedUser = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);

		ratedUser.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingFromUserToRateUser(user, ratedUser);
		});
	}

	@Test
	void existsRatingFromUserToRateUserTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);
		DefaultUserEntity user3 = createUser("login3", "user3@udc.es");
		DefaultUserEntity user4 = createUser("login4", "user4@udc.es");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);

		// Check if there is rating given from user to user2
		userService.rateUser(user, user2, 3);
		Assert.assertTrue(userService.existsRatingFromUserToRateUser(user, user2));

		// Check if there is not rating given from user3 to user4
		Assert.assertFalse(userService.existsRatingFromUserToRateUser(user3, user4));
	}

	@Test
	void rateUserUserNotFoundExceptionTest()
			throws UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(userToRate);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.rateUser(user, userToRate, 3);
		});
	}

	@Test
	void rateUserUserToRateNotFoundExceptionTest()
			throws UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);

		userToRate.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.rateUser(user, userToRate, 3);
		});
	}

	@Test
	void rateUserUserAlreadyGiveRatingToUserToRateTest()
			throws UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException,
			UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);
		userService.rateUser(user, userToRate, 3);

		Assertions.assertThrows(UserAlreadyGiveRatingToUserToRate.class, () -> {
			userService.rateUser(user, userToRate, 3);
		});
	}

	@Test
	void rateUserLowRatingExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);

		Assertions.assertThrows(LowRatingException.class, () -> {
			userService.rateUser(user, userToRate, -1);
		});
	}

	@Test
	void rateUserHighRatingExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);

		Assertions.assertThrows(HighRatingException.class, () -> {
			userService.rateUser(user, userToRate, 7);
		});
	}

	@Test
	void rateUserTest() throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException,
			UserNoRatingException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);
		userService.rateUser(user, userToRate, 3);
		Assert.assertTrue(userService.existsRatingFromUserToRateUser(user, userToRate));
		Assert.assertEquals(3, userService.givenRatingFromUserToRatedUser(user, userToRate));
		Assert.assertEquals(userService.averageRating(userToRate), (Double.valueOf(3)));
		Assert.assertTrue(userService.existsRatingForUser(userToRate));
		Assert.assertFalse(userService.existsRatingForUser(user));
	}

	@Test
	void existsRatingForUserUserNotFoundExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingForUser(user);
		});
	}

	@Test
	void existsRatingForUserTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);

		userService.rateUser(user, userToRate, 3);

		Assert.assertTrue(userService.existsRatingForUser(userToRate));
	}

	@Test
	void averageRatingUserNotFoundExceptionTest() {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.averageRating(user);
		});
	}

	@Test
	void averageRatingUserNoRatingExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		userService.signUp(user);

		Assertions.assertThrows(UserNoRatingException.class, () -> {
			userService.averageRating(user);
		});
	}

	@Test
	void averageRatingTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException, UserAlreadyGiveRatingToUserToRate,
			LowRatingException, HighRatingException, UserNoRatingException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity user2 = createUser(LOGIN2, EMAIL2);
		DefaultUserEntity user3 = createUser("login3", "user3@udc.es");
		DefaultUserEntity user4 = createUser("login4", "user4@udc.es");

		userService.signUp(user);
		userService.signUp(user2);
		userService.signUp(user3);
		userService.signUp(user4);

		// Check the average with only one vote
		userService.rateUser(user, user4, 3);
		Assert.assertEquals(userService.averageRating(user4), (Double.valueOf(3)));

		// Add more ratings for user 4 and check if the average is right
		userService.rateUser(user2, user4, 2);
		userService.rateUser(user3, user4, 4);
		Assert.assertEquals(userService.averageRating(user4), (Double.valueOf(3)));
	}

	@Test
	void givenRatingFromUserToRatedUserUserNotFoundExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(userToRate);

		user.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.givenRatingFromUserToRatedUser(user, userToRate);
		});
	}

	@Test
	void givenRatingFromUserToRatedUserUserToRateNotFoundExceptionTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);

		userToRate.setId(NON_EXISTENT_ID);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.givenRatingFromUserToRatedUser(user, userToRate);
		});
	}

	@Test
	void givenRatingFromUserToRatedTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException {
		DefaultUserEntity user = createUser(LOGIN, EMAIL);
		DefaultUserEntity userToRate = createUser(LOGIN2, EMAIL2);

		userService.signUp(user);
		userService.signUp(userToRate);

		userService.rateUser(user, userToRate, 3);

		Assert.assertEquals(3, userService.givenRatingFromUserToRatedUser(user, userToRate));
	}

	/**
	 * Become premium being premium test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 * @throws AlreadyPremiumUserException      the already premium user exception
	 */

	@Test
	void becomePremiumBeingPremiumTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException { // Create user
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		// Sign up user
		userService.signUp(user);

		// Become premium user
		userService.premiumUser(user);

		// Become premium again
		Assertions.assertEquals(Role.ROLE_USER, userService.premiumUser(user).getRole());
	}

	/**
	 * Become premium not existent user test.
	 */

	@Test
	void becomePremiumNotExistentUserTest() { // Create user
		DefaultUserEntity user = createUser(LOGIN, EMAIL);

		// Assign non existent id
		user.setId(NON_EXISTENT_ID);

		// Become premium without sign up
		Assertions.assertThrows(UserNotFoundException.class, () -> userService.premiumUser(user));
	}

}
