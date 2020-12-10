package es.udc.fi.dc.fd.test.integration.service;

import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.ChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.chat.ChatMessageService;
import es.udc.fi.dc.fd.service.chat.ChatRoomService;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatMessageNotFound;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.chat.exceptions.IncorrectChatMessageException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@WebAppConfiguration
@RunWith(JUnitPlatform.class)
@SpringJUnitConfig
@Transactional
@Rollback
@ContextConfiguration(locations = { "classpath:context/application-context.xml" })
@TestPropertySource({ "classpath:config/persistence-access.properties" })
class ITChatMessageService {
	/** The non existent id. */
	private final Integer NON_EXISTENT_ID = -1;

	/** The login. */
	private final String LOGIN = "login";

	/** The login2. */
	private final String LOGIN2 = "login2";

	/** The email. */
	private final String EMAIL = "user@udc.es";

	/** The email2. */
	private final String EMAIL2 = "user2@udc.es";

	/** The password. */
	private final String PASSWORD = "password";

	/** The content. */
	private final String CONTENT = "content";

	@Autowired
	private UserService userService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private ChatMessageService chatMessageService;

	public ITChatMessageService() {
		super();
	}

	private DefaultUserEntity createUser(String login, String email) {
		return new DefaultUserEntity(login, PASSWORD, "userName", "userLastName", email, "city");
	}

	@Test
	void sendChatMessageTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		ChatMessageEntity first = chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), CONTENT);

		Integer chatId = chatRoomService.getChatId(userOne.getId(), userTwo.getId(), false);
		Set<DefaultChatRoomEntity> chatRoomsOne = chatRoomService.findByUserId(userOne.getId());
		Set<DefaultChatRoomEntity> chatRoomsTwo = chatRoomService.findByUserId(userTwo.getId());

		Assert.assertEquals(1, chatRoomsOne.size());
		Assert.assertEquals(1, chatRoomsTwo.size());
		Assert.assertEquals(chatRoomsOne, chatRoomsTwo);
		for (DefaultChatRoomEntity chatRoom : chatRoomsTwo) {
			Assert.assertEquals(chatId, chatRoom.getId());
		}

		for (DefaultChatRoomEntity chatRoom : chatRoomsOne) {
			Assert.assertEquals(chatId, chatRoom.getId());
		}

		List<DefaultChatMessageEntity> actual = chatMessageService.findChatMessages(userTwo.getId(), userOne.getId());

		Assert.assertEquals(1, actual.size());
		Assert.assertTrue(actual.contains(first));

		ChatMessageEntity second = chatMessageService.sendChatMessage(userTwo.getId(), userOne.getId(), CONTENT);
		ChatMessageEntity third = chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), CONTENT);
		ChatMessageEntity fourth = chatMessageService.sendChatMessage(userTwo.getId(), userOne.getId(), CONTENT);

		actual = chatMessageService.findChatMessages(userTwo.getId(), userOne.getId());

		Assert.assertEquals(4, actual.size());
		Assert.assertTrue(actual.contains(first));
		Assert.assertTrue(actual.contains(second));
		Assert.assertTrue(actual.contains(third));
		Assert.assertTrue(actual.contains(fourth));

		Assert.assertEquals(first, actual.get(0));
		Assert.assertEquals(second, actual.get(1));
		Assert.assertEquals(third, actual.get(2));
		Assert.assertEquals(fourth, actual.get(3));
	}

	@Test
	void sendChatMessageUserOneNonExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);
		userService.signUp(userTwo);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatMessageService.sendChatMessage(NON_EXISTENT_ID, userTwo.getId(), CONTENT);
		});
	}

	@Test
	void sendChatMessageUserTwoNonExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		userService.signUp(userOne);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatMessageService.sendChatMessage(userOne.getId(), NON_EXISTENT_ID, CONTENT);
		});
	}

	@Test
	void sendChatMessageIncorrectChatMessageTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		Assertions.assertThrows(IncorrectChatMessageException.class, () -> {
			chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), "");
		});
	}

	@Test
	void sendChatMessageIncorrectChatMessage2Test()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		Assertions.assertThrows(IncorrectChatMessageException.class, () -> {
			chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), "      ");
		});
	}

	@Test
	void sendChatMessageIncorrectChatMessage3Test()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		Assertions.assertThrows(IncorrectChatMessageException.class, () -> {
			chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), null);
		});
	}

	@Test
	void findChatMessagesUserOneNotExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userTwo);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatMessageService.findChatMessages(NON_EXISTENT_ID, userTwo.getId());
		});
	}

	@Test
	void findChatMessagesUserTwoNotExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		userService.signUp(userOne);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatMessageService.findChatMessages(userOne.getId(), NON_EXISTENT_ID);
		});
	}

	@Test
	void findByIdTest() throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException, ChatMessageNotFound {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		ChatMessageEntity first = chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), CONTENT);
		ChatMessageEntity second = chatMessageService.sendChatMessage(userTwo.getId(), userOne.getId(), CONTENT);
		ChatMessageEntity third = chatMessageService.sendChatMessage(userOne.getId(), userTwo.getId(), CONTENT);
		ChatMessageEntity fourth = chatMessageService.sendChatMessage(userTwo.getId(), userOne.getId(), CONTENT);

		Assert.assertEquals(first, chatMessageService.findById(first.getId()));
		Assert.assertEquals(second, chatMessageService.findById(second.getId()));
		Assert.assertEquals(third, chatMessageService.findById(third.getId()));
		Assert.assertEquals(fourth, chatMessageService.findById(fourth.getId()));
	}

	@Test
	void findfindChatMessagesTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException, ChatMessageNotFound {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		List<DefaultChatMessageEntity> list = chatMessageService.findChatMessages(userOne.getId(), userTwo.getId());

		Assert.assertEquals(0, list.size());
	}

	@Test
	void findByIdNonExistentTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException, ChatMessageNotFound {

		Assertions.assertThrows(ChatMessageNotFound.class, () -> {
			chatMessageService.findById(NON_EXISTENT_ID);
		});
	}

}
