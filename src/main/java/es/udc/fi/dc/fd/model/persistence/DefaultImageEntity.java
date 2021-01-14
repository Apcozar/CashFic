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

package es.udc.fi.dc.fd.model.persistence;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import es.udc.fi.dc.fd.model.ImageEntity;
import es.udc.fi.dc.fd.model.SaleAdvertisementEntity;

/**
 * Persistent entity for the images.
 * <p>
 * This makes use of JPA annotations for the persistence configuration.
 *
 * @author Santiago
 */
@Entity(name = "ImageEntity")
@Table(name = "images")
public class DefaultImageEntity implements ImageEntity {

	/**
	 * Serialization ID.
	 */
	@Transient
	private static final long serialVersionUID = 1328776989450853491L;

	/**
	 * Image's ID.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id = -1;

	/**
	 * Title of the image.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@Column(name = "title", nullable = false, unique = false)
	private String title = "";

	/**
	 * Title of the image.
	 * <p>
	 * This is to have additional data apart from the id, to be used on the tests.
	 */
	@Column(name = "image_path", nullable = false, unique = true)
	private String image_path = "";

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_advertisement_id", nullable = false)
	private DefaultSaleAdvertisementEntity sale_advertisement;

	/**
	 * Constructs an image entity.
	 */
	public DefaultImageEntity() {
		super();
	}

	public DefaultImageEntity(String fileName, String title, DefaultSaleAdvertisementEntity saleAdvertisementEntity) {
		this.title = title;
		this.image_path = fileName;
		this.sale_advertisement = saleAdvertisementEntity;
	}

	/**
	 * Returns the identifier assigned to this image.
	 * <p>
	 * If no identifier has been assigned yet, then the value will be lower than
	 * zero.
	 *
	 * @return the image's identifier
	 */
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getImagePath() {
		return image_path;
	}

	@Override
	public SaleAdvertisementEntity getSale_advertisement() {
		return sale_advertisement;
	}

	@Override
	public void setId(final Integer value) {
		id = checkNotNull(value, "Received a null pointer as identifier");
	}

	@Override
	public void setTitle(final String value) {
		title = checkNotNull(value, "Received a null pointer as title");
	}

	@Override
	public void setImagePath(final String value) {
		image_path = checkNotNull(value, "Received a null pointer as image_path");
	}

	@Override
	public void setSale_advertisement(final DefaultSaleAdvertisementEntity saleAdvertisement) {
		sale_advertisement = checkNotNull(saleAdvertisement, "Received a null pointer as image_path");
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, image_path, sale_advertisement, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof DefaultImageEntity))
			return false;
		DefaultImageEntity other = (DefaultImageEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(image_path, other.image_path)
				&& Objects.equals(sale_advertisement, other.sale_advertisement) && Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "DefaultImageEntity [id=" + id + ", title=" + title + ", image_path=" + image_path
				+ ", sale_advertisement=" + sale_advertisement + "]";
	}

}
