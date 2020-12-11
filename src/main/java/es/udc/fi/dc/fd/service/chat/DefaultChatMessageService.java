package es.udc.fi.dc.fd.service.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.ChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.ChatMessageRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatMessageNotFound;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.chat.exceptions.IncorrectChatMessageException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@Service
public class DefaultChatMessageService implements ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;

	private final ChatRoomService chatRoomService;

	private final UserRepository userRepository;

	private static final String NULL_POINTER_RECEIVED = "Received a null pointer as saleAdvertisementRepository";

	@Autowired
	public DefaultChatMessageService(ChatMessageRepository chatMessageRepository,
			DefaultChatRoomService chatRoomService, UserRepository userRepository) {
		super();
		this.chatMessageRepository = checkNotNull(chatMessageRepository, NULL_POINTER_RECEIVED);
		this.chatRoomService = checkNotNull(chatRoomService, NULL_POINTER_RECEIVED);
		this.userRepository = checkNotNull(userRepository, NULL_POINTER_RECEIVED);
	}

	@Override
	public ChatMessageEntity sendChatMessage(Integer senderId, Integer recipientId, String content)
			throws UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		Optional<DefaultUserEntity> sender = userRepository.findById(senderId);
		Optional<DefaultUserEntity> recipient = userRepository.findById(recipientId);

		if (!sender.isPresent())
			throw new UserNotFoundException(senderId);

		if (!recipient.isPresent())
			throw new UserNotFoundException(recipientId);

		if (content == null || content.isEmpty() || content.isBlank())
			throw new IncorrectChatMessageException();

		Integer chatId = chatRoomService.getChatId(senderId, recipientId, true);

		DefaultChatMessageEntity message = new DefaultChatMessageEntity(chatId, sender.get(), recipient.get(), content);

		return chatMessageRepository.save(message);
	}

	@Override
	public List<DefaultChatMessageEntity> findChatMessages(Integer senderId, Integer recipientId)
			throws UserNotFoundException, ChatRoomNotFoundException {
		Integer chatId = chatRoomService.getChatId(senderId, recipientId, true);

		return chatMessageRepository.findByChatId(chatId);
	}

	@Override
	public ChatMessageEntity findById(Integer messageId) throws ChatMessageNotFound {
		Optional<DefaultChatMessageEntity> message = chatMessageRepository.findById(messageId);

		if (message.isPresent()) {
			return message.get();
		} else {
			throw new ChatMessageNotFound(messageId);
		}

	}
}
