package es.udc.fi.dc.fd.model.form;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 * The Class SaleAdvertisementForm.
 */
public class SaleAdvertisementForm implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 4078947238523064061L;

	/** The product description. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 1, max = 255, message = "{productDescriptionSize}")
	private String productDescription;

	/** The product title. */
	@NotEmpty(message = "{notEmpty}")
	@Size(min = 1, max = 30, message = "{productTitleSize}")
	private String productTitle;

	/** The price. */
	@NotEmpty(message = "{notEmpty}")
	private String price;

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/** The image file. */
	private transient List<MultipartFile> imageFile;

	/**
	 * Constructs a DTO for the sale add form.
	 */
	public SaleAdvertisementForm() {
		super();
	}

	/**
	 * Gets the image file.
	 *
	 * @return the image file
	 */
	public List<MultipartFile> getImageFile() {
		return imageFile;
	}

	/**
	 * Sets the image file.
	 *
	 * @param imageFile the new image file
	 */
	public void setImageFile(List<MultipartFile> imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return the productDescription
	 */
	public String getProductDescription() {
		return productDescription;
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
	 * Hash code.
	 *
	 * @return the int
	 */
	@Override
	public int hashCode() {
		return Objects.hash(productDescription, productTitle, imageFile, price);
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SaleAdvertisementForm other = (SaleAdvertisementForm) obj;
		return Objects.equals(productDescription, other.productDescription)
				&& Objects.equals(productTitle, other.productTitle) && Objects.equals(imageFile, other.imageFile)
				&& Objects.equals(price, other.price);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "SaleAdvertisementForm [productDescription=" + productDescription + ", productTitle=" + productTitle
				+ ", price=" + price + "]";
	}

}
