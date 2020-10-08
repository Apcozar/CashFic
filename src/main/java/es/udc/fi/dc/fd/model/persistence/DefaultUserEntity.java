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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.Role;
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
	@Column(name = "lastName", nullable = false, unique = false)
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
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<DefaultSale_advertisementEntity> sale_advertisements;

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

	@Override
	public Set<DefaultSale_advertisementEntity> getSaleAdvertisements() {
		return sale_advertisements;
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

	@Override
	public void setSale_advertisements(final Set<DefaultSale_advertisementEntity> value) {
		sale_advertisements = checkNotNull(value, "Received a null pointer as images");
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

	@Override
	public String toString() {
		return "UserEntityImplementation [id=" + id + ", login=" + login + ", password=" + password + ", name=" + name
				+ ", lastName=" + lastName + ", email=" + email + ", city=" + city + ", role=" + role + "]";
	}

}
