package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.controller.account.AccountViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.State;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.dto.SaleAdvertisementWithLoggedUserInfoDTO;
import es.udc.fi.dc.fd.model.form.SearchCriteriaForm;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.BuyTransactionService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.BuyTransactionAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnHoldException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyOnSaleException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNoRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@Controller
@RequestMapping("/saleAdvertisement")
public class SaleAdvertisementListViewController {

	/** The sale advertisement service. */
	private final SaleAdvertisementService saleAdvertisementService;

	/**
	 * The User service.
	 */
	private UserService userService;

	/**
	 * The buyTransactionService service.
	 */
	private BuyTransactionService buyTransactionService;

	/** The context. */
	private ServletContext context;

	/**
	 * The Security service.
	 */
	private SecurityService securityService;

	/**
	 * Instantiates a new sale advertisement list view controller.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 * @param context                  the context
	 * @param userService              the user service
	 * @param securityService          the security service
	 * @param buyTransactionService    the buy transaction service
	 */
	@Autowired
	public SaleAdvertisementListViewController(final SaleAdvertisementService saleAdvertisementService,
			final ServletContext context, final UserService userService, final SecurityService securityService,
			final BuyTransactionService buyTransactionService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);
		this.securityService = checkNotNull(securityService, ViewConstants.NULL_POINTER);
		this.buyTransactionService = checkNotNull(buyTransactionService, ViewConstants.NULL_POINTER);
		this.context = checkNotNull(context, ViewConstants.NULL_POINTER);
	}

	/**
	 * Show the sale advertisement info.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/{id}")
	public String showSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			DefaultSaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findByIdDefault(id);

			SaleAdvertisementWithLoggedUserInfoDTO saleAdvertisementDto = createSaleAdvertisementDTO(saleAdvertisement,
					user);
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT, saleAdvertisementDto);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;

		} catch (SaleAdvertisementNotFoundException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show sale add list.
	 *
	 * @param model              the model
	 * @param searchCriteriaFrom the search criteria from
	 * @return the string
	 */
	@GetMapping(path = "/list")
	public String showSaleAdvertisementList(final ModelMap model,
			@ModelAttribute("searchCriteriaForm") SearchCriteriaForm searchCriteriaFrom) {
		try {
			String username;
			DefaultUserEntity user;
			Boolean isRated;

			username = this.securityService.findLoggedInUsername();
			user = userService.findByLogin(username);
			isRated = userService.existsRatingForUser(user);

			model.addAttribute(SaleAdvertisementViewConstants.SEARCH_CRITERIA_FORM, new SearchCriteriaForm());
			model.addAttribute(SaleAdvertisementViewConstants.VIEW_NAME, SaleAdvertisementViewConstants.VIEW_LIST);
			model.addAttribute(AccountViewConstants.IS_RATED, isRated);

			loadViewModel(model, searchCriteriaFrom, user);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;

		} catch (UserNotFoundException | UserNoRatingException e) {
			return ViewConstants.WELCOME;
		}
	}

	@GetMapping(path = "/followedList")
	public String showFollowedSaleAdvertisementList(final ModelMap model,
			@ModelAttribute("searchCriteriaForm") SearchCriteriaForm searchCriteriaFrom) {
		try {
			String username;
			DefaultUserEntity user;
			Boolean isRated;

			username = this.securityService.findLoggedInUsername();
			user = userService.findByLogin(username);
			isRated = userService.existsRatingForUser(user);

			model.addAttribute(SaleAdvertisementViewConstants.SEARCH_CRITERIA_FORM, new SearchCriteriaForm());
			model.addAttribute(SaleAdvertisementViewConstants.VIEW_NAME,
					SaleAdvertisementViewConstants.VIEW_FILTERED_LIST);
			model.addAttribute(AccountViewConstants.IS_RATED, isRated);

			loadViewModelFollow(model, searchCriteriaFrom, user);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;

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
	@GetMapping(path = "/like/{id}")
	public String likeSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model,
			HttpServletRequest request) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			SaleAdvertisementEntity saleAdvertisementToLike = saleAdvertisementService.findById(id);

			userService.like(user, saleAdvertisementToLike);

			String previousPage = request.getHeader(ViewConstants.REFERER);

			return ViewConstants.REDIRECT + previousPage;

		} catch (UserNotFoundException | SaleAdvertisementNotFoundException e) {
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
	@GetMapping(path = "/unlike/{id}")
	public String unlikeSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model,
			HttpServletRequest request) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			SaleAdvertisementEntity saleAdvertisementToLike = saleAdvertisementService.findById(id);

			userService.unlike(user, saleAdvertisementToLike);

			String previousPage = request.getHeader(ViewConstants.REFERER);

			return ViewConstants.REDIRECT + previousPage;

		} catch (UserNotFoundException | SaleAdvertisementNotFoundException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Removes the sale advertisement and its images.
	 *
	 * @param id    the id
	 * @param model the model
	 * @return the string
	 */

	@PostMapping(path = "/remove/{id}")
	public String removeSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model) {
		try {
			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findById(id);
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			if (!saleAdvertisement.getUser().getId().equals(user.getId())) {
				return ViewConstants.WELCOME;
			}

			deleteImages(saleAdvertisement.getImages());

			Object[] likes = saleAdvertisement.getLikes().toArray();
			for (int i = 0; i < likes.length; i++) {
				userService.unlike((UserEntity) likes[i], saleAdvertisement);
			}

			saleAdvertisementService.remove((DefaultSaleAdvertisementEntity) saleAdvertisement);

			model.addAttribute("addSaleAdvertisementRemove", "addSaleAdvertisementRemove");

			return ViewConstants.WELCOME;

		} catch (SaleAdvertisementNotFoundException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
		} catch (UserNotFoundException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Sets the on hold sale advertisement.
	 *
	 * @param id      the id
	 * @param model   the model
	 * @param request the request
	 * @return the string
	 */
	@PostMapping(path = "/setOnHold/{id}")
	public String setOnHoldSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model,
			HttpServletRequest request) {

		try {
			SaleAdvertisementEntity saleAdvertisement;
			saleAdvertisement = saleAdvertisementService.findById(id);
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			if (!saleAdvertisement.getUser().getId().equals(user.getId())) {
				return ViewConstants.WELCOME;
			}

			saleAdvertisementService.setOnHoldAdvertisement(saleAdvertisement.getId());

			String previousPage = request.getHeader(ViewConstants.REFERER);

			return ViewConstants.REDIRECT + previousPage;

		} catch (SaleAdvertisementNotFoundException | UserNotFoundException
				| SaleAdvertisementAlreadyOnHoldException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Sets the on sale sale advertisement.
	 *
	 * @param id      the id
	 * @param model   the model
	 * @param request the request
	 * @return the string
	 */
	@PostMapping(path = "/setOnSale/{id}")
	public String setOnSaleSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model,
			HttpServletRequest request) {

		try {
			SaleAdvertisementEntity saleAdvertisement;
			saleAdvertisement = saleAdvertisementService.findById(id);
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			if (!saleAdvertisement.getUser().getId().equals(user.getId())) {
				return ViewConstants.WELCOME;
			}

			saleAdvertisementService.setOnSaleAdvertisement(saleAdvertisement.getId());

			String previousPage = request.getHeader(ViewConstants.REFERER);

			return ViewConstants.REDIRECT + previousPage;

		} catch (SaleAdvertisementNotFoundException | UserNotFoundException
				| SaleAdvertisementAlreadyOnSaleException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Sets the on sale sale advertisement.
	 *
	 * @param saleAdvertisementId the saleAdvertisementId
	 * @param model               the model
	 * @param request             the request
	 * @return the string
	 */
	@PostMapping(path = "/buy/{saleAdvertisementId}")
	public String buySaleAdvertisement(@PathVariable(value = "saleAdvertisementId") Integer saleAdvertisementId,
			Model model, HttpServletRequest request) {

		try {
			SaleAdvertisementEntity saleAdvertisement;
			saleAdvertisement = saleAdvertisementService.findById(saleAdvertisementId);
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user = userService.findByLogin(username);

			if (saleAdvertisement.getUser().getId().equals(user.getId())) {
				return ViewConstants.WELCOME;
			}

			if (saleAdvertisement.getState() == State.STATE_ON_HOLD) {

				return ViewConstants.WELCOME;
			}
			buyTransactionService.create(user, saleAdvertisement);

			String previousPage = request.getHeader(ViewConstants.REFERER);

			return ViewConstants.REDIRECT + previousPage;

		} catch (SaleAdvertisementNotFoundException | UserNotFoundException | BuyTransactionAlreadyExistsException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Load view model.
	 *
	 * @param model    the model
	 * @param city     the city
	 * @param keywords the keywords
	 * @param minDate  the min date
	 * @param maxDate  the max date
	 * @param minPrice the min price
	 * @param maxPrice the max price
	 * @throws UserNotFoundException
	 * @throws UserNoRatingException
	 */
	private final void loadViewModel(final ModelMap model, SearchCriteriaForm form, DefaultUserEntity user)
			throws UserNotFoundException, UserNoRatingException {

		LocalDate minimumDate;
		LocalDate maximumDate;

		checkSearchCriteriaForm(form);

		if (form.getMinDate() == null || form.getMinDate().isEmpty())
			minimumDate = LocalDate.of(1900, 1, 1);
		else
			minimumDate = LocalDate.parse(form.getMinDate(), DateTimeFormatter.ISO_LOCAL_DATE);

		if (form.getMaxDate() == null || form.getMaxDate().isEmpty())
			maximumDate = LocalDate.now();
		else
			maximumDate = LocalDate.parse(form.getMaxDate(), DateTimeFormatter.ISO_LOCAL_DATE);

		Iterable<DefaultSaleAdvertisementEntity> saleAdvertisementsList = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(form.getCity(), form.getKeywords(),
						LocalDateTime.of(minimumDate, LocalTime.of(0, 0, 0)),
						LocalDateTime.of(maximumDate, LocalTime.of(23, 59, 59)), form.getMinPrice(), form.getMaxPrice(),
						form.getMinRating());
		ArrayList<SaleAdvertisementWithLoggedUserInfoDTO> list = new ArrayList<>();

		for (DefaultSaleAdvertisementEntity saleAdvertisement : saleAdvertisementsList) {

			if ((saleAdvertisement.getBuyTransaction() == null) || !(saleAdvertisement.getBuyTransaction()
					.getCreatedDate().isBefore(LocalDateTime.now().minusDays(1)))) {

				list.add(createSaleAdvertisementDTO(saleAdvertisement, user));
			}
		}

		model.put(SaleAdvertisementViewConstants.PARAM_SALE_ADVERTISEMENTS, list);

	}

	/**
	 * Load view model follow.
	 *
	 * @param model the model
	 * @param form  the form
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	private final void loadViewModelFollow(final ModelMap model, SearchCriteriaForm form, DefaultUserEntity user)
			throws UserNotFoundException, UserNoRatingException {

		LocalDate minimumDate;
		LocalDate maximumDate;

		checkSearchCriteriaForm(form);

		if (form.getMinDate() == null || form.getMinDate().isEmpty())
			minimumDate = LocalDate.of(1900, 1, 1);
		else
			minimumDate = LocalDate.parse(form.getMinDate(), DateTimeFormatter.ISO_LOCAL_DATE);

		if (form.getMaxDate() == null || form.getMaxDate().isEmpty())
			maximumDate = LocalDate.now();
		else
			maximumDate = LocalDate.parse(form.getMaxDate(), DateTimeFormatter.ISO_LOCAL_DATE);

		Iterable<DefaultSaleAdvertisementEntity> unfiltered = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(form.getCity(), form.getKeywords(),
						LocalDateTime.of(minimumDate, LocalTime.of(0, 0, 0)),
						LocalDateTime.of(maximumDate, LocalTime.of(23, 59, 59)), form.getMinPrice(), form.getMaxPrice(),
						form.getMinRating());

		List<SaleAdvertisementWithLoggedUserInfoDTO> filtered = new ArrayList<>();

		for (DefaultSaleAdvertisementEntity saleAdvertisement : unfiltered) {

			if (((saleAdvertisement.getBuyTransaction() == null) || !(saleAdvertisement.getBuyTransaction()
					.getCreatedDate().isBefore(LocalDateTime.now().minusDays(1))))
					&& (user.getFollowed().contains(saleAdvertisement.getUser()))) {

				filtered.add(createSaleAdvertisementDTO(saleAdvertisement, user));
			}

		}

		model.put(SaleAdvertisementViewConstants.PARAM_SALE_ADVERTISEMENTS, filtered);

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
		Double averageRating = null;

		if (isRated)
			averageRating = userService.averageRating(saleAdvertisement.getUser());

		return new SaleAdvertisementWithLoggedUserInfoDTO(saleAdvertisement,
				userLogged.getLikes().contains(saleAdvertisement),
				userLogged.getFollowed().contains(saleAdvertisement.getUser()), isRated, averageRating,
				saleAdvertisement.getBuyTransaction() != null, saleAdvertisement.getUser().equals(userLogged));

	}

	/**
	 * Delete the images.
	 *
	 * @param images the images
	 * @return true, if successful removes all the images
	 */
	private boolean deleteImages(Set<DefaultImageEntity> images) {
		boolean result = true;
		for (DefaultImageEntity image : images) {
			try {
				Path path = Paths.get(context.getRealPath("/") + image.getImagePath());
				Files.delete(path);
			} catch (IOException e) {
				result = false;
			}
		}

		return result;
	}

	/**
	 * Check search criteria form.
	 *
	 * @param form the form
	 */
	private void checkSearchCriteriaForm(SearchCriteriaForm form) {

		if (form.getCity() == null || form.getCity().isEmpty())
			form.setCity("%");
		else if (form.getCity().contains("%"))
			form.setCity("\\%");

		if (form.getKeywords() == null)
			form.setKeywords("");

		if (form.getKeywords().contains("%"))
			form.setKeywords("\\%");

		if (form.getMinPrice() == null)
			form.setMinPrice(BigDecimal.valueOf(0));

		if (form.getMaxPrice() == null)
			form.setMaxPrice(saleAdvertisementService.getMaximumPrice());
	}

}
