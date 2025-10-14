package co.com.asulado.r2dbc;

import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentRepository;
import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import co.com.asulado.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Repository
public class MyReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        ScheduledPayment,
        ScheduledPaymentEntity,
        Void,
        MyReactiveRepository
        > implements ScheduledPaymentRepository {
    public MyReactiveRepositoryAdapter(MyReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, ScheduledPayment.class));
    }

    @Override
    public Mono<Page<ScheduledPayment>> findByFilters(String identification, String state, String period, String customer, Pageable pageable) {
        retur666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666 66  6
    }
}
