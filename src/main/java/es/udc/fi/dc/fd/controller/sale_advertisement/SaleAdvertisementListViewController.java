package es.udc.fi.dc.fd.controller.sale_advertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementViewConstants;
import es.udc.fi.dc.fd.model.Role;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.State;
import es.udc.fi.dc.fd.model.UserEntity;
import es.udc.fi.dc.fd.model.dto.ImageDTO;
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

			DefaultSaleAdvertisementEntity saleAdvertisement = (DefaultSaleAdvertisementEntity) saleAdvertisementService
					.findById(id);

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
	 * @param searchCriteriaForm the search criteria form
	 * @param request            the request
	 * @return the string
	 */
	@GetMapping(path = "/list")
	public String showSaleAdvertisementList(final ModelMap model,
			@ModelAttribute("searchCriteriaForm") SearchCriteriaForm searchCriteriaForm, HttpServletRequest request) {
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

			loadViewModel(model, searchCriteriaForm, request, username);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;

		} catch (UserNotFoundException | IOException e) {
			return ViewConstants.WELCOME;
		}
	}

	@GetMapping(path = "/followedList")
	public String showFollowedSaleAdvertisementList(final ModelMap model,
			@ModelAttribute("searchCriteriaForm") SearchCriteriaForm searchCriteriaForm, HttpServletRequest request) {
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

			loadViewModelFollow(model, searchCriteriaForm, request, user);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;

		} catch (UserNotFoundException | IOException e) {
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
	 * @throws IOException
	 */

	private final void loadViewModel(final ModelMap model, SearchCriteriaForm form, HttpServletRequest request,
			String username) throws IOException {

		Set<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisements = apiCall(form, request, username);

		model.put(SaleAdvertisementViewConstants.PARAM_SALE_ADVERTISEMENTS, saleAdvertisements);

	}

	/**
	 * Load view model follow.
	 *
	 * @param model the model
	 * @param form  the form
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 * @throws IOException
	 */
	private final void loadViewModelFollow(final ModelMap model, SearchCriteriaForm form, HttpServletRequest request,
			UserEntity user) throws IOException {

		Set<SaleAdvertisementWithLoggedUserInfoDTO> unfiltered = apiCall(form, request, user.getLogin());

		List<SaleAdvertisementWithLoggedUserInfoDTO> filtered = new ArrayList<>();

		for (SaleAdvertisementWithLoggedUserInfoDTO saleAdvertisement : unfiltered) {

			user.getFollowed().forEach(u -> {
				if (u.getId().equals(saleAdvertisement.getOwnerUserId()))
					filtered.add(saleAdvertisement);
			});

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

	private String getApiSearchPath(HttpServletRequest request, SearchCriteriaForm searchCriteriaFrom,
			String username) {
		String apiPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ context.getContextPath() + SaleAdvertisementViewConstants.FIND_API_REST;

		apiPath += "?userName=" + username;

		if ((searchCriteriaFrom.getCity() != null) && (StringUtils.isNotBlank(searchCriteriaFrom.getCity()))) {
			apiPath += "&city=" + searchCriteriaFrom.getCity();
		}
		if ((searchCriteriaFrom.getKeywords() != null) && (StringUtils.isNotBlank(searchCriteriaFrom.getKeywords()))) {
			apiPath += "&keywords=" + searchCriteriaFrom.getKeywords();
		}

		if ((searchCriteriaFrom.getMinPrice() != null)
				&& (StringUtils.isNotBlank(searchCriteriaFrom.getMinPrice().toString()))) {
			apiPath += "&minPrice=" + searchCriteriaFrom.getMinPrice().toString();
		}

		if ((searchCriteriaFrom.getMaxPrice() != null)
				&& (StringUtils.isNotBlank(searchCriteriaFrom.getMaxPrice().toString()))) {
			apiPath += "&maxPrice=" + searchCriteriaFrom.getMaxPrice().toString();
		}

		if ((searchCriteriaFrom.getMinDate() != null) && (StringUtils.isNotBlank(searchCriteriaFrom.getMinDate()))) {
			apiPath += "&minDate=" + searchCriteriaFrom.getMinDate();
		}

		if ((searchCriteriaFrom.getMaxDate() != null) && (StringUtils.isNotBlank(searchCriteriaFrom.getMaxDate()))) {
			apiPath += "&maxDate=" + searchCriteriaFrom.getMaxDate();
		}

		if ((searchCriteriaFrom.getMinRating() != null)
				&& (StringUtils.isNotBlank(searchCriteriaFrom.getMinRating().toString()))) {
			apiPath += "&minRating=" + searchCriteriaFrom.getMinRating().toString();

		}
		return apiPath;
	}

	private void parseStringToDto(List<String> saleAdvertisementsString,
			Set<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisementsDto) {

		int i;

		List<ImageDTO> imagesDto;
		DefaultUserEntity user;
		SaleAdvertisementEntity saleAdvertisement;
		SaleAdvertisementWithLoggedUserInfoDTO saleAdvertisementDto;

		String images;
		String imagePath;
		String productTitle;
		String productDescription;
		String ownerUserLogin;
		String state;
		String ownerUserId;
		String role;
		String year;
		String monthValue;
		String dayOfMonth;
		String hour;
		String minute;
		String second;
		String price;
		String saleAdvertisementID;
		String saleAdvertisementLikesCount;
		String userLikeSaleAdvertisement;
		String city;
		String loggedUserFollowsSaleAdvertisementUser;
		String areUserRated;
		String averageRating;
		String saleAdvertisementIsSold;
		String userLogged;

		Double avgRating = null;

		for (String x : saleAdvertisementsString) {
			images = x.substring(x.indexOf('['), x.indexOf(']') + 1);
			x = x.substring(x.indexOf(']') + 1);

			x = x.substring(x.indexOf(":") + 1);
			productTitle = x.substring(1, x.indexOf(',') - 1);

			x = x.substring(x.indexOf(":") + 1);
			productDescription = x.substring(1, x.indexOf(',') - 1);

			x = x.substring(x.indexOf(":") + 1);
			ownerUserLogin = x.substring(1, x.indexOf(',') - 1);

			x = x.substring(x.indexOf(":") + 1);
			state = x.substring(1, x.indexOf(',') - 1);

			x = x.substring(x.indexOf(":") + 1);
			ownerUserId = x.substring(0, x.indexOf(','));

			x = x.substring(x.indexOf(":") + 1);
			role = x.substring(0, x.indexOf(','));

			x = x.substring(x.indexOf("\"year\":"));
			year = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"monthValue\":"));
			monthValue = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"dayOfMonth\":"));
			dayOfMonth = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"hour\":"));
			hour = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"minute\":"));
			minute = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"second\":"));
			second = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"price\":"));
			price = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"saleAdvertisementID\":"));
			saleAdvertisementID = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"saleAdvertisementLikesCount\":"));
			saleAdvertisementLikesCount = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"userLikeSaleAdvertisement\":"));
			userLikeSaleAdvertisement = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"city\":"));
			city = x.substring(x.indexOf(":") + 2, x.indexOf(',') - 1);

			x = x.substring(x.indexOf("\"loggedUserFollowsSaleAdvertisementUser\":"));
			loggedUserFollowsSaleAdvertisementUser = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"areUserRated\":"));
			areUserRated = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"averageRating\":"));
			averageRating = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"saleAdvertisementIsSold\":"));
			saleAdvertisementIsSold = x.substring(x.indexOf(":") + 1, x.indexOf(','));

			x = x.substring(x.indexOf("\"userLogged\":"));
			userLogged = x.substring(x.indexOf(":") + 1);

			imagesDto = new ArrayList<>();

			if (images.length() > 2) {

				for (i = 0; i < images.length(); i++) {

					if (images.charAt(i) == '}') {
						imagePath = images.substring(images.indexOf(':') + 2, i - 1);
						images = images.substring(i + 1);
						imagesDto.add(new ImageDTO(imagePath));
						i = 0;
					}
				}
			}

			user = new DefaultUserEntity();
			user.setId(Integer.parseInt(ownerUserId));
			user.setLogin(ownerUserLogin);
			user.setCity(city);
			user.setRole(Role.values()[Integer.parseInt(role)]);

			saleAdvertisement = new DefaultSaleAdvertisementEntity(Integer.parseInt(saleAdvertisementID), productTitle,
					productDescription, user);
			saleAdvertisement.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
			saleAdvertisement.setState(State.valueOf(state));
			saleAdvertisement.setDate(
					LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(monthValue), Integer.parseInt(dayOfMonth),
							Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second)));

			if (!averageRating.equals("null")) {
				avgRating = Double.parseDouble(averageRating);
			}

			saleAdvertisementDto = new SaleAdvertisementWithLoggedUserInfoDTO(saleAdvertisement,
					Boolean.parseBoolean(userLikeSaleAdvertisement),
					Boolean.parseBoolean(loggedUserFollowsSaleAdvertisementUser), Boolean.parseBoolean(areUserRated),
					avgRating, Boolean.parseBoolean(saleAdvertisementIsSold), Boolean.parseBoolean(userLogged));
			saleAdvertisementDto.setSaleAdvertisementLikesCount(Integer.parseInt(saleAdvertisementLikesCount));
			saleAdvertisementDto.setImages(imagesDto);

			saleAdvertisementsDto.add(saleAdvertisementDto);
		}
	}

	private Set<SaleAdvertisementWithLoggedUserInfoDTO> apiCall(SearchCriteriaForm form, HttpServletRequest request,
			String username) throws IOException {

		int i;

		Set<SaleAdvertisementWithLoggedUserInfoDTO> saleAdvertisements = new LinkedHashSet<>();
		URL url;
		HttpURLConnection connection;

		String apiURL = getApiSearchPath(request, form, username);
		url = new URL(apiURL);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder content = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();

		String responseString = content.toString().substring(1, content.length() - 1);
		List<String> saleAdvertisementsString = new ArrayList<>();

		int count = 0;
		for (i = 0; i < responseString.length(); i++) {
			if (responseString.charAt(i) == '{')
				count += 1;
			if (responseString.charAt(i) == '}')
				count -= 1;

			if (count == 0) {
				saleAdvertisementsString.add(responseString.substring(responseString.indexOf('{') + 1, i));
				responseString = responseString.substring(i + 1);
				i = 0;
			}

		}

		parseStringToDto(saleAdvertisementsString, saleAdvertisements);

		return saleAdvertisements;
	}

}
