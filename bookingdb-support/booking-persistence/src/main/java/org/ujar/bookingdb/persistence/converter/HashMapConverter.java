package org.ujar.bookingdb.persistence.converter;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(Map<String, Object> customerInfo) {

    String customerInfoJson = null;
    try {
      customerInfoJson = objectMapper.writeValueAsString(customerInfo);
    } catch (final JsonProcessingException e) {
      log.error("JSON writing error", e);
    }

    return customerInfoJson;
  }

  @Override
  public Map<String, Object> convertToEntityAttribute(String customerInfoJSON) {

    Map<String, Object> customerInfo = null;
    try {
      customerInfo = objectMapper.readValue(customerInfoJSON, Map.class);
    } catch (final IOException e) {
      log.error("JSON reading error", e);
    }

    return customerInfo;
  }

}
