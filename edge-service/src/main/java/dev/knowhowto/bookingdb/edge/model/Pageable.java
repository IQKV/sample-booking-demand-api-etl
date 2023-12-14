package dev.knowhowto.bookingdb.edge.model;

public record Pageable(Integer offset,
                       Integer pageNumber,
                       Integer pageSize) {
}
