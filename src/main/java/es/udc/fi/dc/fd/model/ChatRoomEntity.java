package es.udc.fi.dc.fd.model;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * The Interface ChatRoomEntity.
 */
public interface ChatRoomEntity extends Serializable {

	/**
	 * Sets the user two.
	 *
	 * @param user the new user two
	 */
	void setUserTwo(DefaultUserEntity user);

	/**
	 * Sets the user one.
	 *
	 * @param user the new user one
	 */
	void setUserOne(DefaultUserEntity user);

	/**
	 * Gets the user one.
	 *
	 * @return the user one
	 */
	DefaultUserEntity getUserOne();

	/**
	 * Gets the user two.
	 *
	 * @return the user two
	 */
	DefaultUserEntity getUserTwo();

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	Integer getId();

}