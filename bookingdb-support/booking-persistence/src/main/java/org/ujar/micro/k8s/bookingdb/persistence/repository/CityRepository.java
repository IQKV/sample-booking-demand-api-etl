package org.ujar.micro.k8s.bookingdb.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.ujar.micro.k8s.bookingdb.persistence.entity.City;

public interface CityRepository
    extends PagingAndSortingRepository<City, Long>, JpaRepository<City, Long> {

  List<City> findAllByCityIdIn(List<Long> cityIds);
}
