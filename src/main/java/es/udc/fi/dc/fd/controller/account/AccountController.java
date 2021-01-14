package es.udc.fi.dc.fd.controller.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.dto.BuyTransactionDTO;
import es.udc.fi.dc.fd.model.dto.SaleAdvertisementWithLoggedUserInfoDTO;
import es.udc.fi.dc.fd.model.dto.UserDTO;
import es.udc.fi.dc.fd.model.form.account.RateForm;
import es.udc.fi.dc.fd.model.form.account.SignInForm;
import es.udc.fi.dc.fd.model.form.account.SignUpForm;
import es.udc.fi.dc.fd.model.persistence.DefaultBuyTransactionEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.HighRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.LowRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserAlreadyGiveRatingToUserToRate;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNoRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserToFollowExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserToUnfollowNotFoundException;

@Controller
public class AccountController {
	/**
	 * The User service.
	 */
	private UserService userService;

	/**
	 * The Security service.
	 */
	private SecurityService securityService;

	private String redirect = "redirect:";

	private String referer = "Referer";

	/**
	 * Constructs a controller with the specified dependencies.
	 * 
	 * @param userService     user service
	 * @param securityService security service
	 */
	@Autowired
	public AccountController(final UserService userService, final SecurityService securityService) {
		super();
		this.securityService = checkNotNull(securityService, ViewConstants.NULL_POINTER);

		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);

	}

	/**
	 * Shows the page when the "get" petition is done.
	 * 
	 * @param model model
	 * 
	 * @return the name for the signUp view
	 */
	@GetMapping(path = "/signUp")
	public String showSignUpView(final Model model) {
		model.addAttribute("signUpForm", new SignUpForm());

		return ViewConstants.VIEW_SIGNUP;
	}

	/**
	 * Shows the page when the "post" petition is done.
	 *
	 * @param signUpForm    sign Up Form
	 * @param bindingResult bindingResult
	 * @param model         model
	 * 
	 * @return the welcome view
	 */
	@PostMapping(path = "/signUp")
	public String signUp(@Valid @ModelAttribute("signUpForm") SignUpForm signUpForm, BindingResult bindingResult,
			Model model) {

		try {
			if (bindingResult.hasErrors()) {
				checkUserNameAndEmail(signUpForm, model);
				return ViewConstants.VIEW_SIGNUP;
			}

			userService.signUp(new DefaultUserEntity(signUpForm.getLogin(), signUpForm.getPassword(),
					signUpForm.getName(), signUpForm.getLastName(), signUpForm.getEmail(), signUpForm.getCity()));

			securityService.autologin(signUpForm.getLogin(), signUpForm.getPassword());
			model.addAttribute(ViewConstants.USER_NAME, signUpForm.getLogin());

			return ViewConstants.WELCOME;
		} catch (UserLoginExistsException e) {
			model.addAttribute(AccountViewConstants.USER_EXIST, AccountViewConstants.USER_EXIST);
			checkEmail(signUpForm, model);
			return ViewConstants.VIEW_SIGNUP;
		} catch (UserEmailExistsException e) {
			model.addAttribute(AccountViewConstants.EMAIL_EXIST, AccountViewConstants.EMAIL_EXIST);
			return ViewConstants.VIEW_SIGNUP;
		} catch (UserLoginAndEmailExistsException e) {
			model.addAttribute(AccountViewConstants.USER_EXIST, AccountViewConstants.USER_EXIST);
			model.addAttribute(AccountViewConstants.EMAIL_EXIST, AccountViewConstants.EMAIL_EXIST);
			return ViewConstants.VIEW_SIGNUP;
		}
	}

	/**
	 * Show sign in view.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/signIn")
	public String showSignInView(final Model model) {
		model.addAttribute("signInForm", new SignInForm());

		return ViewConstants.VIEW_SIGNIN;
	}

	/**
	 * Show profile.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile")
	public String showProfile(final Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			UserDTO userDTO = createUserDTO(user, user);

			model.addAttribute(AccountViewConstants.USER, userDTO);

			return ViewConstants.VIEW_PROFILE;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.VIEW_SIGNIN;
		}
	}

	/**
	 * Show user profile by id.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/{id}")
	public String showUserProfile(@PathVariable(value = "id") Integer id, final Model model) {
		try {
			model.addAttribute(AccountViewConstants.RATE_FORM, new RateForm());

			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);
			DefaultUserEntity user = userService.findById(id);

			UserDTO userDTO = createUserDTO(user, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			return ViewConstants.VIEW_PROFILE;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}

	}

	/**
	 * Follow user.
	 *
	 * @param id      the id
	 * @param model   the model
	 * @param request the request
	 * @return the string
	 */
	@GetMapping(path = "/follow/{id}")
	public String followUser(@PathVariable(value = "id") Integer id, Model model, HttpServletRequest request) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			DefaultUserEntity userToFollow = userService.findById(id);

			userService.followUser(user, userToFollow);

			String previousPage = request.getHeader(referer);

			return redirect + previousPage;

		} catch (UserNotFoundException | UserToFollowExistsException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Unfollow user.
	 *
	 * @param id      the id
	 * @param model   the model
	 * @param request the request
	 * @return the string
	 */
	@GetMapping(path = "/unfollow/{id}")
	public String unfollowUser(@PathVariable(value = "id") Integer id, Model model, HttpServletRequest request) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			DefaultUserEntity userToFollow = userService.findById(id);

			userService.unfollowUser(user, userToFollow);

			String previousPage = request.getHeader(referer);

			return redirect + previousPage;

		} catch (UserNotFoundException | UserToUnfollowNotFoundException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show user followers by id.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/followers/{id}")
	public String showUserFollowers(@PathVariable(value = "id") Integer id, Model model) {

		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);
			DefaultUserEntity user = userService.findById(id);

			UserDTO userDTO = createUserDTO(user, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			List<UserDTO> usersDTOList = createUserDTOList(user.getFollowers(), userLogged);
			model.addAttribute(AccountViewConstants.FOLLOWLIST, usersDTOList);

			model.addAttribute(AccountViewConstants.FOLLOW_NAME, AccountViewConstants.FOLLOWERS);
			model.addAttribute(AccountViewConstants.RATE_FORM, new RateForm());

			return ViewConstants.VIEW_FOLLOW_LIST;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show user followed by user id.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/followed/{id}")
	public String showUserFollowed(@PathVariable(value = "id") Integer id, Model model) {

		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);
			DefaultUserEntity user = userService.findById(id);

			UserDTO userDTO = createUserDTO(user, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			List<UserDTO> usersDTOList = createUserDTOList(user.getFollowed(), userLogged);
			model.addAttribute(AccountViewConstants.FOLLOWLIST, usersDTOList);

			model.addAttribute(AccountViewConstants.FOLLOW_NAME, AccountViewConstants.FOLLOWED);
			model.addAttribute(AccountViewConstants.RATE_FORM, new RateForm());

			return ViewConstants.VIEW_FOLLOW_LIST;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show user followers by id.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/{id}/advertisements")
	public String showUserAdvertisements(@PathVariable(value = "id") Integer id, Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);
			DefaultUserEntity user = userService.findById(id);

			UserDTO userDTO = createUserDTO(user, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			List<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisementsDtoList = createSaleAdvertisementDTOList(
					user.getSaleAdvertisements(), userLogged);
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_LIST, saleAdvertisementsDtoList);

			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_VIEW, AccountViewConstants.USER_ADVERTISEMENTS);
			model.addAttribute(AccountViewConstants.RATE_FORM, new RateForm());

			return ViewConstants.USER_ADVERTISEMENTS_LIKES_LIST;

		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show user followed by user id.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/{id}/likes")
	public String showUserLikes(@PathVariable(value = "id") Integer id, Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);
			DefaultUserEntity user = userService.findById(id);

			UserDTO userDTO = createUserDTO(user, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			List<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisementsDtoList = createSaleAdvertisementDTOList(
					user.getLikes(), userLogged);
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_LIST, saleAdvertisementsDtoList);

			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_VIEW, AccountViewConstants.USER_LIKES);
			model.addAttribute(AccountViewConstants.RATE_FORM, new RateForm());

			return ViewConstants.USER_ADVERTISEMENTS_LIKES_LIST;

		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Become premium. Change the role from user to premium user or from premium
	 * user to user.
	 *
	 * @param model   the model
	 * @param request the request
	 * @return the string
	 */
	@PostMapping(path = "/premium")
	public String becomePremium(Model model, HttpServletRequest request) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			userService.premiumUser(user);

			UserDTO userDTO = createUserDTO(user, user);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			return ViewConstants.VIEW_PROFILE;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Rate user.
	 *
	 * @param id            the id
	 * @param rateForm      the rate form
	 * @param bindingResult the binding result
	 * @param model         the model
	 * @param request       the request
	 * @return the string
	 */
	@PostMapping(path = "/rate/{id}")
	public String rateUser(@PathVariable(value = "id") Integer id, @ModelAttribute("rateForm") RateForm rateForm,
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		try {
			String previousPage = request.getHeader(referer);

			if (bindingResult.hasErrors())
				return redirect + previousPage;

			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			DefaultUserEntity userToRate = userService.findById(id);

			userService.rateUser(user, userToRate, rateForm.getRatingValue());

			return redirect + previousPage;

		} catch (UserNotFoundException | UserAlreadyGiveRatingToUserToRate | LowRatingException
				| HighRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show purchase history.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/profile/history")
	public String showPurchaseHistory(Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);

			UserDTO userDTO = createUserDTO(userLogged, userLogged);
			model.addAttribute(AccountViewConstants.USER, userDTO);

			List<BuyTransactionDTO> buyTransactionDTOList = createBuyTransactionDTOList(userLogged.getBuyTransactions(),
					userLogged);
			model.addAttribute(AccountViewConstants.USER_TRANSACTION_LIST, buyTransactionDTOList);

			return ViewConstants.USER_PURCHASE_HISTORY;

		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Check if the user name and email already exits
	 * 
	 * @param signUpForm the sign up form
	 * @param model      the model
	 */
	private void checkUserNameAndEmail(SignUpForm signUpForm, Model model) {
		try {
			userService.findByLogin(signUpForm.getLogin());
			model.addAttribute(AccountViewConstants.USER_EXIST, AccountViewConstants.USER_EXIST);
		} catch (UserNotFoundException e) {
			// If the exception jump, the user name is not in use
		}

		try {
			userService.findByEmail(signUpForm.getEmail());
			model.addAttribute(AccountViewConstants.EMAIL_EXIST, AccountViewConstants.EMAIL_EXIST);
		} catch (UserEmailNotFoundException e) {
			// If the exception jump, the email is not in use
		}
	}

	/**
	 * Check if the email already exits
	 * 
	 * @param signUpForm the sign up form
	 * @param model      the model
	 */
	private void checkEmail(SignUpForm signUpForm, Model model) {
		try {
			userService.findByEmail(signUpForm.getEmail());
			model.addAttribute(AccountViewConstants.EMAIL_EXIST, AccountViewConstants.EMAIL_EXIST);
		} catch (UserEmailNotFoundException e) {
			// If the exception jump, the email is not in use
		}
	}

	/**
	 * Creates a new user DTO.
	 *
	 * @param user       the user
	 * @param userLogged the user logged
	 * @return the user DTO
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private UserDTO createUserDTO(DefaultUserEntity user, DefaultUserEntity userLogged)
			throws UserNotFoundException, UserNoRatingException {
		boolean isUserLogged;
		boolean isFollowedByUserLogged;
		boolean isRatedByUserLogged;
		Integer rateByUserLogged = null;
		boolean existsAverageRating;
		Double averageRating = null;

		if (user.equals(userLogged)) {
			isUserLogged = true;
			isFollowedByUserLogged = false;
			isRatedByUserLogged = false;
		} else {
			isUserLogged = false;
			isFollowedByUserLogged = userLogged.getFollowed().contains(user);
			isRatedByUserLogged = userService.existsRatingFromUserToRateUser(userLogged, user);
		}

		if (isRatedByUserLogged)
			rateByUserLogged = userService.givenRatingFromUserToRatedUser(userLogged, user);

		existsAverageRating = userService.existsRatingForUser(user);

		if (existsAverageRating)
			averageRating = userService.averageRating(user);

		return new UserDTO(user, isUserLogged, isFollowedByUserLogged, isRatedByUserLogged, rateByUserLogged,
				existsAverageRating, averageRating);
	}

	/**
	 * Creates a new user DTO list.
	 *
	 * @param usersList  the users list
	 * @param userLogged the user logged
	 * @return the list
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private List<UserDTO> createUserDTOList(Set<DefaultUserEntity> usersList, DefaultUserEntity userLogged)
			throws UserNotFoundException, UserNoRatingException {
		List<UserDTO> usersDTOList = new ArrayList<>();

		for (DefaultUserEntity user : usersList) {
			usersDTOList.add(createUserDTO(user, userLogged));
		}

		return usersDTOList;
	}

	/**
	 * Creates a new sale advertisement DTO.
	 *
	 * @param saleAdvertisement the sale advertisement
	 * @param userLogged        the user logged
	 * @return the sale advertisement with logged user info DTO
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private SaleAdvertisementWithLoggedUserInfoDTO createSaleAdvertisementDTO(
			DefaultSaleAdvertisementEntity saleAdvertisement, DefaultUserEntity userLogged)
			throws UserNotFoundException, UserNoRatingException {
		boolean isRated = userService.existsRatingForUser(saleAdvertisement.getUser());
		Double averageRating;
		if (isRated)
			averageRating = userService.averageRating(saleAdvertisement.getUser());
		else
			averageRating = null;

		return new SaleAdvertisementWithLoggedUserInfoDTO(saleAdvertisement,
				userLogged.getLikes().contains(saleAdvertisement),
				userLogged.getFollowed().contains(saleAdvertisement.getUser()), isRated, averageRating,
				saleAdvertisement.getBuyTransaction() != null, saleAdvertisement.getUser().equals(userLogged));

	}

	/**
	 * Creates a new sale advertisement DTO list.
	 *
	 * @param saleAdvertisementsList the sale advertisements list
	 * @param userLogged             the user logged
	 * @return the list
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private List<SaleAdvertisementWithLoggedUserInfoDTO> createSaleAdvertisementDTOList(
			Set<DefaultSaleAdvertisementEntity> saleAdvertisementsList, DefaultUserEntity userLogged)
			throws UserNotFoundException, UserNoRatingException {
		List<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisementsListDtoList = new ArrayList<>();

		for (DefaultSaleAdvertisementEntity saleAdvertisement : saleAdvertisementsList) {
			saleAdvertisementsListDtoList.add(createSaleAdvertisementDTO(saleAdvertisement, userLogged));
		}

		return saleAdvertisementsListDtoList;
	}

	/**
	 * Creates the buy transaction DTO list.
	 *
	 * @param buyTransactionList the buy transaction list
	 * @param userLogged         the user logged
	 * @return the list
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private List<BuyTransactionDTO> createBuyTransactionDTOList(Set<DefaultBuyTransactionEntity> buyTransactionList,
			DefaultUserEntity userLogged) throws UserNotFoundException, UserNoRatingException {
		List<BuyTransactionDTO> buyTransactionDtoList = new ArrayList<>();
		for (DefaultBuyTransactionEntity buyTransaction : buyTransactionList) {
			buyTransactionDtoList.add(new BuyTransactionDTO(buyTransaction.getCreatedDate(),
					createSaleAdvertisementDTO(buyTransaction.getSaleAdvertisement(), userLogged)));
		}
		return buyTransactionDtoList;
	}
}
