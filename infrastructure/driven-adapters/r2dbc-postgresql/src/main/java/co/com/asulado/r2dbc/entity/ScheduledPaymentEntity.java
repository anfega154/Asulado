package co.com.asulado.r2dbc.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScheduledPaymentEntity {
    private String paymentId;
    private String period;
    private String payType;
    private String identification;
    private String name;
    private String state;
    private BigDecimal amount;
    private String customer;
}
