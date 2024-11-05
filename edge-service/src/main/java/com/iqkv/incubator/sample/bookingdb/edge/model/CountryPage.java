package com.iqkv.incubator.sample.bookingdb.edge.model;

import java.util.List;

public record CountryPage(List<Country> content,
                          Integer totalPages,
                          Integer totalElements,
                          Integer size,
                          Integer number,
                          Pageable pageable) {

}
