package es.udc.fi.dc.fd.controller.account;

import static com.google.common.base.Preconditions.checkNotNull;

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
import es.udc.fi.dc.fd.model.form.account.SignInForm;
import es.udc.fi.dc.fd.model.form.account.SignUpForm;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.AlreadyPremiumUserException;
import es.udc.fi.dc.fd.service.user.exceptions.NotLoggedUserException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
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

		String username;
		DefaultUserEntity user;
		try {
			username = this.securityService.findLoggedInUsername();
			user = userService.findByLogin(username);

			model.addAttribute(AccountViewConstants.USER_LOGGED, user);
			model.addAttribute(AccountViewConstants.USER, user);

		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}

		return ViewConstants.VIEW_PROFILE;
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

		DefaultUserEntity user;
		try {

			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity userLogged = userService.findByLogin(username);

			model.addAttribute(AccountViewConstants.USER_LOGGED, userLogged);

			user = userService.findById(id);

			model.addAttribute(AccountViewConstants.USER, user);

		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}

		return ViewConstants.VIEW_PROFILE;
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
	 * Follow user.
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

			model.addAttribute(AccountViewConstants.USER_LOGGED, userLogged);

			DefaultUserEntity user;
			user = userService.findById(id);
			model.addAttribute(AccountViewConstants.USER, user);
			model.addAttribute(AccountViewConstants.FOLLOWLIST, user.getFollowers());
			model.addAttribute(AccountViewConstants.FOLLOW_NAME, AccountViewConstants.FOLLOWERS);
			return ViewConstants.VIEW_FOLLOW_LIST;
		} catch (UserNotFoundException e) {
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

			model.addAttribute(AccountViewConstants.USER_LOGGED, userLogged);

			DefaultUserEntity user;
			user = userService.findById(id);
			model.addAttribute(AccountViewConstants.USER, user);
			model.addAttribute(AccountViewConstants.FOLLOWLIST, user.getFollowed());
			model.addAttribute(AccountViewConstants.FOLLOW_NAME, AccountViewConstants.FOLLOWED);
			return ViewConstants.VIEW_FOLLOW_LIST;
		} catch (UserNotFoundException e) {
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

			model.addAttribute(AccountViewConstants.USER_LOGGED, userLogged);

			DefaultUserEntity user;
			user = userService.findById(id);
			model.addAttribute(AccountViewConstants.USER, user);
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_LIST, user.getSaleAdvertisements());
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_VIEW, AccountViewConstants.USER_ADVERTISEMENTS);
			return ViewConstants.USER_ADVERTISEMENTS_LIKES_LIST;
		} catch (UserNotFoundException e) {
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

			model.addAttribute(AccountViewConstants.USER_LOGGED, userLogged);

			DefaultUserEntity user;
			user = userService.findById(id);
			model.addAttribute(AccountViewConstants.USER, user);
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_LIST, user.getLikes());
			model.addAttribute(AccountViewConstants.USER_ADVERTISEMENTS_VIEW, AccountViewConstants.USER_LIKES);
			return ViewConstants.USER_ADVERTISEMENTS_LIKES_LIST;
		} catch (UserNotFoundException e) {
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

	@GetMapping(path = "/premium/{id}")
	public String becomePremium(@PathVariable(value = "id") Integer id, Model model, HttpServletRequest request) {

		try {

			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			model.addAttribute(AccountViewConstants.USER_LOGGED, user);

			userService.premiumUser(user, id);
			model.addAttribute(AccountViewConstants.USER, user);
			return ViewConstants.VIEW_PROFILE;
		} catch (UserNotFoundException | AlreadyPremiumUserException | NotLoggedUserException e) {
			return ViewConstants.WELCOME;
		}
	}

}
