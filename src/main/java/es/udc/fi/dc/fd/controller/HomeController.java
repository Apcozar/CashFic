/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2020 the original author or authors.
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

package es.udc.fi.dc.fd.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.udc.fi.dc.fd.service.UserService;
import es.udc.fi.dc.fd.service.securityService.SecurityService;

/**
 * Controller for home view.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Controller
@RequestMapping("/")
public class HomeController {

	/**
	 * The User service.
	 */
	private UserService userService;

	/**
	 * The Security service.
	 */
	private SecurityService securityService;

	/**
	 * Default constructor.
	 */
	public HomeController() {
		super();
	}

	/**
	 * Instantiates a new home controller.
	 *
	 * @param securityService the security service
	 * @param userService     the user service
	 */
	@Autowired
	public HomeController(final SecurityService securityService, final UserService userService) {
		this.securityService = checkNotNull(securityService, "received a null pointer as service");
		this.userService = checkNotNull(userService, "received a null pointer as service");
	}

	/**
	 * Shows the welcome view.
	 * 
	 * @return the welcome view
	 */
	@GetMapping(produces = MediaType.TEXT_HTML_VALUE)
	public String showWelcome(Model model) {
		String username = this.securityService.findLoggedInUsername();
		model.addAttribute(ViewConstants.USER_NAME, username);

		return ViewConstants.WELCOME;
	}

}
