/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2017-2019 the original author or authors.
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package es.udc.fi.dc.fd.test.unit.controller.form;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementFormController;
import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementViewConstants;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import es.udc.fi.dc.fd.test.config.UrlConfig;

/**
 * Unit tests for {@link SaleAdvertisementFormController}, checking the methods
 * for sending the form data.
 * <p>
 * These tests send data with missing values, to validate that the controller
 * handles binding error cases.
 * 
 * @author Santiago
 */
@RunWith(JUnitPlatform.class)
final class TestSaleAdvertisementFormControllerMissingData {

	/**
	 * Mocked MVC context.
	 */
	private MockMvc mockMvc;

	/**
	 * Default constructor.
	 */
	public TestSaleAdvertisementFormControllerMissingData() {
		super();
	}

	/**
	 * Sets up the mocked MVC context.
	 * <p>
	 * It expects all the responses to have the OK (200) HTTP code.
	 */
	@BeforeEach
	public final void setUpMockContext() {
		try {
			mockMvc = MockMvcBuilders.standaloneSetup(getController()).alwaysExpect(status().is4xxClientError())
					.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that after receiving form data missing the name, which is a required
	 * field, this is marked as an error.
	 */
	@Test
	final void testSendFormData_NoName_ExpectedAttributeModel() throws Exception {
		final ResultActions result; // Request result

		result = mockMvc.perform(getFormRequest());
		// The response model contains the expected attributes
		result.andExpect(
				MockMvcResultMatchers.model().attributeExists(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM));
//	
		// The response contains the expected errors
		result.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, "productDescription"));
		result.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, "productTitle"));
		result.andExpect(MockMvcResultMatchers.model()
				.attributeHasFieldErrors(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM, "price"));
	}

	/**
	 * Verifies that after receiving form data missing the name, which is a required
	 * field, the view is again the form view.
	 */
	@Test
	final void testSendFormData_NoName_NoViewChange() throws Exception {
		final ResultActions result; // Request result

		result = mockMvc.perform(getFormRequest());

		// The view is valid
		result.andExpect(
				MockMvcResultMatchers.view().name(SaleAdvertisementViewConstants.VIEW_SALE_ADVERTISEMENT_FORM));
	}

	/**
	 * Returns a controller with mocked dependencies.
	 * 
	 * @return a mocked controller
	 * @throws UserNotFoundException
	 * @throws SaleAdvertisementAlreadyExistsException
	 */
	private final SaleAdvertisementFormController getController()
			throws UserNotFoundException, SaleAdvertisementAlreadyExistsException {
		final SaleAdvertisementService saleAdvertisementService; // Mocked service
		final SecurityService securityService; // Mocked service
		final UserService userService;
		final ImageService imageService;
		final ServletContext context;

		final DefaultSaleAdvertisementEntity saleAdvertisement; // Mocked entities
		final DefaultSaleAdvertisementEntity returnSaleAdvertisement; // Mocked entities

		saleAdvertisementService = Mockito.mock(SaleAdvertisementService.class);
		securityService = Mockito.mock(SecurityService.class);
		userService = Mockito.mock(UserService.class);
		imageService = Mockito.mock(ImageService.class);
		context = Mockito.mock(ServletContext.class);

		saleAdvertisement = new DefaultSaleAdvertisementEntity();
		returnSaleAdvertisement = new DefaultSaleAdvertisementEntity();
		returnSaleAdvertisement.setId(1);

		DefaultUserEntity user = new DefaultUserEntity();
		Mockito.when(securityService.findLoggedInUsername()).thenReturn(user.getLogin());
		Mockito.when(userService.findByLogin(user.getLogin())).thenReturn(user);
		Mockito.when(saleAdvertisementService.add(saleAdvertisement)).thenReturn(returnSaleAdvertisement);

		/** The image service. */

		/** The context. */

		return new SaleAdvertisementFormController(saleAdvertisementService, securityService, userService, context,
				imageService);
	}

	/**
	 * Returns a request builder for posting the form data.
	 * <p>
	 * This request is missing all the required request parameters.
	 * <p>
	 * There is only a single required parameter, the {@code name} parameter.
	 * 
	 * @return a request builder for posting the form data
	 */
	private final RequestBuilder getFormRequest() {
		return MockMvcRequestBuilders.post(UrlConfig.ADD_SALE_ADVERTISEMENT);
	}

}
