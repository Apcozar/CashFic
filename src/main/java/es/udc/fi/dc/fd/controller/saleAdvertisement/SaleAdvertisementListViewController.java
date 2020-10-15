package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementNotFoundException;

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
	 * Show the sale advertisement info.
	 *
	 * @param id the id
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/{id}")
	public String showSaleAdvertisement(@PathVariable(value = "id") Integer id, Model model) {
		try {
			SaleAdvertisementEntity saleAdvertisement = saleAdvertisementService.findById(id);
			
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT, saleAdvertisement);
			
		} catch (SaleAdvertisementNotFoundException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST, SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
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
}
