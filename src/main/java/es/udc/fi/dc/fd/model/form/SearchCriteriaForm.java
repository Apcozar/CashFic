package es.udc.fi.dc.fd.model.form;

import java.math.BigDecimal;

public class SearchCriteriaForm {

	/** The city. */
	private String city;

	/** The keywords. */
	private String keywords;

	/** The min date. */
	private String minDate;

	/** The max date. */
	private String maxDate;

	/** The min price. */
	private BigDecimal minPrice;

	/** The max price. */
	private BigDecimal maxPrice;

	/** The min rating. */
	private Double minRating;

	/**
	 * Instantiates a new empty search criteria form.
	 */
	public SearchCriteriaForm() {
		super();
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Gets the keywords.
	 *
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Sets the keywords.
	 *
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * Gets the min date.
	 *
	 * @return the minDate
	 */
	public String getMinDate() {
		return minDate;
	}

	/**
	 * Sets the min date.
	 *
	 * @param minDate the minDate to set
	 */
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}

	/**
	 * Gets the max date.
	 *
	 * @return the maxDate
	 */
	public String getMaxDate() {
		return maxDate;
	}

	/**
	 * Sets the max date.
	 *
	 * @param maxDate the maxDate to set
	 */
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * Gets the min price.
	 *
	 * @return the minPrice
	 */
	public BigDecimal getMinPrice() {
		return minPrice;
	}

	/**
	 * Sets the min price.
	 *
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	/**
	 * Gets the max price.
	 *
	 * @return the maxPrice
	 */
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	/**
	 * Sets the max price.
	 *
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	/**
	 * Gets the min rating.
	 *
	 * @return the minRating
	 */
	public Double getMinRating() {
		return minRating;
	}

	/**
	 * Sets the min rating.
	 *
	 * @param minRating the minRating to set
	 */
	public void setMinRating(Double minRating) {
		this.minRating = minRating;
	}

}
