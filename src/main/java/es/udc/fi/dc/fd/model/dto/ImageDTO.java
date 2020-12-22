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

package es.udc.fi.dc.fd.model.dto;

import java.io.Serializable;

import es.udc.fi.dc.fd.model.ImageEntity;

/**
 * Represents the DTO used for show image's information
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller. This DTO is used inside
 * SaleAdvertisementWithLoggedUserInformation for sale advertisement's images
 * information
 * <p>
 *
 * @author Santiago
 */
public final class ImageDTO implements Serializable {

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 1328776989450853491L;

	/**
	 * Image path.
	 * <p>
	 * The path where image is stored
	 */
	private String imagePath;

	/**
	 * Constructs a DTO for the image entity.
	 *
	 * @param image the image
	 */
	public ImageDTO(ImageEntity image) {
		super();
		imagePath = image.getImagePath();
	}

	/**
	 * Returns the value of the image path.
	 * 
	 * @return the value of the image path
	 */
	public final String getImagePath() {
		return imagePath;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImageDTO other = (ImageDTO) obj;
		if (imagePath == null) {
			if (other.imagePath != null)
				return false;
		} else if (!imagePath.equals(other.imagePath)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ImageDTO [imagePath=" + imagePath + "]";
	}

}
