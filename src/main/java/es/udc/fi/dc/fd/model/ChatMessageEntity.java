package es.udc.fi.dc.fd.model;

import java.io.Serializable;
import java.util.Date;

import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * The Interface ChatMessageEntity.
 */
public interface ChatMessageEntity extends Serializable {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	Integer getId();

	/**
	 * Gets the author user.
	 *
	 * @return the author user
	 */
	DefaultUserEntity getAuthorUser();

	/**
	 * Gets the recipient user.
	 *
	 * @return the recipient user
	 */
	DefaultUserEntity getRecipientUser();

	/**
	 * Sets the author user.
	 *
	 * @param user the new author user
	 */
	void setAuthorUser(DefaultUserEntity user);

	/**
	 * Sets the recipient user.
	 *
	 * @param user the new recipient user
	 */
	void setRecipientUser(DefaultUserEntity user);

	/**
	 * Gets the time sent.
	 *
	 * @return the time sent
	 */
	Date getTimeSent();

	/**
	 * Gets the contents.
	 *
	 * @return the contents
	 */
	String getContents();

	/**
	 * Gets the chat id.
	 *
	 * @return the chat id
	 */
	Integer getChatId();

	/**
	 * Sets the chat id.
	 *
	 * @param chatId the new chat id
	 */
	void setChatId(Integer chatId);

}