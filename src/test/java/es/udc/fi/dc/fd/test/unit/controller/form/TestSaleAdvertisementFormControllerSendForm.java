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

import java.math.BigDecimal;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementFormController;
import es.udc.fi.dc.fd.controller.sale_advertisement.SaleAdvertisementViewConstants;
import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultSaleAdvertisementEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;
import es.udc.fi.dc.fd.service.ImageService;
import es.udc.fi.dc.fd.service.SaleAdvertisementService;
import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.exceptions.ImageAlreadyExistsException;
import es.udc.fi.dc.fd.service.exceptions.SaleAdvertisementAlreadyExistsException;
import es.udc.fi.dc.fd.service.securityService.SecurityService;
import es.udc.fi.dc.fd.service.user.exceptions.UserNotFoundException;
import es.udc.fi.dc.fd.test.config.UrlConfig;

/**
 * Unit tests for {@link SaleAdvertisementFormController}, checking the methods
 * for sending the form data.
 * 
 * @author Santiago
 */
@RunWith(JUnitPlatform.class)
final class TestSaleAdvertisementFormControllerSendForm {

	/**
	 * Mocked MVC context.
	 */
	private MockMvc mockMvc;

	/**
	 * Default constructor.
	 */
	public TestSaleAdvertisementFormControllerSendForm() {
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
			mockMvc = MockMvcBuilders.standaloneSetup(getController())
					.alwaysExpect(MockMvcResultMatchers.status().is3xxRedirection()).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verifies that after receiving valid form data the expected attributes are
	 * loaded into the model.
	 */
	@Test
	final void testSendFormData_ExpectedAttributeModel() throws Exception {
		final ResultActions result; // Request result

		result = mockMvc.perform(getFormRequest());

		// The response model contains the expected attributes
		result.andExpect(
				MockMvcResultMatchers.model().attributeExists(SaleAdvertisementViewConstants.SALE_ADVERTISEMENT_FORM));
	}

	/**
	 * Verifies that after received valid form data the expected view is returned.
	 */
	@Test
	final void testSendFormData_ExpectedView() throws Exception {
		final ResultActions result; // Request result

		// TODO: Just verify it is not this same view
		result = mockMvc.perform(getFormRequest());

		// The view is valid
		result.andExpect(MockMvcResultMatchers.view().name("redirect:/saleAdvertisement/1"));
	}

	/**
	 * Returns a controller with mocked dependencies.
	 * 
	 * @return a mocked controller
	 * @throws ImageAlreadyExistsException
	 */
	private final SaleAdvertisementFormController getController()
			throws UserNotFoundException, SaleAdvertisementAlreadyExistsException, ImageAlreadyExistsException {
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

		DefaultUserEntity user = new DefaultUserEntity();
		user.setId(1);
		user.setLogin("userLogin");

		saleAdvertisement = new DefaultSaleAdvertisementEntity("productTitle", "productDescription", user);
		saleAdvertisement.setPrice(BigDecimal.valueOf(5));
		returnSaleAdvertisement = new DefaultSaleAdvertisementEntity("productTitle", "productDescription", user);
		returnSaleAdvertisement.setId(1);

		Mockito.when(securityService.findLoggedInUsername()).thenReturn(user.getLogin());
		Mockito.when(userService.findByLogin(user.getLogin())).thenReturn(user);
		Mockito.when(saleAdvertisementService.add(saleAdvertisement)).thenReturn(returnSaleAdvertisement);
		DefaultImageEntity image = new DefaultImageEntity("imageFile", "imageFile", returnSaleAdvertisement);
		Mockito.when(imageService.add(image)).thenReturn(image);

		/** The image service. */

		/** The context. */

		return new SaleAdvertisementFormController(saleAdvertisementService, securityService, userService, context,
				imageService);
	}

	/**
	 * Returns a request builder for posting the form data.
	 * <p>
	 * This request contains all the required request parameters.
	 * <p>
	 * There is only a single required parameter, the {@code name} parameter.
	 * 
	 * @return a request builder for posting the form data
	 */
	private final RequestBuilder getFormRequest() {

		MockMultipartFile mockMultipartFile = new MockMultipartFile("dddd", "dasd", "text/plain",
				"test data".getBytes());
		return MockMvcRequestBuilders.multipart(UrlConfig.ADD_SALE_ADVERTISEMENT).file(mockMultipartFile)
				.param("productDescription", "productDescription").param("productTitle", "productTitle")
				.param("price", "5").characterEncoding("UTF-8");
	}

}
