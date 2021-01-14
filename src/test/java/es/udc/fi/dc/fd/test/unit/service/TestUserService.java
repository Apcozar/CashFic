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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.persistence.DefaultRateUserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.RateUserRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.DefaultUserService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.HighRatingException;
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

	@Test
	void findByIdUserNotFoundThrowException() {
		int identifier = 1;
		Optional<DefaultUserEntity> userOptional = Optional.empty();
		Mockito.when(userRepository.findById(identifier)).thenReturn(userOptional);
		assertThrows(UserNotFoundException.class, () -> {
			userService.findById(identifier);
		});
	}

	@Test
	void findByIdUserFound() {
		int identifier = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(identifier);
		Optional<DefaultUserEntity> userOptional = Optional.of(user);
		Mockito.when(userRepository.findById(identifier)).thenReturn(userOptional);
		try {
			assertEquals(userService.findById(identifier), user);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void likeNullUserThrowException() {
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);

		assertThrows(NullPointerException.class, () -> {
			userService.like(null, saleAdvertisement);
		});
	}

	@Test
	void likeNullSaleAdvertisementThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);

		assertThrows(NullPointerException.class, () -> {
			userService.like(user, null);
		});
	}

	@Test
	void likeUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.like(user, saleAdvertisement);
		});
	}

	@Test
	void likeSaleAdvertisementNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(false);

		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			userService.like(user, saleAdvertisement);
		});
	}

	@Test
	void likeUserSaleAdvertisement() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultUserEntity userNoLike = new DefaultUserEntity();
		userNoLike.setId(userId);
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);
		user.addLike(saleAdvertisement);
		saleAdvertisement.addUsersLike(user);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(saleAdvertisementRepository.save(saleAdvertisement)).thenReturn(saleAdvertisement);

		try {
			assertEquals(userService.like(userNoLike, saleAdvertisement), user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void unlikeNullUserThrowException() {
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);

		assertThrows(NullPointerException.class, () -> {
			userService.unlike(null, saleAdvertisement);
		});
	}

	@Test
	void unlikeNullSaleAdvertisementThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);

		assertThrows(NullPointerException.class, () -> {
			userService.unlike(user, null);
		});
	}

	@Test
	void unlikeUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.unlike(user, saleAdvertisement);
		});
	}

	@Test
	void unlikeSaleAdvertisementNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(false);

		assertThrows(SaleAdvertisementNotFoundException.class, () -> {
			userService.unlike(user, saleAdvertisement);
		});
	}

	@Test
	void unlikeUserSaleAdvertisement() {
		int userId = 1;
		DefaultUserEntity userWithLike = new DefaultUserEntity();
		userWithLike.setId(userId);
		int saleAdvertisementId = 1;
		DefaultSaleAdvertisementEntity saleAdvertisement = new DefaultSaleAdvertisementEntity();
		saleAdvertisement.setId(saleAdvertisementId);
		DefaultUserEntity userNoLike = new DefaultUserEntity();
		userNoLike.setId(userId);

		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(saleAdvertisementRepository.existsById(saleAdvertisementId)).thenReturn(true);
		userWithLike.addLike(saleAdvertisement);
		saleAdvertisement.addUsersLike(userWithLike);
		Mockito.when(userRepository.save(userNoLike)).thenReturn(userNoLike);
		Mockito.when(saleAdvertisementRepository.save(saleAdvertisement)).thenReturn(saleAdvertisement);

		try {
			assertEquals(userService.unlike(userWithLike, saleAdvertisement), userNoLike);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void followUserNotFoundThrowException() {
		int userId = 1;
		int userToFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToFollow = new DefaultUserEntity();
		user.setId(userId);
		userToFollow.setId(userToFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	@Test
	void followUserToFollowNotFoundThrowException() {
		int userId = 1;
		int userToFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToFollow = new DefaultUserEntity();
		user.setId(userId);
		userToFollow.setId(userToFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToFollowId)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	@Test
	void followUserAlreadyFollowsThrowException() {
		int userId = 1;
		int userToFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToFollow = new DefaultUserEntity();
		user.setId(userId);
		userToFollow.setId(userToFollowId);
		user.addFollowUser(userToFollow);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToFollowId)).thenReturn(true);

		assertThrows(UserToFollowExistsException.class, () -> {
			userService.followUser(user, userToFollow);
		});
	}

	@Test
	void followUser() {
		int userId = 1;
		int userToFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToFollow = new DefaultUserEntity();
		user.setId(userId);
		userToFollow.setId(userToFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToFollowId)).thenReturn(true);
		DefaultUserEntity userHaveFollow = new DefaultUserEntity();
		userHaveFollow.setId(userId);
		userHaveFollow.addFollowserUser(userToFollow);

		Mockito.when(userRepository.save(userToFollow)).thenReturn(userToFollow);
		Mockito.when(userRepository.save(user)).thenReturn(userHaveFollow);
		try {
			assertEquals(userService.followUser(user, userToFollow), userHaveFollow);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void unfollowUserUserNotFoundThrowException() {
		int userId = 1;
		int userToUnFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToUnFollow = new DefaultUserEntity();
		user.setId(userId);
		userToUnFollow.setId(userToUnFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		Mockito.when(userRepository.existsById(userToUnFollowId)).thenReturn(true);

		assertThrows(UserNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnFollow);
		});
	}

	@Test
	void unfollowUserUserToUnfollowNotFoundThrowException() {
		int userId = 1;
		int userToUnFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToUnFollow = new DefaultUserEntity();
		user.setId(userId);
		userToUnFollow.setId(userToUnFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToUnFollowId)).thenReturn(false);

		assertThrows(UserNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnFollow);
		});
	}

	@Test
	void unfollowUserUserNotFollowUserToUnfollowThrowException() {
		int userId = 1;
		int userToUnFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToUnFollow = new DefaultUserEntity();
		user.setId(userId);
		userToUnFollow.setId(userToUnFollowId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToUnFollowId)).thenReturn(true);

		assertThrows(UserToUnfollowNotFoundException.class, () -> {
			userService.unfollowUser(user, userToUnFollow);
		});
	}

	@Test
	void unfollowUser() {
		int userId = 1;
		int userToUnFollowId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToUnFollow = new DefaultUserEntity();
		user.setId(userId);
		userToUnFollow.setId(userToUnFollowId);
		user.addFollowUser(userToUnFollow);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToUnFollowId)).thenReturn(true);
		Mockito.when(userRepository.save(userToUnFollow)).thenReturn(userToUnFollow);
		Mockito.when(userRepository.save(user)).thenReturn(user);
		try {
			assertEquals(userService.unfollowUser(user, userToUnFollow), user);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void minRating() {
		assertEquals(1, userService.getMinRating());
	}

	@Test
	void maxRating() {
		assertEquals(5, userService.getMaxRating());
	}

	@Test
	void existsRatingFromUserToRateUserUserNotFoundThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingFromUserToRateUser(user, userToRate);
		});
	}

	@Test
	void existsRatingFromUserToRateUserRateUserNotFoundThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingFromUserToRateUser(user, userToRate);
		});
	}

	@Test
	void existsRatingFromUserToRateUserReturnBoolean() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatingFromUserToRatedUser(userId, userToRateId)).thenReturn(false);
		try {
			assertEquals(false, userService.existsRatingFromUserToRateUser(user, userToRate));
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void existsRatingForUserUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.existsRatingForUser(user);
		});

	}

	@Test
	void rateUserUserNotFoundThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		assertThrows(UserNotFoundException.class, () -> {
			userService.rateUser(user, userToRate, userService.getMinRating());
		});
	}

	@Test
	void rateUserUserToRateNotFoundThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.rateUser(user, userToRate, userService.getMinRating());
		});
	}

	@Test
	void rateUserHaveRateThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatingFromUserToRatedUser(userId, userToRateId)).thenReturn(true);
		assertThrows(UserAlreadyGiveRatingToUserToRate.class, () -> {
			userService.rateUser(user, userToRate, userService.getMinRating());
		});
	}

	@Test
	void rateUserRateUnderMinRatingThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatingFromUserToRatedUser(userId, userToRateId)).thenReturn(false);
		assertThrows(LowRatingException.class, () -> {
			userService.rateUser(user, userToRate, userService.getMinRating() - 1);
		});
	}

	@Test
	void rateUserRateGreaterThanMaxRatingThrowException() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatingFromUserToRatedUser(userId, userToRateId)).thenReturn(false);
		assertThrows(HighRatingException.class, () -> {
			userService.rateUser(user, userToRate, userService.getMaxRating() + 1);
		});
	}

	@Test
	void rateUser() {
		int userId = 1;
		int userToRateId = 2;
		DefaultUserEntity user = new DefaultUserEntity();
		DefaultUserEntity userToRate = new DefaultUserEntity();
		user.setId(userId);
		userToRate.setId(userToRateId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(userToRateId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatingFromUserToRatedUser(userId, userToRateId)).thenReturn(false);
		DefaultRateUserEntity rate = new DefaultRateUserEntity(user, userToRate, userService.getMaxRating());
		Mockito.when(rateUserRepository.save(rate)).thenReturn(rate);

		try {
			userService.rateUser(user, userToRate, userService.getMaxRating());
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void averageRatingUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.averageRating(user);
		});
	}

	@Test
	void averageRatingUserWithoutRateThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatedUser(userId)).thenReturn(false);
		assertThrows(UserNoRatingException.class, () -> {
			userService.averageRating(user);
		});
	}

	@Test
	void averageRating() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(rateUserRepository.existsRatedUser(userId)).thenReturn(true);
		Mockito.when(rateUserRepository.findAverageRating(userId)).thenReturn(Double.valueOf(5));
		try {
			assertEquals(userService.averageRating(user), Double.valueOf(5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	void givenRatingFromUserToRatedUserUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int ratedUserId = 2;
		DefaultUserEntity ratedUser = new DefaultUserEntity();
		ratedUser.setId(ratedUserId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.givenRatingFromUserToRatedUser(user, ratedUser);
		});
	}

	@Test
	void givenRatingFromUserToRatedUserRatedUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int ratedUserId = 2;
		DefaultUserEntity ratedUser = new DefaultUserEntity();
		ratedUser.setId(ratedUserId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(ratedUserId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.givenRatingFromUserToRatedUser(user, ratedUser);
		});
	}

	@Test
	void givenRatingFromUserToRatedUser() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		int ratedUserId = 2;
		DefaultUserEntity ratedUser = new DefaultUserEntity();
		ratedUser.setId(ratedUserId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.existsById(ratedUserId)).thenReturn(true);
		Mockito.when(rateUserRepository.givenRatingFromUserToRatedUser(userId, ratedUserId))
				.thenReturn(Integer.valueOf(4));
		try {
			assertEquals(userService.givenRatingFromUserToRatedUser(user, ratedUser), Integer.valueOf(4));
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void premiumUserUserNotFoundThrowException() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		Mockito.when(userRepository.existsById(userId)).thenReturn(false);
		assertThrows(UserNotFoundException.class, () -> {
			userService.premiumUser(user);
		});
	}

	@Test
	void premiumUserNoPremiumToPremium() {
		int userId = 1;
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		user.setRole(Role.ROLE_USER);
		DefaultUserEntity premiumUser = new DefaultUserEntity();
		premiumUser.setId(userId);
		premiumUser.setRole(Role.ROLE_PREMIUM);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.save(user)).thenReturn(premiumUser);
		try {
			assertEquals(userService.premiumUser(user), premiumUser);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void premiumUserPremiumToNoPremium() {
		int userId = 1;
		DefaultUserEntity premiumUser = new DefaultUserEntity();
		premiumUser.setId(userId);
		premiumUser.setRole(Role.ROLE_PREMIUM);
		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(userId);
		user.setRole(Role.ROLE_USER);
		Mockito.when(userRepository.existsById(userId)).thenReturn(true);
		Mockito.when(userRepository.save(premiumUser)).thenReturn(user);
		try {
			assertEquals(userService.premiumUser(premiumUser), user);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
}
