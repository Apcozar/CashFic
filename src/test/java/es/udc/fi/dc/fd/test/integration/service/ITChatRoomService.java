package es.udc.fi.dc.fd.test.integration.service;

import java.util.Optional;
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

import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.repository.ChatRoomRepository;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.chat.ChatRoomService;
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
class ITChatRoomService {
	/** The non existent id. */
	private final Integer NON_EXISTENT_ID = -1;

	/** The login. */
	private final String LOGIN = "login";

	/** The login2. */
	private final String LOGIN2 = "login2";

	/** The login3. */
	private final String LOGIN3 = "login3";

	/** The login4. */
	private final String LOGIN4 = "login4";

	/** The email. */
	private final String EMAIL = "user@udc.es";

	/** The email2. */
	private final String EMAIL2 = "user2@udc.es";

	/** The email3. */
	private final String EMAIL3 = "user3@udc.es";

	/** The email4. */
	private final String EMAIL4 = "user4@udc.es";

	/** The password. */
	private final String PASSWORD = "password";

	@Autowired
	private UserService userService;

	@Autowired
	private ChatRoomService chatRoomService;

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	public ITChatRoomService() {
		super();
	}

	private DefaultUserEntity createUser(String login, String email) {
		return new DefaultUserEntity(login, PASSWORD, "userName", "userLastName", email, "city");
	}

	@Test
	void getChatIdTest() throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		Integer chatRommId = chatRoomService.getChatId(userOne.getId(), userTwo.getId(), true);

		Assert.assertNotNull(chatRommId);
	}

	@Test
	void getChatIdChatRoomNotExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userOne);
		userService.signUp(userTwo);

		Assertions.assertThrows(ChatRoomNotFoundException.class, () -> {
			chatRoomService.getChatId(userOne.getId(), userTwo.getId(), false);
		});

	}

	@Test
	void getChatIdChatRoomUserOneNotExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);

		userService.signUp(userTwo);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatRoomService.getChatId(NON_EXISTENT_ID, userTwo.getId(), true);
		});

	}

	@Test
	void getChatIdChatRoomUserTwoNotExistsTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);

		userService.signUp(userOne);

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatRoomService.getChatId(userOne.getId(), NON_EXISTENT_ID, true);
		});

	}

	@Test
	void findByUserIdTest() throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {
		DefaultUserEntity userOne = createUser(LOGIN, EMAIL);
		DefaultUserEntity userTwo = createUser(LOGIN2, EMAIL2);
		DefaultUserEntity userThree = createUser(LOGIN3, EMAIL3);
		DefaultUserEntity userFour = createUser(LOGIN4, EMAIL4);

		userService.signUp(userOne);
		userService.signUp(userTwo);
		userService.signUp(userThree);
		userService.signUp(userFour);

		Set<DefaultChatRoomEntity> chatRooms = chatRoomService.findByUserId(userOne.getId());
		Assert.assertEquals(0, chatRooms.size());

		Integer chatRommOneId = chatRoomService.getChatId(userOne.getId(), userTwo.getId(), true);
		chatRooms = chatRoomService.findByUserId(userOne.getId());

		Assert.assertEquals(1, chatRooms.size());

		Optional<DefaultChatRoomEntity> chatRoomOne = chatRoomRepository.findById(chatRommOneId);
		Assert.assertEquals(userOne, chatRoomOne.get().getUserOne());
		Assert.assertEquals(userTwo, chatRoomOne.get().getUserTwo());

		Integer chatRommTwoId = chatRoomService.getChatId(userOne.getId(), userThree.getId(), true);
		Integer chatRommThreeId = chatRoomService.getChatId(userOne.getId(), userFour.getId(), true);

		chatRooms = chatRoomService.findByUserId(userOne.getId());

		Assert.assertEquals(3, chatRooms.size());

		Optional<DefaultChatRoomEntity> chatRoomTwo = chatRoomRepository.findById(chatRommTwoId);
		Assert.assertEquals(userOne, chatRoomTwo.get().getUserOne());
		Assert.assertEquals(userThree, chatRoomTwo.get().getUserTwo());

		Optional<DefaultChatRoomEntity> chatRoomThree = chatRoomRepository.findById(chatRommThreeId);
		Assert.assertEquals(userOne, chatRoomThree.get().getUserOne());
		Assert.assertEquals(userFour, chatRoomThree.get().getUserTwo());

		chatRooms = chatRoomService.findByUserId(userTwo.getId());
		Assert.assertEquals(1, chatRooms.size());
		chatRooms = chatRoomService.findByUserId(userThree.getId());
		Assert.assertEquals(1, chatRooms.size());
		chatRooms = chatRoomService.findByUserId(userFour.getId());
		Assert.assertEquals(1, chatRooms.size());
	}

	@Test
	void findByUserIdNonExistentTest()
			throws UserLoginExistsException, UserEmailExistsException, UserLoginAndEmailExistsException,
			UserNotFoundException, ChatRoomNotFoundException, IncorrectChatMessageException {

		Assertions.assertThrows(UserNotFoundException.class, () -> {
			chatRoomService.findByUserId(NON_EXISTENT_ID);
		});

	}

}
