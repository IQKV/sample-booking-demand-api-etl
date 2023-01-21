package org.ujar.bs.dst.k8s.bookingdb.edge.model;

public record Pageable(Integer offset,
                       Integer pageNumber,
                       Integer pageSize) {
}
