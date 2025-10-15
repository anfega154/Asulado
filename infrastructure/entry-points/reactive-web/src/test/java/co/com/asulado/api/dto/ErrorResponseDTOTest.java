package co.com.asulado.api.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorResponseDTOTest
{
    @Test
    void errorResponseDTO_shouldCreateAndGetValues() {
        ErrorResponseDTO dto = new ErrorResponseDTO(
                "2025-10-15T14:00:00Z",
                404,
                "Not Found",
                "Payment not found",
                "/scheduled-payments",
                "trace-123"
        );

        assertEquals("2025-10-15T14:00:00Z", dto.timestamp());
        assertEquals(404, dto.status());
        assertEquals("Not Found", dto.error());
        assertEquals("Payment not found", dto.message());
        assertEquals("/scheduled-payments", dto.path());
        assertEquals("trace-123", dto.traceId());

        ErrorResponseDTO copy = new ErrorResponseDTO(
                "2025-10-15T14:00:00Z",
                404,
                "Not Found",
                "Payment not found",
                "/scheduled-payments",
                "trace-123"
        );
        assertEquals(dto, copy);
        assertEquals(dto.hashCode(), copy.hashCode());
        assertTrue(dto.toString().contains("timestamp"));
    }
}
