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

package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.UserEntity;

/**
 * Persistent entity for the users.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago
 */
@Entity(name = "UserEntity")
@Table(name = "users")
public class DefaultUserEntity implements UserEntity {

	/** The Constant NULL_USER. */
	private static final String NULL_USER = "Received a null pointer as user";

	/**
	 * Serialization ID.
	 */
	@Transient
	private static final long serialVersionUID = 1328776989450853491L;

	/**
	 * User's ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	/**
	 * login of the user.
	 */
	@Column(name = "login", nullable = false, unique = true)
	private String login;

	/**
	 * The password.
	 */
	@Column(name = "password", nullable = false, unique = false)
	private String password;

	/**
	 * name of the user.
	 */
	@Column(name = "name", nullable = false, unique = false)
	private String name;

	/**
	 * Last name of the user.
	 */
	@Column(name = "last_name", nullable = false, unique = false)
	private String lastName;

	/**
	 * The user email.
	 */
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	/**
	 * city of the user.
	 */
	@Column(name = "city", nullable = false, unique = false)
	private String city = "";

	/**
	 * The user role.
	 */
	@Column(name = "role", nullable = true, unique = false)
	private Role role;

	/**
	 * Sale_advertisements of the user.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@OneToMany(mappedBy = "user")
	private Set<DefaultSaleAdvertisementEntity> sale_advertisements;

	/** The followers. */
	@ManyToMany(mappedBy = "followed")
	private Set<DefaultUserEntity> followers = new HashSet<>();

	/** The followed. */
	@ManyToMany
	@JoinTable(name = "follow_users", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "user_follow_id"))
	private Set<DefaultUserEntity> followed = new HashSet<>();

	/**
	 * The sale advertisements liked by the user
	 */
	@ManyToMany
	@JoinTable(name = "likes", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "sale_advertisement_id"))
	private Set<DefaultSaleAdvertisementEntity> likedSaleAdvertisements = new HashSet<DefaultSaleAdvertisementEntity>();

	/**
	 * Constructs an sale_advertisement entity.
	 */
	public DefaultUserEntity() {
		super();
	}

	public DefaultUserEntity(String login, String password, String name, String lastName, String email, String city) {
		super();
		this.login = login;
		this.password = password;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.city = city;
		this.role = Role.ROLE_USER;
		this.sale_advertisements = new HashSet<>();
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Override
	public Integer getId() {
		return id;
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	@Override
	public String getLogin() {
		return login;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	@Override
	public String getCity() {
		return city;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	@Override
	public Role getRole() {
		return role;
	}

	/**
	 * Gets the sale advertisements.
	 *
	 * @return the sale advertisements
	 */
	@Override
	public Set<DefaultSaleAdvertisementEntity> getSaleAdvertisements() {
		return sale_advertisements;
	}

	/**
	 * Gets the followers.
	 *
	 * @return the followers
	 */
	@Override
	public Set<DefaultUserEntity> getFollowers() {
		return followers;
	}

	/**
	 * Gets the followed.
	 *
	 * @return the followed
	 */
	@Override
	public Set<DefaultUserEntity> getFollowed() {
		return followed;
	}

	/**
	 * Sets the id.
	 *
	 * @param value the new id
	 */
	@Override
	public void setId(final Integer value) {
		id = checkNotNull(value, "Received a null pointer as identifier");
	}

	/**
	 * Sets the login.
	 *
	 * @param value the new login
	 */
	@Override
	public void setLogin(final String value) {
		login = checkNotNull(value, "Received a null pointer as login");
	}

	/**
	 * Sets the password.
	 *
	 * @param value the new password
	 */
	@Override
	public void setPassword(final String value) {
		password = checkNotNull(value, "Received a null pointer as password");
	}

	/**
	 * Sets the name.
	 *
	 * @param value the new name
	 */
	@Override
	public void setName(final String value) {
		name = checkNotNull(value, "Received a null pointer as name");
	}

	/**
	 * Sets the last name.
	 *
	 * @param value the new last name
	 */
	@Override
	public void setLastName(final String value) {
		lastName = checkNotNull(value, "Received a null pointer as last name");
	}

	/**
	 * Sets the email.
	 *
	 * @param value the new email
	 */
	@Override
	public void setEmail(final String value) {
		email = checkNotNull(value, "Received a null pointer as email");
	}

	/**
	 * Sets the city.
	 *
	 * @param value the new city
	 */
	@Override
	public void setCity(final String value) {
		city = checkNotNull(value, "Received a null pointer as city");
	}

	/**
	 * Sets the role.
	 *
	 * @param value the new role
	 */
	@Override
	public void setRole(final Role value) {
		role = checkNotNull(value, "Received a null pointer as role");
	}

	/**
	 * Sets the sale advertisements.
	 *
	 * @param value the new sale advertisements
	 */
	@Override
	public void setSale_advertisements(final Set<DefaultSaleAdvertisementEntity> value) {
		sale_advertisements = checkNotNull(value, "Received a null pointer as images");
	}

	/**
	 * Adds the follower user to the list of user followers.
	 *
	 * @param user the user
	 */
	@Override
	public void addFollowserUser(DefaultUserEntity user) {
		checkNotNull(user, NULL_USER);
		followers.add(user);
	}

	/**
	 * Removes the follower user from the list of used followers.
	 *
	 * @param user the user
	 */
	@Override
	public void removeFollowserUser(DefaultUserEntity user) {
		checkNotNull(user, NULL_USER);
		followers.remove(user);
	}

	/**
	 * Adds the follow user to the list of user followed.
	 *
	 * @param user the user
	 */
	@Override
	public void addFollowUser(DefaultUserEntity user) {
		checkNotNull(user, NULL_USER);
		followed.add(user);
	}

	/**
	 * Removes the follow user from the list of used followed.
	 *
	 * @param user the user
	 */
	@Override
	public void removeFollowUser(DefaultUserEntity user) {
		checkNotNull(user, NULL_USER);
		followed.remove(user);
	}

	@Override
	public String toString() {
		return "UserEntityImplementation [id=" + id + ", login=" + login + ", password=" + password + ", name=" + name
				+ ", lastName=" + lastName + ", email=" + email + ", city=" + city + ", role=" + role + "]";
	}

	@Override
	public Set<DefaultSaleAdvertisementEntity> getLikes() {
		return likedSaleAdvertisements;
	}

	@Override
	public void addLike(SaleAdvertisementEntity saleAdvertisement) {
		checkNotNull(saleAdvertisement, "Received a null pointer as saleAdvertisement");
		likedSaleAdvertisements.add((DefaultSaleAdvertisementEntity) saleAdvertisement);
	}

	@Override
	public void removeLike(SaleAdvertisementEntity saleAdvertisement) {
		checkNotNull(saleAdvertisement, "Received a null pointer as saleAdvertisement");
		likedSaleAdvertisements.remove((DefaultSaleAdvertisementEntity) saleAdvertisement);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, login, password, name, lastName, email, city, role);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof DefaultUserEntity)) {
			return false;
		}
		DefaultUserEntity other = (DefaultUserEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(login, other.login)
				&& Objects.equals(password, other.password) && Objects.equals(name, other.name)
				&& Objects.equals(lastName, other.lastName) && role == other.role && Objects.equals(email, other.email)
				&& Objects.equals(city, other.city);
	}

}
