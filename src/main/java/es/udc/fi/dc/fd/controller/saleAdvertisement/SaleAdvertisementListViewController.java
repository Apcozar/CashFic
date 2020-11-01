package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;

@Controller
@RequestMapping("/saleAdvertisement")
public class SaleAdvertisementListViewController {

	/** The sale advertisement service. */
	private final SaleAdvertisementService saleAdvertisementService;

	/** The image service. */
	private ImageService imageService;

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
	 * @param securityService          the security service
	 */
	@Autowired
	public SaleAdvertisementListViewController(final SaleAdvertisementService saleAdvertisementService,
			final ServletContext context, final ImageService imageService, final SecurityService securityService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
		this.imageService = checkNotNull(imageService, ViewConstants.NULL_POINTER);
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
			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findById(id);

			loadImageViewModel(model, saleAdvertisement);
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT, saleAdvertisement);

		} catch (SaleAdvertisementNotFoundException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
		}
		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT;
	}

	/**
	 * Show sale add list.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/list")
	public String showSaleAdvertisementList(final ModelMap model) {
		loadViewModel(model);

		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_LIST;
	}

	/**
	 * Load view model.
	 *
	 * @param model the model
	 */
	private final void loadViewModel(final ModelMap model) {
		model.put(SaleAdvertisementViewConstants.PARAM_SALE_ADVERTISEMENTS,
				saleAdvertisementService.getSaleAdvertisementsByDateDesc());
	}

	/**
	 * Load image view model.
	 *
	 * @param model             the model
	 * @param saleAdvertisement the sale advertisement
	 */
	private final void loadImageViewModel(final Model model, SaleAdvertisementEntity saleAdvertisement) {
		Set<DefaultImageEntity> imagesSet = saleAdvertisement.getImages();
		DefaultImageEntity first;
		List<DefaultImageEntity> images = new ArrayList<>();

		Iterator<DefaultImageEntity> iterator = imagesSet.iterator();

		if (iterator.hasNext()) {
			first = iterator.next();

			while (iterator.hasNext())
				images.add(iterator.next());

			model.addAttribute(SaleAdvertisementViewConstants.PARAM_IMAGE, first);
			model.addAttribute(SaleAdvertisementViewConstants.PARAM_IMAGES, images);
		}
	}
}