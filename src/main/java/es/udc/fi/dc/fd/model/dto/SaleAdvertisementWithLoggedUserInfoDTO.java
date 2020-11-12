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
import java.time.LocalDateTime;
import java.util.ArrayList;

import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;

/**
 * Represents the information send to sale advertisement list view
 * <p>
 * This is a DTO, meant to allow communication between the view and the
 * controller
 * <p>
 *
 * @author SantiagoPaz
 */
public final class SaleAdvertisementWithLoggedUserInfoDTO implements Serializable {

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 1328776989450853491L;

	/**
	 * Sale advertisement's images
	 */
	private ArrayList<ImageDTO> images = new ArrayList<>();
	/**
	 * Sale advertisement's product title
	 */
	private final String productTitle;
	/**
	 * Sale advertisement's product description
	 */
	private final String productDescription;
	/**
	 * Sale advertisement's owner user Login
	 */
	private final String ownerUserLogin;
	/**
	 * Sale advertisement's publish date
	 */
	private final LocalDateTime date;
	// Should not use id?
	private final Integer saleAdvertisementID;
	/**
	 * Sale advertisement's likes count
	 */
	private final Integer saleAdvertisementLikesCount;
	/**
	 * boolean true if logged user likes sale advertisement false if not
	 */
	private final boolean userLikeSaleAdvertisement;

	/**
	 * Constructs SaleAdvertisementWithLoggedUserInfoDTO DTO.
	 */
	public SaleAdvertisementWithLoggedUserInfoDTO(SaleAdvertisementEntity saleAdvertisement,
			boolean userLikeSaleAdvertisement) {
		super();

		productTitle = saleAdvertisement.getProductTitle();
		productDescription = saleAdvertisement.getProductDescription();
		ownerUserLogin = saleAdvertisement.getUser().getLogin();
		saleAdvertisementLikesCount = saleAdvertisement.getLikes().size();
		saleAdvertisementID = saleAdvertisement.getId();
		date = saleAdvertisement.getDate();
		this.userLikeSaleAdvertisement = userLikeSaleAdvertisement;

		saleAdvertisement.getImages().forEach((image) -> {
			images.add(new ImageDTO(image));

		});

	}

	/**
	 * @return the images
	 */
	public ArrayList<ImageDTO> getImages() {
		return images;
	}

	/**
	 * @return the productTitle
	 */
	public String getProductTitle() {
		return productTitle;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}

	/**
	 * @return the ownerUserLogin
	 */
	public String getOwnerUserLogin() {
		return ownerUserLogin;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @return the saleAdvertisementID
	 */
	public Integer getSaleAdvertisementID() {
		return saleAdvertisementID;
	}

	/**
	 * @return the saleAdvertisementLikesCount
	 */
	public Integer getSaleAdvertisementLikesCount() {
		return saleAdvertisementLikesCount;
	}

	/**
	 * @return the userLikeSaleAdvertisement
	 */
	public boolean isUserLikeSaleAdvertisement() {
		return userLikeSaleAdvertisement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((images == null) ? 0 : images.hashCode());
		result = prime * result + ((ownerUserLogin == null) ? 0 : ownerUserLogin.hashCode());
		result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
		result = prime * result + ((productTitle == null) ? 0 : productTitle.hashCode());
		result = prime * result + ((saleAdvertisementID == null) ? 0 : saleAdvertisementID.hashCode());
		result = prime * result + ((saleAdvertisementLikesCount == null) ? 0 : saleAdvertisementLikesCount.hashCode());
		result = prime * result + (userLikeSaleAdvertisement ? 1231 : 1237);
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
		SaleAdvertisementWithLoggedUserInfoDTO other = (SaleAdvertisementWithLoggedUserInfoDTO) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (images == null) {
			if (other.images != null)
				return false;
		} else if (!images.equals(other.images))
			return false;
		if (ownerUserLogin == null) {
			if (other.ownerUserLogin != null)
				return false;
		} else if (!ownerUserLogin.equals(other.ownerUserLogin))
			return false;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		if (productTitle == null) {
			if (other.productTitle != null)
				return false;
		} else if (!productTitle.equals(other.productTitle))
			return false;
		if (saleAdvertisementID == null) {
			if (other.saleAdvertisementID != null)
				return false;
		} else if (!saleAdvertisementID.equals(other.saleAdvertisementID))
			return false;
		if (saleAdvertisementLikesCount == null) {
			if (other.saleAdvertisementLikesCount != null)
				return false;
		} else if (!saleAdvertisementLikesCount.equals(other.saleAdvertisementLikesCount))
			return false;
		if (userLikeSaleAdvertisement != other.userLikeSaleAdvertisement)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SaleAdvertisementDTO [images=" + images + ", productTitle=" + productTitle + ", productDescription="
				+ productDescription + ", ownerUserLogin=" + ownerUserLogin + ", date=" + date
				+ ", saleAdvertisementID=" + saleAdvertisementID + ", saleAdvertisementLikesCount="
				+ saleAdvertisementLikesCount + ", userLikeSaleAdvertisement=" + userLikeSaleAdvertisement + "]";
	}

}
