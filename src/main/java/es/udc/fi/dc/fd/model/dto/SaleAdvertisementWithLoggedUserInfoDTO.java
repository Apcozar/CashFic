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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private List<ImageDTO> images = new ArrayList<>();

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
	 * Sale advertisement's state
	 */
	private final String state;

	public String getState() {
		return state;
	}

	/**
	 * Sale advertisement's owner user identifier
	 */
	private final Integer ownerUserId;

	/** The role. */
	private final Integer role;

	/**
	 * Sale advertisement's publish date
	 */
	private final LocalDateTime date;

	/** The advertisement's price. */
	private final BigDecimal price;

	/** The sale advertisement ID. */
	private final Integer saleAdvertisementID;
	/**
	 * Sale advertisement's likes count
	 */
	private Integer saleAdvertisementLikesCount;
	/**
	 * boolean true if logged user likes sale advertisement false if not
	 */
	private final boolean userLikeSaleAdvertisement;

	/** The city of the user. */
	private final String city;

	/**
	 * boolean true if logged user likes sale advertisement false if not
	 */
	private final boolean loggedUserFollowsSaleAdvertisementUser;

	/** The are user rated. */
	private final boolean areUserRated;

	/** The average rating. */
	private final Double averageRating;

	/**
	 * boolean true sale advertisement have been sold false if not
	 */
	private final boolean saleAdvertisementIsSold;

	/** The is user logged. */
	private final boolean isUserLogged;

	/**
	 * Constructs SaleAdvertisementWithLoggedUserInfoDTO DTO.
	 *
	 * @param saleAdvertisement                      the sale advertisement
	 * @param userLikeSaleAdvertisement              the user like sale
	 *                                               advertisement
	 * @param loggedUserFollowsSaleAdvertisementUser the logged user follows sale
	 *                                               advertisement user
	 * @param areUserRated                           the are user rated
	 * @param averageRating                          the average rating
	 * @param saleAdvertisementIsSold                the sale advertisement is sold
	 * @param isUserLogged                           the is user logged
	 */
	public SaleAdvertisementWithLoggedUserInfoDTO(SaleAdvertisementEntity saleAdvertisement,
			boolean userLikeSaleAdvertisement, boolean loggedUserFollowsSaleAdvertisementUser, boolean areUserRated,
			Double averageRating, boolean saleAdvertisementIsSold, boolean isUserLogged) {

		super();

		productTitle = saleAdvertisement.getProductTitle();
		productDescription = saleAdvertisement.getProductDescription();
		ownerUserLogin = saleAdvertisement.getUser().getLogin();
		saleAdvertisementLikesCount = saleAdvertisement.getLikes().size();
		saleAdvertisementID = saleAdvertisement.getId();
		date = saleAdvertisement.getDate();
		this.userLikeSaleAdvertisement = userLikeSaleAdvertisement;
		ownerUserId = saleAdvertisement.getUser().getId();
		this.loggedUserFollowsSaleAdvertisementUser = loggedUserFollowsSaleAdvertisementUser;
		state = saleAdvertisement.getState().toString();
		price = saleAdvertisement.getPrice();
		city = saleAdvertisement.getUser().getCity();
		role = saleAdvertisement.getUser().getRole().ordinal();
		this.areUserRated = areUserRated;
		this.averageRating = averageRating;
		saleAdvertisement.getImages().forEach(image -> images.add(new ImageDTO(image)));
		this.saleAdvertisementIsSold = saleAdvertisementIsSold;
		this.isUserLogged = isUserLogged;
	}

	/**
	 * @return the saleAdvertisementIsSold
	 */
	public boolean isSaleAdvertisementIsSold() {
		return saleAdvertisementIsSold;
	}

	/**
	 * @return the images
	 */
	public List<ImageDTO> getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(List<ImageDTO> images) {
		this.images = images;
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

	public BigDecimal getPrice() {
		return price;
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
	 * Sets the sale advertisement likes count.
	 *
	 * @param saleAdvertisementLikesCount the sale advertisement likes count
	 */
	public void setSaleAdvertisementLikesCount(Integer saleAdvertisementLikesCount) {
		this.saleAdvertisementLikesCount = saleAdvertisementLikesCount;
	}

	/**
	 * @return the userLikeSaleAdvertisement
	 */
	public boolean isUserLikeSaleAdvertisement() {
		return userLikeSaleAdvertisement;
	}

	public Integer getOwnerUserId() {
		return ownerUserId;
	}

	public boolean isLoggedUserFollowsSaleAdvertisementUser() {
		return loggedUserFollowsSaleAdvertisementUser;
	}

	public String getCity() {
		return city;
	}

	public boolean isAreUserRated() {
		return areUserRated;
	}

	public Double getAverageRating() {
		return averageRating;
	}

	/**
	 * @return the role
	 */
	public Integer getRole() {
		return role;
	}

	public boolean isUserLogged() {
		return isUserLogged;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, date, images, loggedUserFollowsSaleAdvertisementUser, ownerUserId, ownerUserLogin,
				price, productDescription, productTitle, role, saleAdvertisementID, saleAdvertisementLikesCount, state,
				userLikeSaleAdvertisement, areUserRated, averageRating, isUserLogged);
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
		return Objects.equals(city, other.city) && Objects.equals(date, other.date)
				&& Objects.equals(images, other.images)
				&& loggedUserFollowsSaleAdvertisementUser == other.loggedUserFollowsSaleAdvertisementUser
				&& Objects.equals(ownerUserId, other.ownerUserId)
				&& Objects.equals(ownerUserLogin, other.ownerUserLogin) && Objects.equals(price, other.price)
				&& Objects.equals(productDescription, other.productDescription)
				&& Objects.equals(productTitle, other.productTitle) && Objects.equals(role, other.role)
				&& Objects.equals(saleAdvertisementID, other.saleAdvertisementID)
				&& Objects.equals(saleAdvertisementLikesCount, other.saleAdvertisementLikesCount)
				&& Objects.equals(state, other.state) && userLikeSaleAdvertisement == other.userLikeSaleAdvertisement
				&& Objects.equals(areUserRated, other.areUserRated)
				&& Objects.equals(averageRating, other.averageRating)
				&& Objects.equals(isUserLogged, other.isUserLogged);
	}

	@Override
	public String toString() {
		return "SaleAdvertisementWithLoggedUserInfoDTO [images=" + images + ", productTitle=" + productTitle
				+ ", productDescription=" + productDescription + ", ownerUserLogin=" + ownerUserLogin + ", state="
				+ state + ", ownerUserId=" + ownerUserId + ", date=" + date + ", saleAdvertisementID="
				+ saleAdvertisementID + ", saleAdvertisementLikesCount=" + saleAdvertisementLikesCount
				+ ", userLikeSaleAdvertisement=" + userLikeSaleAdvertisement
				+ ", loggedUserFollowsSaleAdvertisementUser=" + loggedUserFollowsSaleAdvertisementUser + ", city="
				+ city + ", isUserLogged=" + isUserLogged + "]";
	}

}
