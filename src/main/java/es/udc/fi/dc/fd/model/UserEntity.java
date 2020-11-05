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

package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.util.Set;

import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * A user entity.
 *
 * @author Santiago
 */
public interface UserEntity extends Serializable {

	/**
	 * Returns the identifier assigned to this user.
	 * <p>
	 * If no identifier has been assigned yet, then the value is expected to be
	 * {@code null} or lower than zero.
	 *
	 * @return the user's identifier
	 */
	public Integer getId();

	/**
	 * Returns the login of the user.
	 *
	 * @return the user's login
	 */
	public String getLogin();

	/**
	 * Returns the password.
	 *
	 * @return the password
	 */
	public String getPassword();

	/**
	 * Returns the name of the user.
	 *
	 * @return the user's name
	 */
	public String getName();

	/**
	 * Returns the last name of the user.
	 *
	 * @return the user's last name
	 */
	public String getLastName();

	/**
	 * Returns the email.
	 *
	 * @return the email
	 */
	public String getEmail();

	/**
	 * Returns the city of the user.
	 *
	 * @return the user's city
	 */
	public String getCity();

	/**
	 * Returns the role.
	 *
	 * @return the role
	 */
	public Role getRole();

	/**
	 * Returns the sale_advertisements of the user.
	 *
	 * @return the user's sale_advertisements
	 */
	public Set<DefaultSaleAdvertisementEntity> getSaleAdvertisements();

	/**
	 * Gets the followers.
	 *
	 * @return the followers
	 */
	public Set<DefaultUserEntity> getFollowers();

	/**
	 * Gets the followed.
	 *
	 * @return the followed
	 */
	public Set<DefaultUserEntity> getFollowed();

	/**
	 * Sets the identifier assigned to this user.
	 *
	 * @param identifier the identifier for the user
	 */
	public void setId(final Integer identifier);

	/**
	 * Changes the login of the user.
	 *
	 * @param login the login to set on the user
	 */
	public void setLogin(final String login);

	/**
	 * Changes the password of the user.
	 *
	 * @param password the password to set on the user
	 */
	public void setPassword(final String password);

	/**
	 * Changes the name of the user.
	 *
	 * @param name the name to set on the user
	 */
	public void setName(final String name);

	/**
	 * Changes the last Name of the user.
	 *
	 * @param lastName the last Name to set on the user
	 */
	public void setLastName(final String lastName);

	/**
	 * Changes the email of the user.
	 *
	 * @param email the email to set on the user
	 */
	public void setEmail(String email);

	/**
	 * Changes the city of the user.
	 *
	 * @param city the city to set on the user
	 */
	public void setCity(final String city);

	/**
	 * Changes the role of the user.
	 *
	 * @param role the role to set on the user
	 */
	public void setRole(Role role);

	/**
	 * Changes the sale_advertisements of the user.
	 *
	 * @param sale_advertisements the sale_advertisements to set on the user
	 */
	public void setSale_advertisements(final Set<DefaultSaleAdvertisementEntity> sale_advertisements);

	/**
	 * Adds the follower user to the list of user followers.
	 *
	 * @param user the user
	 */
	public void addFollowserUser(DefaultUserEntity user);

	/**
	 * Removes the follower user from the list of used followers.
	 *
	 * @param user the user
	 */
	public void removeFollowserUser(DefaultUserEntity user);

	/**
	 * Adds the follow user to the list of user followed.
	 *
	 * @param user the user
	 */
	public void addFollowUser(DefaultUserEntity user);

	/**
	 * Removes the follow user from the list of used followed.
	 *
	 * @param user the user
	 */
	public void removeFollowUser(DefaultUserEntity user);

}
