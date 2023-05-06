package org.ujar.bookingdb.importer.service;

import org.springframework.stereotype.Service;

@Service
public interface HotelImporterService {

  void importHotels(Long cityId);
}
