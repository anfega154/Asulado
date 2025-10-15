package co.com.asulado.api.dto;

import java.util.List;

public record PageableDTO<T>(
        List<T> elements,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {}
