package org.ujar.bookingdb.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.bookingdb.persistence.entity.City;

public interface CityRepository
    extends PagingAndSortingRepository<City, Long>, JpaRepository<City, Long> {

  List<City> findAllByCityIdIn(List<Long> cityIds);

  Optional<City> findOneByCityId(Long cityId);
}
