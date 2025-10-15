package co.com.asulado.usecase.scheduledpayment;

import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ScheduledPaymentUseCaseTest {

    private ScheduledPaymentRepository repository;
    private ScheduledPaymentUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(ScheduledPaymentRepository.class);
        useCase = new ScheduledPaymentUseCase(repository);
    }

    @Test
    void shouldReturnPagedResultsWhenRepositoryReturnsData() {
        // Arrange
        ScheduledPayment payment = new ScheduledPayment();
        payment.setPaymentId(BigDecimal.ONE);
        payment.setPeriod("2024-12");
        payment.setPayType("MENSUAL");
        payment.setIdentification("123");
        payment.setName("Andres Gañan");
        payment.setState("PENDIENTE");
        payment.setAmount(BigDecimal.valueOf(50000));
        payment.setCustomer("ASULADO");

        Pageable<ScheduledPayment> pageable = new Pageable<>(
                List.of(payment), 0, 10, 1
        );

        when(repository.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(Flux.just(pageable));

        // Act & Assert
        StepVerifier.create(useCase.findByFilters(0, 10, "2024-12", "123", "CC", 1L))
                .expectNextMatches(result -> {
                    List<ScheduledPayment> elements = result.getElements();
                    return result.getPage() == 0 &&
                            result.getSize() == 10 &&
                            result.getTotalElements() == 1 &&
                            elements.size() == 1 &&
                            "ASULADO".equals(elements.get(0).getCustomer()) &&
                            "2024-12".equals(elements.get(0).getPeriod());
                })
                .verifyComplete();

        Mockito.verify(repository).findByFilters(0, 10, "2024-12", "123", "CC", 1L);
    }

    @Test
    void shouldThrowExceptionWhenRepositoryReturnsEmptyFlux() {
        // Arrange
        when(repository.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(useCase.findByFilters(0, 10, "2024-12", "123", "CC", 1L))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().equals("No se encontraron pagos programados con los filtros proporcionados"))
                .verify();

        Mockito.verify(repository).findByFilters(0, 10, "2024-12", "123", "CC", 1L);
    }
}
