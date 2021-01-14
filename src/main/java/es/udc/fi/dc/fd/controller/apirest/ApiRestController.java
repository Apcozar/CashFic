package es.udc.fi.dc.fd.controller.apirest;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.dto.SaleAdvertisementWithLoggedUserInfoDTO;
import es.udc.fi.dc.fd.model.form.SearchCriteriaForm;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNoRatingException;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@RestController
@RequestMapping("/api")
public class ApiRestController {

	/** The sale advertisement service. */
	private SaleAdvertisementService saleAdvertisementService;

	/** The user service. */
	private UserService userService;

	/**
	 * Instantiates a new api rest controller.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 * @param userService              the user service
	 * @param securityService          the security service
	 */
	@Autowired
	public ApiRestController(SaleAdvertisementService saleAdvertisementService, UserService userService,
			SecurityService securityService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);
	}

	/**
	 * Find.
	 *
	 * @param userName  the user name
	 * @param city      the city
	 * @param keywords  the keywords
	 * @param minDate   the min date
	 * @param maxDate   the max date
	 * @param minPrice  the min price
	 * @param maxPrice  the max price
	 * @param minRating the min rating
	 * @return the response entity
	 * @throws UserNotFoundException the user not found exception
	 * @throws UserNoRatingException the user no rating exception
	 */
	@GetMapping("/search")
	public ResponseEntity<List<SaleAdvertisementWithLoggedUserInfoDTO>> find(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "keywords", required = false) String keywords,
			@RequestParam(value = "minDate", required = false) String minDate,
			@RequestParam(value = "maxDate", required = false) String maxDate,
			@RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
			@RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
			@RequestParam(value = "minRating", required = false) Double minRating)
			throws UserNotFoundException, UserNoRatingException {

		LocalDate minimumDate;
		LocalDate maximumDate;

		DefaultUserEntity user = userService.findByLogin(userName);
		SearchCriteriaForm form = checkSearchCriteriaParams(city, keywords, minPrice, maxPrice);

		if (minDate == null || minDate.isEmpty())
			minimumDate = LocalDate.of(1900, 1, 1);
		else
			minimumDate = LocalDate.parse(minDate, DateTimeFormatter.ISO_LOCAL_DATE);

		if (maxDate == null || maxDate.isEmpty())
			maximumDate = LocalDate.now();
		else
			maximumDate = LocalDate.parse(maxDate, DateTimeFormatter.ISO_LOCAL_DATE);

		Iterable<DefaultSaleAdvertisementEntity> saleAdvertisementsList = saleAdvertisementService
				.getSaleAdvertisementsBySearchCriteria(form.getCity(), form.getKeywords(),
						LocalDateTime.of(minimumDate, LocalTime.of(0, 0, 0)),
						LocalDateTime.of(maximumDate, LocalTime.of(23, 59, 59)), form.getMinPrice(), form.getMaxPrice(),
						minRating);

		List<SaleAdvertisementWithLoggedUserInfoDTO> list = new ArrayList<>();

		for (DefaultSaleAdvertisementEntity saleAdvertisement : saleAdvertisementsList) {

			if ((saleAdvertisement.getBuyTransaction() == null) || !(saleAdvertisement.getBuyTransaction()
					.getCreatedDate().isBefore(LocalDateTime.now().minusDays(1)))) {

				list.add(createSaleAdvertisementDTO(saleAdvertisement, user));
			}
		}

		return ResponseEntity.ok().body(list);

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
	 * Check search criteria params.
	 *
	 * @param form the form
	 */
	private SearchCriteriaForm checkSearchCriteriaParams(String city, String keywords, BigDecimal minPrice,
			BigDecimal maxPrice) {

		SearchCriteriaForm form = new SearchCriteriaForm();

		if (city == null || city.isEmpty())
			form.setCity("%");
		else if (city.contains("%"))
			form.setCity("\\%");
		else
			form.setCity(city);

		if (keywords == null)
			form.setKeywords("");
		else if (keywords.contains("%"))
			form.setKeywords("\\%");
		else
			form.setKeywords(keywords);

		if (minPrice == null)
			form.setMinPrice(BigDecimal.valueOf(0));
		else
			form.setMinPrice(minPrice);

		if (maxPrice == null)
			form.setMaxPrice(saleAdvertisementService.getMaximumPrice());
		else
			form.setMaxPrice(maxPrice);

		return form;
	}
}
