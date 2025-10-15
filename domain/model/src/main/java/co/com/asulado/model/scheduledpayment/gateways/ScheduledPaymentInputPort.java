package co.com.asulado.model.scheduledpayment.gateways;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import reactor.core.publisher.Flux;

public interface ScheduledPaymentInputPort {
    Flux<Pageable<ScheduledPayment>> findByFilters(int page, int size, String period, String identification, String identificationType);
}
