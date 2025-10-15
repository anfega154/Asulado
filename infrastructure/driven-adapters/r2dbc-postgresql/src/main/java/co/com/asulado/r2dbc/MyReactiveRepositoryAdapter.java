package co.com.asulado.r2dbc;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.pageable.Pagination;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentRepository;
import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import co.com.asulado.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
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
    public Flux<Pageable<ScheduledPayment>> findByFilters(int page, int size, String sortBy, String direction, String name) {
        return scheduledPaymentRepositoryImpl.findByFilters(page, size, sortBy, direction, name)
                .collectList()
                .map(list -> toPageable(list, page, size))
                .flux()
                .onErrorResume(e -> Flux.error(new IllegalStateException(ERROR_FETCHING + e.getMessage())));
    }

    private Pageable<ScheduledPayment> toPageable(List<ScheduledPayment> list, int page, int size) {
        return Pagination.paginateAndSort(list, page, size, ScheduledPayment::getPaymentId);
    }

}
