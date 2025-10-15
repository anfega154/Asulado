package co.com.asulado.model.scheduledpayment;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledPaymentTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        ScheduledPayment payment = new ScheduledPayment();
        BigDecimal paymentId = BigDecimal.valueOf(12345);
        String period = "2025-01";
        String payType = "TRANSFER";
        String identification = "123456789";
        String name = "Andres Gañan";
        String state = "COMPLETED";
        BigDecimal amount = BigDecimal.valueOf(150000.75);
        String customer = "Asulado S.A.";

        payment.setPaymentId(paymentId);
        payment.setPeriod(period);
        payment.setPayType(payType);
        payment.setIdentification(identification);
        payment.setName(name);
        payment.setState(state);
        payment.setAmount(amount);
        payment.setCustomer(customer);

        assertEquals(paymentId, payment.getPaymentId());
        assertEquals(period, payment.getPeriod());
        assertEquals(payType, payment.getPayType());
        assertEquals(identification, payment.getIdentification());
        assertEquals(name, payment.getName());
        assertEquals(state, payment.getState());
        assertEquals(amount, payment.getAmount());
        assertEquals(customer, payment.getCustomer());
    }

    @Test
    void shouldAllowNullValues() {
        ScheduledPayment payment = new ScheduledPayment();

        payment.setPaymentId(null);
        payment.setPeriod(null);
        payment.setPayType(null);
        payment.setIdentification(null);
        payment.setName(null);
        payment.setState(null);
        payment.setAmount(null);
        payment.setCustomer(null);

        assertNull(payment.getPaymentId());
        assertNull(payment.getPeriod());
        assertNull(payment.getPayType());
        assertNull(payment.getIdentification());
        assertNull(payment.getName());
        assertNull(payment.getState());
        assertNull(payment.getAmount());
        assertNull(payment.getCustomer());
    }
}
