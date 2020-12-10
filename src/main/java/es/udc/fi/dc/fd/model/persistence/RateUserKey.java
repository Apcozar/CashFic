package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class RateUserKey.
 */
@Embeddable
public class RateUserKey implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The user id. */
	@Column(name = "user_id")
	Integer userId;

	/** The user rated id. */
	@Column(name = "user_rated_id")
	Integer userRatedId;

	/**
	 * Instantiates a new rate user key.
	 */
	public RateUserKey() {
		super();
	}

	/**
	 * Instantiates a new rate user key.
	 *
	 * @param userId      the user id
	 * @param userRatedId the user rated id
	 */
	public RateUserKey(Integer userId, Integer userRatedId) {
		super();
		this.userId = userId;
		this.userRatedId = userRatedId;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user rated id.
	 *
	 * @return the user rated id
	 */
	public Integer getUserRatedId() {
		return userRatedId;
	}

	/**
	 * Sets the user rated id.
	 *
	 * @param userRatedId the new user rated id
	 */
	public void setUserRatedId(Integer userRatedId) {
		this.userRatedId = userRatedId;
	}

	/**
	 * Hash code.
	 *
	 * @return the integer
	 */
	@Override
	public int hashCode() {
		return Objects.hash(userId, userRatedId);
	}

	/**
	 * Equals.
	 *
	 * @param obj the object
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RateUserKey)) {
			return false;
		}
		RateUserKey other = (RateUserKey) obj;
		return Objects.equals(userId, other.userId) && Objects.equals(userRatedId, other.userRatedId);
	}

}
