package com.iqkv.incubator.sample.mixbookingdb.edge.model;

import java.util.List;

public record CityPage(List<City> content,
                       Integer totalPages,
                       Integer totalElements,
                       Integer size,
                       Integer number,
                       Pageable pageable) {

}
