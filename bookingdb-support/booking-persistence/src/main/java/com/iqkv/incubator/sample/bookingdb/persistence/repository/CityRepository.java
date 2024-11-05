package com.iqkv.incubator.sample.bookingdb.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.iqkv.incubator.sample.bookingdb.persistence.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CityRepository
    extends PagingAndSortingRepository<City, Long>, JpaRepository<City, Long> {

  List<City> findAllByCityIdIn(List<Long> cityIds);

  Optional<City> findOneByCityId(Long cityId);
}
