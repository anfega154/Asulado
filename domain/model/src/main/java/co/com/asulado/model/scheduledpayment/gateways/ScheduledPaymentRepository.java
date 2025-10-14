package co.com.asulado.model.scheduledpayment.gateways;

import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

public interface ScheduledPaymentRepository {
    Mono<Page<ScheduledPayment>> findByFilters(String identification, String state, String period, String customer, Pageable pageable);
}
