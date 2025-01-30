/*
 * Copyright 2025 IQKV Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iqkv.incubator.sample.mixbookingdb.apiclient.client;

import java.util.Map;

import com.iqkv.incubator.sample.mixbookingdb.apiclient.config.NetClientProperties;
import org.springframework.stereotype.Service;

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
