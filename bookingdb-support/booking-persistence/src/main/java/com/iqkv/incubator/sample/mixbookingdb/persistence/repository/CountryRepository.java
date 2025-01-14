/*
 * Copyright 2024 IQKV Team.
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

package com.iqkv.incubator.sample.mixbookingdb.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.iqkv.incubator.sample.mixbookingdb.persistence.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CountryRepository
    extends PagingAndSortingRepository<Country, Long>, JpaRepository<Country, Long> {

  List<Country> findAllByCountryIn(List<String> countryCodes);

  @Query(value = "SELECT * FROM countries c WHERE c.country = :country LIMIT 1", nativeQuery = true)
  Optional<Country> findOneByCountry(String country);
}
