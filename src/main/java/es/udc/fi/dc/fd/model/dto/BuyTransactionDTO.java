package es.udc.fi.dc.fd.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class BuyTransactionDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The created date. */
	private final LocalDateTime createdDate;

	/** The sale advertisement DTO. */
	private final SaleAdvertisementWithLoggedUserInfoDTO saleAdvertisementDTO;

	/**
	 * Instantiates a new buy transaction DTO.
	 *
	 * @param createdDate          the created date
	 * @param saleAdvertisementDTO the sale advertisement DTO
	 */
	public BuyTransactionDTO(LocalDateTime createdDate, SaleAdvertisementWithLoggedUserInfoDTO saleAdvertisementDTO) {
		super();
		this.createdDate = createdDate;
		this.saleAdvertisementDTO = saleAdvertisementDTO;
	}

	/**
	 * Gets the created date.
	 *
	 * @return the created date
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * Gets the sale advertisement DTO.
	 *
	 * @return the sale advertisement DTO
	 */
	public SaleAdvertisementWithLoggedUserInfoDTO getSaleAdvertisementDTO() {
		return saleAdvertisementDTO;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdDate, saleAdvertisementDTO);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuyTransactionDTO other = (BuyTransactionDTO) obj;
		return Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(saleAdvertisementDTO, other.saleAdvertisementDTO);
	}

	@Override
	public String toString() {
		return "BuyTransactionDTO [createdDate=" + createdDate + ", saleAdvertisementDTO=" + saleAdvertisementDTO + "]";
	}

}
