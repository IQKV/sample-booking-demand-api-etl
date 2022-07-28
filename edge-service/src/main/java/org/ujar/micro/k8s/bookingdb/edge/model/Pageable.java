package org.ujar.micro.k8s.bookingdb.edge.model;

public record Pageable(Integer offset,
                       Integer pageNumber,
                       Integer pageSize) {
}
