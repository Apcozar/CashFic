package es.udc.fi.dc.fd.service.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.ChatRoomRepository;
import es.udc.fi.dc.fd.repository.UserRepository;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@Service
public class DefaultChatRoomService implements ChatRoomService {

	private ChatRoomRepository chatRoomRepository;

	private final UserRepository userRepository;

	@Autowired
	public DefaultChatRoomService(final ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
		super();
		this.chatRoomRepository = checkNotNull(chatRoomRepository,
				"Received a null pointer as saleAdvertisementRepository");
		this.userRepository = checkNotNull(userRepository, "Received a null pointer as saleAdvertisementRepository");
	}

	@Override
	public Integer getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist)
			throws UserNotFoundException, ChatRoomNotFoundException {

		Optional<DefaultChatRoomEntity> chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId,
				recipientId);

		if (chatRoom.isPresent()) {
			return chatRoom.get().getId();
		}

		if (createIfNotExist) {

			Optional<DefaultUserEntity> sender = userRepository.findById(senderId);
			Optional<DefaultUserEntity> recipient = userRepository.findById(recipientId);

			if (!sender.isPresent())
				throw new UserNotFoundException(senderId);

			if (!recipient.isPresent())
				throw new UserNotFoundException(recipientId);

			DefaultChatRoomEntity newChatRoom = new DefaultChatRoomEntity(sender.get(), recipient.get());

			chatRoomRepository.save(newChatRoom);

			return newChatRoom.getId();

		} else {
			throw new ChatRoomNotFoundException(senderId, recipientId);
		}

	}

	@Override
	public Set<DefaultChatRoomEntity> findByUserId(Integer userId) throws UserNotFoundException {

		Optional<DefaultUserEntity> user = userRepository.findById(userId);

		if (!user.isPresent())
			throw new UserNotFoundException(userId);

		return chatRoomRepository.findByUserId(userId);
	}
}
