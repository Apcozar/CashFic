/**
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

package es.udc.fi.dc.fd.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultRateUserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.RateUserRepository;
import es.udc.fi.dc.fd.repository.SaleAdvertisementRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
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
 * Default implementation of the user service.
 * 
 * @author Santiago
 *
 */
@Service
public class DefaultUserService implements UserService {

	public static final int MAX_RATING = 5;
	public static final int MIN_RATING = 1;
	private static final String NULL_USER_MESSAGE = "Received a null pointer as user";

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final UserRepository userDao;

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final SaleAdvertisementRepository saleAdvertisementRepository;

	/**
	 * Repository for the domain entities handled by the service.
	 */
	private final RateUserRepository rateUserDao;

	/**
	 * Constructs an user service with the specified repository.
	 *
	 * @param repository                  the repository for the user instances
	 * @param saleAdvertisementRepository the repository for sale advertisements
	 * @param rateUserRepository          the rate user repository
	 */
	@Autowired
	public DefaultUserService(final UserRepository repository,
			final SaleAdvertisementRepository saleAdvertisementRepository,
			final RateUserRepository rateUserRepository) {
		super();
		userDao = checkNotNull(repository, "Received a null pointer as repository");
		this.saleAdvertisementRepository = checkNotNull(saleAdvertisementRepository,
				"Received a null pointer as saleAdvertisementRepository");
		rateUserDao = checkNotNull(rateUserRepository, "Received a null pointer as repository");
	}

	@Override
	public void signUp(DefaultUserEntity user)
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException {
		checkNotNull(user, NULL_USER_MESSAGE);
		if ((userDao.existsByLogin(user.getLogin())) && (userDao.existsByEmail(user.getEmail()))) {
			throw new UserLoginAndEmailExistsException(user.getLogin(), user.getEmail());
		}
		if (userDao.existsByLogin(user.getLogin())) {
			throw new UserLoginExistsException(user.getLogin());
		}
		if (userDao.existsByEmail(user.getEmail())) {
			throw new UserEmailExistsException(user.getEmail());
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.ROLE_USER);
		userDao.save(user);
	}

	@Override
	@Transactional(readOnly = true)
	public DefaultUserEntity login(String login, String password)
			throws UserNotFoundException, UserIncorrectLoginException {

		if (!userDao.existsByLogin(login)) {
			throw new UserNotFoundException(login);
		}
		DefaultUserEntity user = userDao.findByLogin(login);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UserIncorrectLoginException(login, password);
		}
		return user;
	}

	@Override
	public DefaultUserEntity findById(Integer identifier) throws UserNotFoundException {
		Optional<DefaultUserEntity> user = userDao.findById(identifier);

		if (!user.isPresent()) {
			throw new UserNotFoundException(identifier);
		}
		return user.get();
	}

	@Override
	public DefaultUserEntity findByLogin(String login) throws UserNotFoundException {
		if (!userDao.existsByLogin(login)) {
			throw new UserNotFoundException(login);
		}
		return userDao.findByLogin(login);
	}

	@Override
	public DefaultUserEntity findByEmail(String email) throws UserEmailNotFoundException {
		if (!userDao.existsByEmail(email)) {
			throw new UserEmailNotFoundException(email);
		}
		return userDao.findByEmail(email);
	}

	@Override
	public UserEntity like(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException {
		checkNotNull(user, NULL_USER_MESSAGE);
		checkNotNull(saleAdvertisement, "Received a null pointer as saleAdvertisement");
		checkUser(user.getId());
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementNotFoundException(saleAdvertisement.getId());
		}
		saleAdvertisement.addUsersLike((DefaultUserEntity) user);
		saleAdvertisementRepository.save((DefaultSaleAdvertisementEntity) saleAdvertisement);
		user.addLike(saleAdvertisement);
		return userDao.save((DefaultUserEntity) user);
	}

	@Override
	public UserEntity unlike(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException {
		checkNotNull(user, NULL_USER_MESSAGE);
		checkNotNull(saleAdvertisement, "Received a null pointer as saleAdvertisement");
		checkUser(user.getId());
		if (!saleAdvertisementRepository.existsById(saleAdvertisement.getId())) {
			throw new SaleAdvertisementNotFoundException(saleAdvertisement.getId());
		}
		saleAdvertisement.removeUsersLike((DefaultUserEntity) user);
		saleAdvertisementRepository.save((DefaultSaleAdvertisementEntity) saleAdvertisement);
		user.removeLike(saleAdvertisement);
		return userDao.save((DefaultUserEntity) user);
	}

	public UserEntity followUser(UserEntity user, UserEntity userToFollow)
			throws UserNotFoundException, UserToFollowExistsException {
		checkUser(user.getId());
		checkUser(userToFollow.getId());
		if (user.getFollowed().contains(userToFollow)) {
			throw new UserToFollowExistsException(userToFollow);
		}
		user.addFollowUser((DefaultUserEntity) userToFollow);
		userToFollow.addFollowserUser((DefaultUserEntity) user);
		userDao.save((DefaultUserEntity) userToFollow);
		return userDao.save((DefaultUserEntity) user);
	}

	@Override
	public UserEntity unfollowUser(UserEntity user, UserEntity userToUnfollow)
			throws UserNotFoundException, UserToUnfollowNotFoundException {
		checkUser(user.getId());
		checkUser(userToUnfollow.getId());
		if (!user.getFollowed().contains(userToUnfollow)) {
			throw new UserToUnfollowNotFoundException(userToUnfollow);
		}
		user.removeFollowUser((DefaultUserEntity) userToUnfollow);
		userToUnfollow.removeFollowserUser((DefaultUserEntity) user);
		userDao.save((DefaultUserEntity) userToUnfollow);
		return userDao.save((DefaultUserEntity) user);
	}

	@Override
	public int getMaxRating() {
		return MAX_RATING;
	}

	@Override
	public int getMinRating() {
		return MIN_RATING;
	}

	@Override
	public boolean existsRatingFromUserToRateUser(UserEntity user, UserEntity ratedUser) throws UserNotFoundException {
		checkUser(user.getId());
		checkUser(ratedUser.getId());
		return rateUserDao.existsRatingFromUserToRatedUser(user.getId(), ratedUser.getId());
	}

	@Override
	public boolean existsRatingForUser(UserEntity user) throws UserNotFoundException {
		checkUser(user.getId());
		return rateUserDao.existsRatedUser(user.getId());
	}

	@Override
	public void rateUser(UserEntity user, UserEntity userToRate, Integer rating)
			throws UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException {
		checkUser(user.getId());
		checkUser(userToRate.getId());
		if (rateUserDao.existsRatingFromUserToRatedUser(user.getId(), userToRate.getId())) {
			throw new UserAlreadyGiveRatingToUserToRate(user.getId(), userToRate.getId());
		}
		if (rating < getMinRating()) {
			throw new LowRatingException(rating, getMinRating());
		}
		if (rating > getMaxRating()) {
			throw new HighRatingException(rating, getMaxRating());
		}
		DefaultRateUserEntity rateEntity = new DefaultRateUserEntity((DefaultUserEntity) user,
				(DefaultUserEntity) userToRate, rating);
		rateUserDao.save(rateEntity);
	}

	@Override
	public Double averageRating(UserEntity user) throws UserNotFoundException, UserNoRatingException {
		checkUser(user.getId());
		if (!existsRatingForUser(user)) {
			throw new UserNoRatingException(user.getId());
		}
		return rateUserDao.findAverageRating(user.getId());
	}

	@Override
	public int givenRatingFromUserToRatedUser(UserEntity user, UserEntity ratedUser) throws UserNotFoundException {
		checkUser(user.getId());
		checkUser(ratedUser.getId());
		return rateUserDao.givenRatingFromUserToRatedUser(user.getId(), ratedUser.getId());
	}

	public UserEntity premiumUser(UserEntity user) throws UserNotFoundException {
		checkUser(user.getId());
		if (user.getRole() != Role.ROLE_PREMIUM) {
			user.setRole(Role.ROLE_PREMIUM);
			return userDao.save((DefaultUserEntity) user);
		} else {
			user.setRole(Role.ROLE_USER);
			return userDao.save((DefaultUserEntity) user);
		}
	}

	private void checkUser(Integer identifier) throws UserNotFoundException {
		if (!userDao.existsById(identifier)) {
			throw new UserNotFoundException(identifier);
		}
	}

}
