package es.udc.fi.dc.fd.model.form;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

public class ChatForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotEmpty(message = "{notEmpty}")
	private String messageText;

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public ChatForm(String messageText) {
		super();
		this.messageText = messageText;
	}

	public ChatForm() {
		super();
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageText);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChatForm other = (ChatForm) obj;
		return Objects.equals(messageText, other.messageText);
	}

	@Override
	public String toString() {
		return "ChatForm [messageText=" + messageText + "]";
	}
}
