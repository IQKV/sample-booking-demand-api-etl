package org.ujar.micro.k8s.bookingdb.edge.model;

import java.util.List;

public record CityPage(List<City> content,
                       Integer totalPages,
                       Integer totalElements,
                       Integer size,
                       Integer number,
                       Pageable pageable) {

}
