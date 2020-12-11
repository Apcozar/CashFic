package es.udc.fi.dc.fd.service.chat.exceptions;

/**
 * The Class IncorrectChatMessageException.
 */
public class IncorrectChatMessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new incorrect chat message exception.
	 */
	public IncorrectChatMessageException() {

		super("The message content is invalid");
	}
}
