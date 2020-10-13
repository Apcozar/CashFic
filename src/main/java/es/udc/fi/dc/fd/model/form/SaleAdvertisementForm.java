package es.udc.fi.dc.fd.model.form;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import es.udc.fi.dc.fd.model.persistence.DefaultImageEntity;
import es.udc.fi.dc.fd.model.persistence.DefaultUserEntity;

/**
 * The Class SaleAdvertisementForm.
 */
public class SaleAdvertisementForm implements Serializable{

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 4078947238523064061L;

	/** The product description. */
	private String productDescription;
	
	/** The product title. */
	private String productTitle;
	
	/** The user. */
	private DefaultUserEntity user;
	
	/** The date. */
	private LocalDateTime date;
	
	/** The images. */
	private Set<DefaultImageEntity> images;
	
	/**
	 * Constructs a DTO for the sale add form.
	 */
	public SaleAdvertisementForm() {
		super();
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, images, productDescription, productTitle, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleAdvertisementForm other = (SaleAdvertisementForm) obj;
		return Objects.equals(date, other.date) && Objects.equals(images, other.images)
				&& Objects.equals(productDescription, other.productDescription)
				&& Objects.equals(productTitle, other.productTitle) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "SaleAdvertisementForm [productDescription=" + productDescription + ", productTitle=" + productTitle + ", user="
				+ user + ", date=" + date + ", images=" + images + "]";
	}

	/**
	 * @param productDescription the productDescription to set
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	/**
	 * @return the productTitle
	 */
	public String getProductTitle() {
		return productTitle;
	}

	/**
	 * @param productTitle the productTitle to set
	 */
	public void setProductTitle(String productTitle) {
		this.productTitle = productTitle;
	}

	/**
	 * @return the user
	 */
	public DefaultUserEntity getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(DefaultUserEntity user) {
		this.user = user;
	}

	/**
	 * @return the date
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return the images
	 */
	public Set<DefaultImageEntity> getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(Set<DefaultImageEntity> images) {
		this.images = images;
	}
	
	

}
