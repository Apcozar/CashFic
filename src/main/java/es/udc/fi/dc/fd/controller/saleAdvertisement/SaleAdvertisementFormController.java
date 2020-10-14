package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.form.SaleAdvertisementForm;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementNotFoundException;
import es.udc.fi.dc.service.exceptions.SaleAdvertisementServiceException;

@Controller
@RequestMapping("/saleAvertisement")
public class SaleAdvertisementFormController {

	/** The sale add service. */
	private SaleAdvertisementService saleAdvertisementService;

	/**
	 * Instantiates a new sale add controller.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 */
	@Autowired
	public SaleAdvertisementFormController(SaleAdvertisementService saleAdvertisementService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
	}

	/**
	 * Show add sale advertisement view when de "get" petition is done.
	 *
	 * @param model the model
	 * @return the string
	 */
	@GetMapping(path = "/addSaleAdvertisement")
	public String showAddSaleAdvertisementView(final Model model) {
		model.addAttribute("saleAdvertisementForm", new SaleAdvertisementForm());

		return SaleAdvertisementViewConstants.VIEW_ADD_SALE_ADVERTISEMENT;
	}

	/**
	 * Shows the page when the "post" petition is done.
	 *
	 * @param saleAdvertisementForm the sale add form
	 * @param bindingResult         the binding result
	 * @return the welcome view
	 * @throws SaleAdvertisementServiceException       the sale advertisement
	 *                                                 service exception
	 * @throws SaleAdvertisementAlreadyExistsException exception
	 */
	@PostMapping(path = "/addSaleAdvertisement")
	public String addSaleAdvertisement(
			@Valid @ModelAttribute("saleAdvertisementForm") SaleAdvertisementForm saleAdvertisementForm,
			BindingResult bindingResult)
			throws SaleAdvertisementServiceException, SaleAdvertisementAlreadyExistsException {

		if (bindingResult.hasErrors()) {
			return SaleAdvertisementViewConstants.VIEW_ADD_SALE_ADVERTISEMENT;
		}

		saleAdvertisementService.add(new DefaultSaleAdvertisementEntity(saleAdvertisementForm.getProductTitle(),
				saleAdvertisementForm.getProductDescription(), saleAdvertisementForm.getImages(),
				saleAdvertisementForm.getUser(), saleAdvertisementForm.getDate()));

		return ViewConstants.WELCOME;
	}

	/**
	 * Update sale add.
	 *
	 * @param id                    the sale id
	 * @param saleAdvertisementForm the sale add form
	 * @param bindingResult         the binding result
	 * @param model                 the model
	 * 
	 * @return the welcome view
	 * @throws SaleAdvertisementNotFoundException exception
	 */
	@PutMapping(path = "/{id}")
	public String updateSaleAdvertisement(@PathVariable Integer id,
			@Valid @ModelAttribute("saleAdvertisementForm") SaleAdvertisementForm saleAdvertisementForm,
			BindingResult bindingResult, Model model) throws SaleAdvertisementNotFoundException {

		try {
			if (bindingResult.hasErrors()) {
				checkSaleAdvertisement(id, model);
				return SaleAdvertisementViewConstants.UPDATE_SALE_ADVERTISEMENT;
			}

			saleAdvertisementService.update(new DefaultSaleAdvertisementEntity(id,
					saleAdvertisementForm.getProductTitle(), saleAdvertisementForm.getProductDescription(),
					saleAdvertisementForm.getUser(), saleAdvertisementForm.getDate()));

			return ViewConstants.WELCOME;
		} catch (SaleAdvertisementServiceException e) {
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
			return SaleAdvertisementViewConstants.UPDATE_SALE_ADVERTISEMENT;
		}
	}

	/**
	 * Check if the sale exists or not.
	 *
	 * @param id    the sale id
	 * @param model the model
	 * @throws SaleAdvertisementNotFoundException
	 */
	private void checkSaleAdvertisement(Integer id, Model model) throws SaleAdvertisementNotFoundException {
		try {
			saleAdvertisementService.findById(id);
			model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST,
					SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_NOT_EXIST);
		} catch (SaleAdvertisementNotFoundException e) {
			// If the exception jump, the sale does not exist
		}
	}

}
