package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.controller.account.AccountViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;

@Controller
@RequestMapping("/saleAdvertisement")
public class SaleAdvertisementListViewController {

	/** The sale advertisement service. */
	private final SaleAdvertisementService saleAdvertisementService;

	/** The image service. */
	private ImageService imageService;

	/**
	 * The User service.
	 */
	private UserService userService;

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
	 * @param imageService             the image service
	 * @param userService              the user service
	 * @param securityService          the security service
	 * @param userService              the user service
	 */
	@Autowired
	public SaleAdvertisementListViewController(final SaleAdvertisementService saleAdvertisementService,
			final ServletContext context, final ImageService imageService, final UserService userService,
			final SecurityService securityService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
		this.imageService = checkNotNull(imageService, ViewConstants.NULL_POINTER);
		this.userService = checkNotNull(userService, ViewConstants.NULL_POINTER);
		this.securityService = checkNotNull(securityService, ViewConstants.NULL_POINTER);
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

			model.addAttribute(ViewConstants.USER_ID, user.getId());

			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findById(id);

			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT, saleAdvertisement);

			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
		} catch (SaleAdvertisementNotFoundException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
		} catch (UserNotFoundException e) {
			return ViewConstants.WELCOME;
		}
	}

	/**
	 * Show sale add list.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/list")
	public String showSaleAdvertisementList(final ModelMap model, @RequestParam(required = false) String city,
			@RequestParam(required = false) String keywords, @RequestParam(required = false) String minDate,
			@RequestParam(required = false) String maxDate, @RequestParam(required = false) BigDecimal minPrice,
			@RequestParam(required = false) BigDecimal maxPrice) {
		try {
			String username = this.securityService.findLoggedInUsername();
			DefaultUserEntity user;
			user = userService.findByLogin(username);
			model.addAttribute(AccountViewConstants.USER, user);
		    loadViewModel(model, city, keywords, minDate, maxDate, minPrice, maxPrice);
			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;
		} catch (UserNotFoundException e) {
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

	private final void loadViewModel(final ModelMap model, String city, String keywords, String minDate, String maxDate,
			BigDecimal minPrice, BigDecimal maxPrice) {

		LocalDate minimumDate;
		LocalDate maximumDate;

		if (city == null || city.isEmpty())
			city = "%";
		else if (city.contains("%"))
			keywords = "\\%";
		if (city == null || city.isEmpty())
			city = "%";
		if (keywords == null)
			keywords = "";
		if (keywords.contains("%"))
			keywords = "\\%";
		if (minPrice == null)
			minPrice = BigDecimal.valueOf(0);
		if (maxPrice == null)
			maxPrice = saleAdvertisementService.getMaximumPrice();
		if (minDate == null || minDate.isEmpty())
			minimumDate = LocalDate.MIN;
		else
			minimumDate = LocalDate.parse(minDate, DateTimeFormatter.ISO_LOCAL_DATE);

		if (maxDate == null || maxDate.isEmpty())
			maximumDate = LocalDate.now();
		else
			maximumDate = LocalDate.parse(maxDate, DateTimeFormatter.ISO_LOCAL_DATE);

		model.put(SaleAdvertisementViewConstants.PARAM_SALE_ADVERTISEMENTS,
				saleAdvertisementService.getSaleAdvertisementsBySearchCriteria(city, keywords,
						LocalDateTime.of(minimumDate, LocalTime.of(0, 0, 0)),
						LocalDateTime.of(maximumDate, LocalTime.of(23, 59, 59)), minPrice, maxPrice));
	}

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
	 * Delete the images.
	 *
	 * @param images the images
	 */
	private void deleteImages(Set<DefaultImageEntity> images) {
		for (DefaultImageEntity image : images) {
			File file = new File(context.getRealPath("/"), image.getImagePath());

			file.delete();
		}

	}
}