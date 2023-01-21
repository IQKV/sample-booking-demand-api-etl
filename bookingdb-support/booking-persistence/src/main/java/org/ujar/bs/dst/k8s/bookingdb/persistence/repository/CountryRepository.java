package org.ujar.bs.dst.k8s.bookingdb.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.bs.dst.k8s.bookingdb.persistence.entity.Country;

public interface CountryRepository
    extends PagingAndSortingRepository<Country, Long>, JpaRepository<Country, Long> {

  List<Country> findAllByCountryIn(List<String> countryCodes);

  @Query(value = "SELECT * FROM countries c WHERE c.country = :country LIMIT 1", nativeQuery = true)
  Optional<Country> findOneByCountry(String country);
}
