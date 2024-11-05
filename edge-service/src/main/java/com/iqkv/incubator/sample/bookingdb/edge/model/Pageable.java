package com.iqkv.incubator.sample.bookingdb.edge.model;

public record Pageable(Integer offset,
                       Integer pageNumber,
                       Integer pageSize) {
}
