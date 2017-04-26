package be.ceau.podcastparser.models;

import java.math.BigDecimal;

/**
 * <p>
 * A single latitude-longitude pair.
 * </p>
 * <p>
 * Used in Geo and GeoRSS namespace specifications.
 * </p>
 */
public class GeoPoint {

	private BigDecimal latitude;
	private BigDecimal longitude;

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
