package es.udc.fi.dc.fd.model.persistence;

import java.io.Serializable;

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
	 * @return the int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userRatedId == null) ? 0 : userRatedId.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RateUserKey other = (RateUserKey) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userRatedId == null) {
			if (other.userRatedId != null)
				return false;
		} else if (!userRatedId.equals(other.userRatedId))
			return false;
		return true;
	}

}
