package co.com.asulado.api;

import co.com.asulado.api.Handler;
import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HandlerTest {

    @Mock
    private ScheduledPaymentInputPort scheduledPaymentInputPort;

    private Handler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new Handler(scheduledPaymentInputPort);
    }

    private ServerRequest mockRequest(Map<String, String> params) {
        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam(anyString())).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            return Optional.ofNullable(params.get(key));
        });
        return request;
    }

    @Test
    void listenListScheduledPaymentsByFilters_whenPaymentsFound() {
        ScheduledPayment payment = new ScheduledPayment();
        payment.setAmount(BigDecimal.TEN);
        Pageable<ScheduledPayment> pageable = mock(Pageable.class);
        when(pageable.getElements()).thenReturn(List.of(payment));

        when(scheduledPaymentInputPort.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.just(pageable));

        ServerRequest request = mockRequest(Map.of(
                "page", "1",
                "size", "5",
                "period", "202401",
                "identification", "123",
                "identificationType", "CC",
                "id", "42"
        ));

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .consumeNextWith(response -> {
                    assert response != null;
                })
                .verifyComplete();

        verify(scheduledPaymentInputPort).findByFilters(1, 5, "202401", "123", "CC", 42L);
    }

    @Test
    void listenListScheduledPaymentsByFilters_whenNoPaymentsFound() {
        when(scheduledPaymentInputPort.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.empty());

        ServerRequest request = mockRequest(Map.of(
                "page", "0",
                "size", "10"
        ));

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .consumeNextWith(response -> {
                    assert response != null;
                })
                .verifyComplete();
    }

    @Test
    void listenListScheduledPaymentsByFilters_withMissingParams() {
        ScheduledPayment payment = new ScheduledPayment();
        Pageable<ScheduledPayment> pageable = mock(Pageable.class);
        when(pageable.getElements()).thenReturn(List.of(payment));

        when(scheduledPaymentInputPort.findByFilters(anyInt(), anyInt(), anyString(), anyString(), anyString(), any()))
                .thenReturn(Flux.just(pageable));

        ServerRequest request = mockRequest(Map.of());

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .consumeNextWith(response -> {
                    assert response != null;
                })
                .verifyComplete();

        verify(scheduledPaymentInputPort).findByFilters(0, 10, "", "", "", null);
    }
}