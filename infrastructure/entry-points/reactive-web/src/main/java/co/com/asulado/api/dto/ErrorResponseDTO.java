package co.com.asulado.api.dto;

public record ErrorResponseDTO (
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId
) {}