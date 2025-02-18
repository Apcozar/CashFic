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

package es.udc.fi.dc.fd.test.config;

/**
 * Contains configuration information for the controller URLs.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 */
public final class UrlConfig {

	/**
	 * Form view URL.
	 */
	public static final String URL_FORM = "/entity/edit";

	/**
	 * URL for posting the form data.
	 */
	public static final String URL_FORM_POST = "/entity";

	/**
	 * URL for posting the form data.
	 */
	public static final String ADD_SALE_ADVERTISEMENT = "/saleAdvertisement/addSaleAdvertisement";

	/**
	 * Entities list view URL.
	 */
	public static final String URL_LIST = "/entity/list";

	/**
	 * Entities REST URL.
	 */
	public static final String URL_REST = "/rest/entity";

	/**
	 * Default constructor to avoid initialization.
	 */
	private UrlConfig() {
		super();
	}

}
