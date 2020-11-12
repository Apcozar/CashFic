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

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserIncorrectLoginException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
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
public class ITUserService {

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
	 * Sign up and find by login name test.
	 *
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 * @throws UserNotFoundException            the user not found exception
	 */
	@Test
	public void signUpAndFindByLoginNameTest() throws UserLoginExistsException, UserEmailExistsException,
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
	public void signUpAndFindByIdTest() throws UserLoginExistsException, UserEmailExistsException,
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
	public void signUpAndFindByEmailTest() throws UserLoginExistsException, UserEmailExistsException,
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
	public void signUpAndLoginTest()
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
	public void signUpExistentLoginTest()
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
	public void signUpExistentEmailTest()
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
	public void signUpExistentLoginAndEmailTest()
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
	public void FindByNonExistentIdTest() throws UserNotFoundException {
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
	public void FindByNonExistentLoginTest() throws UserNotFoundException {
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
	public void FindByNonExistentEMAILTest() throws UserEmailNotFoundException {
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
	public void signUpAndLoginNonExistentLoginTest() throws UserNotFoundException, UserIncorrectLoginException,
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
	public void signUpAndLoginIncorrectPasswordTest() throws UserLoginExistsException, UserEmailExistsException,
			UserLoginAndEmailExistsException, UserNotFoundException, UserIncorrectLoginException {
		DefaultUserEntity expected = createUser(LOGIN, EMAIL);

		userService.signUp(expected);

		Assertions.assertThrows(UserIncorrectLoginException.class, () -> {
			userService.login(LOGIN, INCORRECT_PASSWORD);
		});
	}

	/**
	 * <<<<<<< HEAD Check service updates user entity with sale advertisement added
	 * to likes list persist it and adds in sale advertisement entity the user in
	 * likes list Check both sides of relationship
	 */
	@Test
	public void userAddLikeSaleAdvertisementsTest()
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
	 * Check service updates user entity with sale advertisement added to likes list
	 * and removed from, check if remove the relationship from both sides
	 */
	@Test
	public void userRemoveLikeSaleAdvertisementsTest()
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
	 * Create two users and three sale advertisements. First user likes all sale
	 * advertisements second user only like second sale advertisement Check that can
	 * store several sale advertisement liked by user and a sale advertisement can
	 * store several users who liked it
	 */
	@Test
	public void usersLikeSeveralSaleAdvertisements()
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

		SaleAdvertisementEntity storedFirstSaleAdvertisement = saleAdvertisementService.add(firstSaleAdvertisement);

		// Create and store sale advertisement
		DefaultSaleAdvertisementEntity secondSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		secondSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		secondSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		secondSaleAdvertisement.setProductTitle("first saleAdv title test");
		secondSaleAdvertisement.setUser(firstUser);

		SaleAdvertisementEntity storedSecondSaleAdvertisement = saleAdvertisementService.add(secondSaleAdvertisement);

		// Create and store sale advertisement
		DefaultSaleAdvertisementEntity thirthSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		thirthSaleAdvertisement.setDate(LocalDateTime.of(2020, 3, 2, 20, 50));
		thirthSaleAdvertisement.setProductDescription("first sale advertisement product description test");
		thirthSaleAdvertisement.setProductTitle("first saleAdv title test");
		thirthSaleAdvertisement.setUser(firstUser);

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
	public void followUserUserNotFoundTest() throws UserNotFoundException, UserToFollowExistsException,
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
	public void followUserUserToFollowNotFoundTest() throws UserNotFoundException, UserToFollowExistsException,
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
	public void followUserUserToFollowExistsTest() throws UserNotFoundException, UserToFollowExistsException,
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
	public void followUserTest() throws UserNotFoundException, UserToFollowExistsException, UserLoginExistsException,
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
		Assert.assertEquals(user.getFollowed().size(), 0);
		userService.followUser(user, user2);
		Assert.assertTrue(user.getFollowed().contains(user2));
		Assert.assertTrue(user2.getFollowers().contains(user));
		Assert.assertEquals(user2.getFollowers().size(), 1);

		// Check if one user follows many users in both directions
		userService.followUser(user, user3);
		userService.followUser(user, user4);
		Assert.assertEquals(user.getFollowed().size(), 3);
		Assert.assertTrue(user.getFollowed().contains(user3));
		Assert.assertTrue(user3.getFollowers().contains(user));
		Assert.assertEquals(user3.getFollowers().size(), 1);
		Assert.assertTrue(user.getFollowed().contains(user4));
		Assert.assertTrue(user4.getFollowers().contains(user));
		Assert.assertEquals(user4.getFollowers().size(), 1);
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
	public void unfollowUserUserNotFoundTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
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
	public void unfollowUserUserToUnfollowNotFoundTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
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
	public void unfollowUserUserToFollowNotExistsTest() throws UserNotFoundException, UserToUnfollowNotFoundException,
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
	public void unfollowUserTest()
			throws UserNotFoundException, UserToUnfollowNotFoundException, UserLoginExistsException,
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
		Assert.assertEquals(user.getFollowed().size(), 0);
		Assert.assertFalse(user.getFollowed().contains(user2));
		Assert.assertFalse(user2.getFollowers().contains(user));

		// Check if one user unfollows many users in both directions
		userService.followUser(user, user2);
		userService.followUser(user, user3);
		userService.followUser(user, user4);
		userService.unfollowUser(user, user2);
		Assert.assertEquals(user.getFollowed().size(), 2);
		Assert.assertFalse(user.getFollowed().contains(user2));
		Assert.assertFalse(user2.getFollowers().contains(user));
		Assert.assertEquals(user2.getFollowers().size(), 0);
		userService.unfollowUser(user, user3);
		Assert.assertEquals(user.getFollowed().size(), 1);
		Assert.assertFalse(user.getFollowed().contains(user3));
		Assert.assertFalse(user3.getFollowers().contains(user));
		Assert.assertEquals(user3.getFollowers().size(), 0);
		userService.unfollowUser(user, user4);
		Assert.assertEquals(user.getFollowed().size(), 0);
		Assert.assertFalse(user.getFollowed().contains(user3));
		Assert.assertFalse(user4.getFollowers().contains(user));
		Assert.assertEquals(user4.getFollowers().size(), 0);
	}
}
