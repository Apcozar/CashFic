package es.udc.fi.dc.fd.controller.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import es.udc.fi.dc.fd.controller.ViewConstants;
import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementViewConstants;
import es.udc.fi.dc.fd.model.form.SaleAdvertisementForm;

/**
 * The Class GlobalDefaultExceptionHandler.
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	/**
	 * Handle max size exception of multipart.
	 *
	 * @param exc   the exception
	 * @param model the model
	 * @return the string
	 */
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(MaxUploadSizeExceededException exc, Model model) {

		String maxSize = (exc.getMaxUploadSize() / 1000000) + " Mb";
		model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, new SaleAdvertisementForm());
		model.addAttribute(SaleAdvertisementViewConstants.MAX_SIZE_ERROR, maxSize);

		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
	}

	/**
	 * Handle multipart exception.
	 *
	 * @param exc   the exception
	 * @param model the model
	 * @return the string
	 */
	@ExceptionHandler(MultipartException.class)
	public String handleMultipartException(MultipartException exc, Model model) {

		model.addAttribute(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, new SaleAdvertisementForm());
		model.addAttribute(SaleAdvertisementViewConstants.UPLOAD_IMAGE_ERROR,
				SaleAdvertisementViewConstants.UPLOAD_IMAGE_ERROR);

		return SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM;
	}

	/**
	 * Handles exceptions that are not controlled
	 *
	 * @param exc   the exception
	 * @param model the model
	 * @return the string
	 */
	@ExceptionHandler(Exception.class)
	public String handleException(Exception exc, Model model) {
		return ViewConstants.SERVER_ERROR;
	}

}
