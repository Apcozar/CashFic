package es.udc.fi.dc.fd.model.form.account;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class SignInForm.
 */
public class SignInForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6007908253327250119L;

	/** The login. */
	@NotEmpty(message = "{notEmpty}")
	private String login;

	@NotEmpty(message = "{notEmpty}")
	private String password;

	/**
	 * Instantiates a new sign in form.
	 */
	@NotEmpty
	public SignInForm() {
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
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(password, login);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SignInForm)) {
			return false;
		}
		SignInForm other = (SignInForm) obj;
		return Objects.equals(password, other.password) && Objects.equals(login, other.login);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "SignInFrom [login=" + login + ", password=" + password + "]";
	}

}
