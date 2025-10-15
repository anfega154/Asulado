package co.com.asulado.r2dbc;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentRepository;
import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import co.com.asulado.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@Slf4j
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        ScheduledPayment,
        ScheduledPaymentEntity,
        Void,
        MyReactiveRepository
        > implements ScheduledPaymentRepository {

    private final ScheduledPaymentRepositoryImpl scheduledPaymentRepositoryImpl;
    private static final String ERROR_FETCHING = "Error buscando pagos programados por filtros: ";

    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper, ScheduledPaymentRepositoryImpl scheduledPaymentRepositoryImpl) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, ScheduledPayment.class));
        this.scheduledPaymentRepositoryImpl = scheduledPaymentRepositoryImpl;
    }

    @Override
    public Flux<Pageable<ScheduledPayment>> findByFilters(int page, int size, String period, String identification, String identificationType, Long id) {
        return scheduledPaymentRepositoryImpl.findByFilters(page, size, period, identification, identificationType, id)
                .map(entity -> mapper.map(entity, ScheduledPayment.class))
                .collectList()
                .map(list -> toPageable(list, page, size))
                .doOnNext(pageable -> log.info("Paginated list of ScheduledPayments: {}", pageable.getElements()))
                .flux()
                .onErrorResume(e -> Flux.error(new IllegalStateException(ERROR_FETCHING + e.getMessage())));
    }


    private Pageable<ScheduledPayment> toPageable(List<ScheduledPayment> list, int page, int size) {
        return new Pageable<>(list, page, size, list.size());
    }

}
