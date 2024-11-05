package com.iqkv.incubator.sample.bookingdb.persistence.repository;

import com.iqkv.incubator.sample.bookingdb.persistence.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface HotelRepository
    extends PagingAndSortingRepository<Hotel, Long>, JpaRepository<Hotel, Long> {

  void deleteByHotelId(Long hotelId);
}
