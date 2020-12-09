package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.model.persistence.RateUserKey;

/**
 * The Interface RateUserEntity.
 */
public interface RateUserEntity extends Serializable {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	RateUserKey getId();

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	void setId(RateUserKey id);

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	DefaultUserEntity getUser();

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	void setUser(DefaultUserEntity user);

	/**
	 * Gets the user rated.
	 *
	 * @return the user rated
	 */
	DefaultUserEntity getUserRated();

	/**
	 * Sets the user rated.
	 *
	 * @param userRated the new user rated
	 */
	void setUserRated(DefaultUserEntity userRated);

	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	Integer getRating();

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	void setRating(Integer rating);

}
