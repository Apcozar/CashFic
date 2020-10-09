package es.udc.fi.dc.fd.model.form.account;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class SignUpForm.
 */
public class SignUpForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8518415547116270497L;

	/** The login. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 6, max = 40, message = "{loginSize}")
	private String login;

	/** The password. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 8, max = 40, message = "{passwordSize}")
	private String password;

	/** The user name. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 1, max = 30, message = "{nameSize}")
	private String name;

	/** The user last name. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 1, max = 40, message = "{lastNameSize}")
	private String lastName;

	/** The email. */
	@NotEmpty(message = "{notEmpty}")
	@Email(message = "{notMail}")
	private String email;

	/** The city. */
	@NotEmpty(message = "{notEmpty}")
	private String city;

	/**
	 * Instantiates a new sign up form.
	 */
	@NotEmpty
	public SignUpForm() {
		super();
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Sets the login.
	 *
	 * @param login the new login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public int hashCode() {
		return Objects.hash(login, password, name, lastName, email, city);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SignUpForm)) {
			return false;
		}
		SignUpForm other = (SignUpForm) obj;
		return Objects.equals(login, other.login) && Objects.equals(password, other.password)
				&& Objects.equals(name, other.name) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(email, other.email) && Objects.equals(city, other.city);
	}

	@Override
	public String toString() {
		return "SingUpForm [login=" + login + ", password=" + password + ", name=" + name + ", lastName=" + lastName
				+ ", email=" + email + ", city=" + city + "]";
	}

}
