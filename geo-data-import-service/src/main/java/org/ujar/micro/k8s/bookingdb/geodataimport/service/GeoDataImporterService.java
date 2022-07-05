package org.ujar.micro.k8s.bookingdb.geodataimport.service;

import org.springframework.stereotype.Service;

@Service
public interface GeoDataImporterService {

  void importCountries();

  void importCities();
}
