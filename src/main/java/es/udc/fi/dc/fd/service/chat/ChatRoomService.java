package es.udc.fi.dc.fd.service.chat;

import java.util.Set;

import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * The Interface ChatRoomService.
 */
public interface ChatRoomService {

	/**
	 * Gets the chat id between senderId and recipientId. If createIfNotExist is
	 * true then create the chat if it does not exist
	 *
	 * @param senderId         the sender id
	 * @param recipientId      the recipient id
	 * @param createIfNotExist the create if not exist
	 * @return the chat id
	 * @throws UserNotFoundException     the user not found exception
	 * @throws ChatRoomNotFoundException the chat room not found exception
	 */
	Integer getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist)
			throws UserNotFoundException, ChatRoomNotFoundException;

	/**
	 * Find by user id.
	 *
	 * @param userId the user id
	 * @return the sets the
	 * @throws UserNotFoundException the user not found exception
	 */
	Set<DefaultChatRoomEntity> findByUserId(Integer userId) throws UserNotFoundException;

}