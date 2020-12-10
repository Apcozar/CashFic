package es.udc.fi.dc.fd.model.form.account;

import java.io.Serializable;
import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * The Class RateForm.
 */
public class RateForm implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The rating value. */
	@NotEmpty(message = "{notEmpty}")
	private Integer ratingValue;

	/**
	 * Instantiates a new rate form.
	 *
	 * @param ratingValue the rating value
	 */
	public RateForm(Integer ratingValue) {
		super();
		this.ratingValue = ratingValue;
	}

	/**
	 * Instantiates a new rate form.
	 */
	public RateForm() {
		super();
	}

	/**
	 * Gets the rating value.
	 *
	 * @return the rating value
	 */
	public Integer getRatingValue() {
		return ratingValue;
	}

	/**
	 * Sets the rating value.
	 *
	 * @param ratingValue the new rating value
	 */
	public void setRatingValue(Integer ratingValue) {
		this.ratingValue = ratingValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ratingValue);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RateForm other = (RateForm) obj;
		return Objects.equals(ratingValue, other.ratingValue);
	}

	@Override
	public String toString() {
		return "RateForm [ratingValue=" + ratingValue + "]";
	}

}
