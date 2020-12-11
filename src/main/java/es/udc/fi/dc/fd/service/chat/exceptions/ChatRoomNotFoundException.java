package es.udc.fi.dc.fd.service.chat.exceptions;

/**
 * The Class ChatRoomNotFoundException.
 */
public class ChatRoomNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new chat room not found exception.
	 *
	 * @param senderId    the sender id
	 * @param recipientId the recipient id
	 */
	public ChatRoomNotFoundException(Integer senderId, Integer recipientId) {

		super("The chat room between the user with the id: " + senderId + "and the user with the id: " + recipientId
				+ " not found");
	}
}
