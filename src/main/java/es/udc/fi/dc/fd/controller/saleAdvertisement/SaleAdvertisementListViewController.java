package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;

@Controller
@RequestMapping("/saleAdvertisement")
public class SaleAdvertisementListViewController {

	/** The sale advertisement service. */
	private final SaleAdvertisementService saleAdvertisementService;

	/**
	 * Instantiates a new sale advertisement list view controller.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 */
	@Autowired
	public SaleAdvertisementListViewController(final SaleAdvertisementService saleAdvertisementService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
	}

	/**
	 * Show sale add list.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/list")
	public String showSaleAddList(final ModelMap model) {
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
				saleAdvertisementService.getAllSaleAdvertisements());
	}
}
