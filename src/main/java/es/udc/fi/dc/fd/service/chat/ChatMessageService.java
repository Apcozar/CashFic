package es.udc.fi.dc.fd.service.chat;

import java.util.List;

import es.udc.fi.dc.fd.model.ChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultChatMessageEntity;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatMessageNotFound;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.chat.exceptions.IncorrectChatMessageException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * The Interface ChatMessageService.
 */
public interface ChatMessageService {

	/**
	 * Send chat message.
	 *
	 * @param senderId    the sender id
	 * @param recipientId the recipient id
	 * @param content     the content
	 * @return the chat message entity
	 * @throws UserNotFoundException         the user not found exception
	 * @throws ChatRoomNotFoundException     the chat room not found exception
	 * @throws IncorrectChatMessageException the incorrect chat message exception
	 */
	ChatMessageEntity sendChatMessage(Integer senderId, Integer recipientId, String content)
			throws UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException;

	/**
	 * Find chat messages.
	 *
	 * @param senderId    the sender id
	 * @param recipientId the recipient id
	 * @return the list
	 * @throws UserNotFoundException     the user not found exception
	 * @throws ChatRoomNotFoundException the chat room not found exception
	 */
	List<DefaultChatMessageEntity> findChatMessages(Integer senderId, Integer recipientId)
			throws UserNotFoundException, ChatRoomNotFoundException;

	/**
	 * Find by id.
	 *
	 * @param messageId the message id
	 * @return the chat message entity
	 * @throws ChatMessageNotFound the chat message not found
	 */
	ChatMessageEntity findById(Integer messageId) throws ChatMessageNotFound;

}