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

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
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
 * Service for the user domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 * @author Santiago
 */
public interface UserService {

	/**
	 * Sign up a new user.
	 *
	 * @param user the new user
	 * @throws UserLoginExistsException         the user login exists exception
	 * @throws UserEmailExistsException         the user email exists exception
	 * @throws UserLoginAndEmailExistsException the user login and email exists
	 *                                          exception
	 */
	void signUp(DefaultUserEntity user)
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException;

	/**
	 * Login.
	 *
	 * @param userName the user name
	 * @param password the password
	 * @return the default user entity
	 * @throws UserNotFoundException       the user not found exception
	 * @throws UserIncorrectLoginException the incorrect login exception
	 */
	DefaultUserEntity login(String userName, String password) throws UserNotFoundException, UserIncorrectLoginException;

	/**
	 * Find by id.
	 *
	 * @param identifier the identifier
	 * @return the default user entity
	 * @throws UserNotFoundException the user not found exception
	 */
	DefaultUserEntity findById(final Integer identifier) throws UserNotFoundException;

	/**
	 * Find by login.
	 *
	 * @param login the login
	 * @return the default user entity
	 * @throws UserNotFoundException the user not found exception
	 */
	DefaultUserEntity findByLogin(String login) throws UserNotFoundException;

	/**
	 * Find by email.
	 *
	 * @param email the email
	 * @return the default user entity
	 * @throws UserEmailNotFoundException the email not found exception
	 */
	DefaultUserEntity findByEmail(String email) throws UserEmailNotFoundException;

	/**
	 * Add sale advertisement to the user likes and return updated user entity add
	 * user to the sale advertisement likes
	 *
	 * @param user              the user entity to add sale advertisement as like
	 * @param saleAdvertisement the sale advertisement to add user as like
	 * @return the updated user entity
	 * @throws UserNotFoundException              when cannot found the user
	 * @throws SaleAdvertisementNotFoundException when cannot found the sale
	 *                                            advertisement
	 */
	public UserEntity like(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException;

	/**
	 * Remove sale advertisement to the user likes and return updated user entity
	 * remove user from sale advertisement likes too
	 *
	 * @param user              the user entity to remove sale advertisement as like
	 * @param saleAdvertisement the sale advertisement to remove user as like
	 * @return the updated user entity
	 * @throws UserNotFoundException              when cannot found the user
	 * @throws SaleAdvertisementNotFoundException when cannot found the sale
	 *                                            advertisement
	 */
	public UserEntity unlike(UserEntity user, SaleAdvertisementEntity saleAdvertisement)
			throws UserNotFoundException, SaleAdvertisementNotFoundException;

	/**
	 * Follow user
	 * 
	 * @param user         the user
	 * @param userToFollow the user to follow
	 * @return the user entity
	 * @throws UserNotFoundException       the user not found exception
	 * @throws UserToFollowExistsException the user to follow exists exception
	 */
	UserEntity followUser(UserEntity user, UserEntity userToFollow)
			throws UserNotFoundException, UserToFollowExistsException;

	/**
	 * Unfollow user.
	 *
	 * @param user           the user
	 * @param userToUnfollow the user to unfollow
	 * @return the user entity
	 * @throws UserNotFoundException           the user not found exception
	 * @throws UserToUnfollowNotFoundException the user to unfollow not found
	 *                                         exception
	 */
	UserEntity unfollowUser(UserEntity user, UserEntity userToUnfollow)
			throws UserNotFoundException, UserToUnfollowNotFoundException;

	/**
	 * Gets the max rating.
	 *
	 * @return the max rating
	 */
	int getMaxRating();

	/**
	 * Gets the min rating.
	 *
	 * @return the min rating
	 */
	int getMinRating();

	/**
	 * Exists rating from user to rate user.
	 *
	 * @param user      the user
	 * @param ratedUser the rated user
	 * @return true, if successful
	 * @throws UserNotFoundException the user not found exception
	 */
	boolean existsRatingFromUserToRateUser(UserEntity user, UserEntity ratedUser) throws UserNotFoundException;

	/**
	 * Exists rating for user.
	 *
	 * @param user the user
	 * @return true, if successful
	 * @throws UserNotFoundException the user not found exception
	 */
	boolean existsRatingForUser(UserEntity user) throws UserNotFoundException;

	/**
	 * Rate user.
	 *
	 * @param user       the user
	 * @param userToRate the user to rate
	 * @param rating     the rating
	 * @throws UserNotFoundException             the user not found exception
	 * @throws UserAlreadyGiveRatingToUserToRate the user already give rating to
	 *                                           user to rate
	 * @throws LowRatingException                the low rating exception
	 * @throws HighRatingException               the high rating exception
	 */
	void rateUser(UserEntity user, UserEntity userToRate, Integer rating)
			throws UserNotFoundException, UserAlreadyGiveRatingToUserToRate, LowRatingException, HighRatingException;

	/**
	 * Average rating.
	 *
	 * @param user the user
	 * @return the double
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	Double averageRating(UserEntity user) throws UserNotFoundException, UserNoRatingException;

	/**
	 * Given rating from user to rated user.
	 *
	 * @param user      the user
	 * @param ratedUser the rated user
	 * @return the int
	 * @throws UserNotFoundException the user not found exception
	 */
	int givenRatingFromUserToRatedUser(UserEntity user, UserEntity ratedUser) throws UserNotFoundException;

	public UserEntity premiumUser(UserEntity user) throws UserNotFoundException;

}
