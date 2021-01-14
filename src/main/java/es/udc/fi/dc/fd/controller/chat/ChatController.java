package es.udc.fi.dc.fd.controller.chat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.form.ChatForm;
import es.udc.fi.dc.fd.model.persistence.DefaultChatMessageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultChatRoomEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.chat.ChatMessageService;
import es.udc.fi.dc.fd.service.chat.ChatRoomService;
import es.udc.fi.dc.fd.service.chat.exceptions.ChatRoomNotFoundException;
import es.udc.fi.dc.fd.service.chat.exceptions.IncorrectChatMessageException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

/**
 * The Class ChatController.
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

	/**
	 * The User service.
	 */
	private final UserService userService;

	/**
	 * The Security service.
	 */
	private final SecurityService securityService;

	/** The chat room service. */
	private final ChatRoomService chatRoomService;

	/** The chat message service. */
	private final ChatMessageService chatMessageService;

	/**
	 * Constructs a controller with the specified dependencies.
	 *
	 * @param userService        the user service
	 * @param securityService    the security service
	 * @param chatRoomService    the chat room service
	 * @param chatMessageService the chat message service
	 */
	@Autowired
	public ChatController(UserService userService, SecurityService securityService, ChatRoomService chatRoomService,
			ChatMessageService chatMessageService) {
		super();
		this.securityService = checkNotNull(securityService, ViewConstants.NULL_POINTER);

		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);

		this.chatRoomService = checkNotNull(chatRoomService, ViewConstants.NULL_POINTER);

		this.chatMessageService = checkNotNull(chatMessageService, ViewConstants.NULL_POINTER);
	}

	/**
	 * Show the chat without any selected conversation.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/emptyChat")
	public String showEmptyChat(Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			model.addAttribute(ChatViewConstants.USER, user);
			model.addAttribute(ChatViewConstants.CHAT_FORM, new ChatForm());

			Set<DefaultChatRoomEntity> chatList = chatRoomService.findByUserId(user.getId());
			model.addAttribute(ChatViewConstants.CHAT_LIST, chatList);

			return ChatViewConstants.CHAT_VIEW;

		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	/**
	 * Show chat with the user with the id .
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/{id}")
	public String showChat(@PathVariable(value = "id") Integer id, Model model) {
		try {
			loadMessagges(model, id);

			return ChatViewConstants.CHAT_VIEW;

		} catch (UserNotFoundException | ChatRoomNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	/**
	 * Show messages with the user with the id only by reloading the section
	 * #messages_history.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/messages/{id}")
	public String showMessagges(@PathVariable(value = "id") Integer id, Model model) {
		try {
			loadMessagges(model, id);

			return ChatViewConstants.CHAT_VIEW + " :: #messages_history";

		} catch (UserNotFoundException | ChatRoomNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	/**
	 * Returns true if there are new messages with the user id.
	 *
	 * @param userId      the user id
	 * @param messageSize the message size
	 * @param model       the model
	 * @return true, if successful
	 */
	@GetMapping(path = "/{userId}/{messageSize}")
	public @ResponseBody boolean areNewMessages(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "messageSize") Integer messageSize, Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);
			DefaultUserEntity recipientUser = userService.findById(userId);

			List<DefaultChatMessageEntity> messages = chatMessageService.findChatMessages(user.getId(),
					recipientUser.getId());

			return messages.size() != messageSize;

		} catch (UserNotFoundException | ChatRoomNotFoundException e) {
			return false;
		}
	}

	/**
	 * Send message from logged user to the user id.
	 *
	 * @param chatForm      the chat form
	 * @param bindingResult the binding result
	 * @param id            the id
	 * @param model         the model
	 * @return the string
	 */
	@PostMapping(path = "/{id}")
	public String sendMessage(@Valid @ModelAttribute ChatForm chatForm, BindingResult bindingResult,
			@PathVariable(value = "id") Integer id, final Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);
			DefaultUserEntity recipientUser = userService.findById(id);

			if (!bindingResult.hasErrors()) {
				chatMessageService.sendChatMessage(user.getId(), recipientUser.getId(), chatForm.getMessageText());
				model.addAttribute(ChatViewConstants.CHAT_FORM, new ChatForm());
			}

			model.addAttribute(ChatViewConstants.USER, user);
			model.addAttribute(ChatViewConstants.RECIPIENT_USER_ID, recipientUser.getId());

			Integer chatId = chatRoomService.getChatId(user.getId(), recipientUser.getId(), true);
			model.addAttribute(ChatViewConstants.CHAT_ACTIVE, chatId);

			Set<DefaultChatRoomEntity> chatList = chatRoomService.findByUserId(user.getId());
			model.addAttribute(ChatViewConstants.CHAT_LIST, chatList);

			List<DefaultChatMessageEntity> messages = chatMessageService.findChatMessages(user.getId(),
					recipientUser.getId());
			model.addAttribute(ChatViewConstants.MESSAGES, messages);
			model.addAttribute(ChatViewConstants.MESSAGES_SIZE, messages.size());

			return ChatViewConstants.CHAT_VIEW;

		} catch (UserNotFoundException | ChatRoomNotFoundException | IncorrectChatMessageException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	private void loadMessagges(Model model, Integer userToChatId)
			throws UserNotFoundException, ChatRoomNotFoundException {
		String username = this.securityService.findLoggedInUsername();
		DefaultUserEntity user = userService.findByLogin(username);
		DefaultUserEntity recipientUser = userService.findById(userToChatId);

		model.addAttribute(ChatViewConstants.USER, user);
		model.addAttribute(ChatViewConstants.RECIPIENT_USER_ID, recipientUser.getId());
		model.addAttribute(ChatViewConstants.CHAT_FORM, new ChatForm());

		Integer chatId = chatRoomService.getChatId(user.getId(), recipientUser.getId(), true);
		model.addAttribute(ChatViewConstants.CHAT_ACTIVE, chatId);

		Set<DefaultChatRoomEntity> chatList = chatRoomService.findByUserId(user.getId());
		model.addAttribute(ChatViewConstants.CHAT_LIST, chatList);

		List<DefaultChatMessageEntity> messages = chatMessageService.findChatMessages(user.getId(),
				recipientUser.getId());
		model.addAttribute(ChatViewConstants.MESSAGES, messages);
		model.addAttribute(ChatViewConstants.MESSAGES_SIZE, messages.size());
	}
}
