package co.com.asulado.api.dto;

import java.math.BigDecimal;

public record ScheduledPaymentDTO(
        BigDecimal paymentId,
        String period,
        String payType,
        String identification,
        String name,
        String state,
        BigDecimal amount,
        String customer
) {}