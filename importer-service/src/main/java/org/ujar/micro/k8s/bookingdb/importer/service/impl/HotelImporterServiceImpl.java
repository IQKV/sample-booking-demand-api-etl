package org.ujar.micro.k8s.bookingdb.importer.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.ujar.micro.k8s.bookingdb.apiclient.client.BookingcomNetClient;
import org.ujar.micro.k8s.bookingdb.importer.service.HotelImporterService;
import org.ujar.micro.k8s.bookingdb.persistence.entity.City;
import org.ujar.micro.k8s.bookingdb.persistence.entity.Hotel;
import org.ujar.micro.k8s.bookingdb.persistence.repository.CityRepository;
import org.ujar.micro.k8s.bookingdb.persistence.repository.HotelRepository;

@Service
@Slf4j
public class HotelImporterServiceImpl implements HotelImporterService {

  private static final Integer LIMIT = 100;
  private final BookingcomNetClient client;
  private final CityRepository cityRepository;
  private final HotelRepository hotelRepository;
  private final ObjectMapper mapper;

  public HotelImporterServiceImpl(BookingcomNetClient client, CityRepository cityRepository,
                                  HotelRepository hotelRepository, ObjectMapper mapper) {
    this.client = client;
    this.cityRepository = cityRepository;
    this.hotelRepository = hotelRepository;
    this.mapper = mapper;
  }

  @Transactional
  @Override
  public void importHotels(Long cityId) {
    City city = cityRepository.findOneByCityId(cityId).orElseThrow(RuntimeException::new);
    String body;
    List<Hotel> entities;
    int offset = 0;
    do {
      body = client.getHotels(cityId, LIMIT, offset);
      JsonNode nodes;
      try {
        nodes = mapper.readTree(body).get("result");
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }

      if (nodes == null) {
        break;
      }
      entities = mapper.convertValue(nodes, new TypeReference<>() {
      });

      if (entities != null && !entities.isEmpty()) {
        entities.forEach(hotel -> {
          hotel.setCity(city);
          var hotelId = hotel.getHotelId();
          hotelRepository.deleteByHotelId(hotelId);
          hotelRepository.saveAndFlush(hotel);
        });
      }
      offset += LIMIT;
    } while (entities != null && !entities.isEmpty());

    log.info("Import of hotels is finished.");
  }
}
