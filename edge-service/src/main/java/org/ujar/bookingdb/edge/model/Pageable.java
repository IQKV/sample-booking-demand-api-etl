package org.ujar.bookingdb.edge.model;

public record Pageable(Integer offset,
                       Integer pageNumber,
                       Integer pageSize) {
}
