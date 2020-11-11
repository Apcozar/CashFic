package es.udc.fi.dc.fd.model;

/**
 * The Enum State.
 */
public enum State {

	/** The advertisement state. */
	STATE_ON_SALE("STATE_ON_SALE"), STATE_ON_HOLD("STATE_ON_HOLD");

	/** The role. */
	private String saleAdvertisementState;

	/**
	 * Instantiates a new state.
	 *
	 * @param state the state
	 */
	State(String state) {
		this.saleAdvertisementState = state;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return this.saleAdvertisementState;
	}
}
