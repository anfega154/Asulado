package co.com.asulado.api.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PageableDTOTest {

    @Test
    void pageableDTO_shouldCreateAndGetValues() {
        ScheduledPaymentDTO paymentDTO = new ScheduledPaymentDTO(
                BigDecimal.ONE, "202501", "Credit", "123", "John Doe",
                "ACTIVE", BigDecimal.TEN, "Customer1"
        );

        PageableDTO<ScheduledPaymentDTO> pageableDTO = new PageableDTO<>(
                List.of(paymentDTO),
                0,
                10,
                1L,
                1,
                true
        );

        assertEquals(1, pageableDTO.elements().size());
        assertEquals(paymentDTO, pageableDTO.elements().get(0));
        assertEquals(0, pageableDTO.page());
        assertEquals(10, pageableDTO.size());
        assertEquals(1L, pageableDTO.totalElements());
        assertEquals(1, pageableDTO.totalPages());
        assertTrue(pageableDTO.last());

        PageableDTO<ScheduledPaymentDTO> copy = new PageableDTO<>(
                List.of(paymentDTO),
                0,
                10,
                1L,
                1,
                true
        );
        assertEquals(pageableDTO, copy);
        assertEquals(pageableDTO.hashCode(), copy.hashCode());

        assertTrue(pageableDTO.toString().contains("elements"));
    }
}
