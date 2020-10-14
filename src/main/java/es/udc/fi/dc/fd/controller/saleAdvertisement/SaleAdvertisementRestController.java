package es.udc.fi.dc.fd.controller.saleAdvertisement;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;

@RestController
@RequestMapping("/rest/saleAdvertisement")
public class SaleAdvertisementRestController {

	/** The sale advertisement service. */
	private final SaleAdvertisementService saleAdvertisementService;

	/**
	 * Constructs a controller with the specified dependencies.
	 *
	 * @param saleAdvertisementService the sale advertisement service
	 */
	@Autowired
	public SaleAdvertisementRestController(SaleAdvertisementService saleAdvertisementService) {
		super();
		this.saleAdvertisementService = checkNotNull(saleAdvertisementService, ViewConstants.NULL_POINTER);
	}

	/**
	 * Returns a paginated collection of sale advertisements.
	 *
	 * @param page the pagination data
	 * 
	 * @return a paginated collection of sale advertisements
	 */
	@GetMapping
	public Iterable<? extends SaleAdvertisementEntity> getSaleAdvertisements(final Pageable page) {
		return saleAdvertisementService.getSaleAdvertisements(page);
	}
}
