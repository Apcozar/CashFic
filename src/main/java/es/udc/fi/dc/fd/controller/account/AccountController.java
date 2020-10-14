package es.udc.fi.dc.fd.controller.account;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.form.account.SignInForm;
import es.udc.fi.dc.fd.model.form.account.SignUpForm;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.EmailNotFoundException;
import es.udc.fi.dc.fd.service.user.exceptions.UserEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginAndEmailExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserLoginExistsException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

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
	 * Sign in.
	 *
	 * @param signInForm    the sign in form
	 * @param bindingResult the binding result
	 * @param model         the model
	 * @return the string
	 */
	@PostMapping(path = "/signIn")
	public String signIn(@Valid @ModelAttribute("signInForm") SignInForm signInForm, BindingResult bindingResult,
			Model model) {

		try {
			if (bindingResult.hasErrors()) {
				return ViewConstants.VIEW_SIGNIN;
			}
			this.userService.login(signInForm.getLogin(), signInForm.getPassword());

		} catch (Exception e) {
			model.addAttribute(AccountViewConstants.ERROR_LOGIN, AccountViewConstants.ERROR_LOGIN);
			return ViewConstants.VIEW_SIGNIN;
		}

		this.securityService.autologin(signInForm.getLogin(), signInForm.getPassword());
		return "redirect:/";
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

			model.addAttribute(AccountViewConstants.USER, user);

		} catch (UserNotFoundException e) {
			return ViewConstants.VIEW_SIGNIN;
		}

		return ViewConstants.VIEW_PROFILE;

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
		} catch (EmailNotFoundException e) {
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
		} catch (EmailNotFoundException e) {
			// If the exception jump, the email is not in use
		}
	}

}
