package co.com.asulado.api.dto;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduledPaymentDTOTest {

    @Test
    void scheduledPaymentDTO_shouldCreateAndGetValues() {
        ScheduledPaymentDTO dto = new ScheduledPaymentDTO(
                BigDecimal.ONE, "202501", "Credit", "123", "John Doe",
                "ACTIVE", BigDecimal.TEN, "Customer1"
        );

        assertEquals(BigDecimal.ONE, dto.paymentId());
        assertEquals("202501", dto.period());
        assertEquals("Credit", dto.payType());
        assertEquals("123", dto.identification());
        assertEquals("John Doe", dto.name());
        assertEquals("ACTIVE", dto.state());
        assertEquals(BigDecimal.TEN, dto.amount());
        assertEquals("Customer1", dto.customer());

        // equals, hashCode, toString
        ScheduledPaymentDTO copy = new ScheduledPaymentDTO(
                BigDecimal.ONE, "202501", "Credit", "123", "John Doe",
                "ACTIVE", BigDecimal.TEN, "Customer1"
        );
        assertEquals(dto, copy);
        assertEquals(dto.hashCode(), copy.hashCode());
        assertTrue(dto.toString().contains("paymentId"));
    }
}
