package org.ujar.bookingdb.apiclient.client;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.ujar.bookingdb.apiclient.config.NetClientProperties;

@Service
public class BookingcomNetClient extends AbstractNetClient {

  public BookingcomNetClient(NetClientProperties properties) {
    super(properties);
  }

  /**
   * Returns all counties the where booking.com offers hotels.
   */
  public String getCountries(Integer rows, Integer offset) {
    final var queryParams = Map.of(
        "languages", LANGUAGE_CODE,
        "rows", rows.toString(),
        "offset", offset.toString());
    return doRequest("countries", queryParams);
  }

  /**
   * Returns a list of cities where Booking.com offers hotels.
   */
  public String getCities(String countryCode, Integer rows, Integer offset) {
    final var queryParams = Map.of(
        "languages", LANGUAGE_CODE,
        "rows", rows.toString(),
        "offset", offset.toString(),
        "countries", countryCode);
    return doRequest("cities", queryParams);
  }

  /**
   * Returns a list of hotels.
   */
  public String getHotels(Long cityId, Integer rows, Integer offset) {
    final var queryParams = Map.of(
        "rows", rows.toString(),
        "offset", offset.toString(),
        "city_ids", cityId.toString(),
        "extras", "hotel_description"
    );
    return doRequest("hotels", queryParams);
  }
}
