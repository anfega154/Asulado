package co.com.asulado.r2dbc;

import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MyReactiveRepository extends ReactiveCrudRepository<ScheduledPaymentEntity, Void>, ReactiveQueryByExampleExecutor<ScheduledPaymentEntity> {

}
