package co.com.asulado.r2dbc;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @Mock
    MyReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @Mock
    ScheduledPaymentRepositoryImpl scheduledPaymentRepositoryImpl;

    @InjectMocks
    MyReactiveRepositoryAdapter adapter;

    private ScheduledPaymentEntity entity;
    private ScheduledPayment domain;

    @BeforeEach
    void setup() {
        entity = new ScheduledPaymentEntity();
        entity.setPaymentId(BigDecimal.ONE);
        entity.setPeriod("2025-01");
        entity.setPayType("Pago programado");
        entity.setIdentification("CC 1001");
        entity.setName("Andrés Gañán");
        entity.setState("Pagado");
        entity.setAmount(BigDecimal.valueOf(3500000));
        entity.setCustomer("Asulado S.A.");

        domain = new ScheduledPayment();
        domain.setPaymentId(BigDecimal.ONE);
        domain.setPeriod("2025-01");
        domain.setPayType("Pago programado");
        domain.setIdentification("CC 1001");
        domain.setName("Andrés Gañán");
        domain.setState("Pagado");
        domain.setAmount(BigDecimal.valueOf(3500000));
        domain.setCustomer("Asulado S.A.");
    }

    @Test
    void mustReturnPageableWhenFindByFiltersSuccess() {
        // Arrange
        when(scheduledPaymentRepositoryImpl.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.just(entity));
        when(mapper.map(any(ScheduledPaymentEntity.class), eq(ScheduledPayment.class))).thenReturn(domain);

        // Act
        Flux<Pageable<ScheduledPayment>> result = adapter.findByFilters(0, 5, "2025-01", "", "", null);

        // Assert
        StepVerifier.create(result)
                .assertNext(pageable -> {
                    List<ScheduledPayment> list = pageable.getElements();
                    assert !list.isEmpty();
                    assert list.get(0).getPaymentId().equals(BigDecimal.ONE);
                    assert pageable.getPage() == 0;
                    assert pageable.getSize() == 5;
                })
                .verifyComplete();
    }

    @Test
    void mustReturnEmptyPageableWhenNoResults() {
        // Arrange
        when(scheduledPaymentRepositoryImpl.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.empty());

        // Act
        Flux<Pageable<ScheduledPayment>> result = adapter.findByFilters(0, 5, "", "", "", null);

        // Assert
        StepVerifier.create(result)
                .assertNext(pageable -> {
                    assert pageable.getElements().isEmpty();
                    assert pageable.getTotalElements() == 0;
                    assert pageable.getTotalPages() == 0;
                    assert pageable.isLast();
                })
                .verifyComplete();
    }

    @Test
    void mustHandleErrorGracefully() {
        // Arrange
        when(scheduledPaymentRepositoryImpl.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.error(new RuntimeException("DB error")));

        // Act
        Flux<Pageable<ScheduledPayment>> result = adapter.findByFilters(0, 5, "", "", "", null);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().contains("Error buscando pagos programados por filtros"))
                .verify();
    }
}
