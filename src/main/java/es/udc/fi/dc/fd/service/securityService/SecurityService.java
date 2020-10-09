package es.udc.fi.dc.fd.service.securityService;

/**
 * The Interface SecurityService.
 */
public interface SecurityService {
	/**
	 * Find logged in user name.
	 *
	 * @return the string
	 */
	String findLoggedInUsername();

	/**
	 * Autologin.
	 *
	 * @param username the username
	 * @param password the password
	 */
	void autologin(String username, String password);

	/**
	 * Logout.
	 */
	public void logout();
}
