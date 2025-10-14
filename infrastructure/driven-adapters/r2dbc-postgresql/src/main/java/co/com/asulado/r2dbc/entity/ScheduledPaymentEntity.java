package co.com.asulado.r2dbc.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScheduledPaymentEntity {
    private BigDecimal paymentId;
    private String period;
    private String payType;
    private String identification;
    private String name;
    private String state;
    private BigDecimal amount;
    private String customer;
66666666{6{6{6{<<<<6{<<<<<<<<6{6<<<<<<<<<<<<<<<<<<<<<6{6{66{{666<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<}