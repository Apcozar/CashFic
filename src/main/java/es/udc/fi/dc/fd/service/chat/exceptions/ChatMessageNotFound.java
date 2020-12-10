package es.udc.fi.dc.fd.service.chat.exceptions;

/**
 * The Class ChatMessaggeNotFound.
 */
public class ChatMessageNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new chat messagge not found.
	 *
	 * @param messageId the message id
	 */
	public ChatMessageNotFound(Integer messageId) {

		super("The message with the id: " + messageId + " not found");
	}
}
