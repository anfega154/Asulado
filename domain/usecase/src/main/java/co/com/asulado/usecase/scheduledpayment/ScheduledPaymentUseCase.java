package co.com.asulado.usecase.scheduledpayment;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentInputPort;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentRepository;
import reactor.core.publisher.Flux;

public class ScheduledPaymentUseCase implements ScheduledPaymentInputPort {

    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private static final String EMPTY_LIST_ERROR = "No se encontraron pagos programados con los filtros proporcionados";

    public ScheduledPaymentUseCase(ScheduledPaymentRepository scheduledPaymentRepository) {
        this.scheduledPaymentRepository = scheduledPaymentRepository;
    }

    @Override
    public Flux<Pageable<ScheduledPayment>> findByFilters(int page, int size, String period, String identification, String identificationType) {
        return scheduledPaymentRepository.findByFilters(page, size, period, identification, identificationType)
                .switchIfEmpty(Flux.error(new IllegalStateException(EMPTY_LIST_ERROR)));
    }
}
