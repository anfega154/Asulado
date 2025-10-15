package co.com.asulado.api;

import co.com.asulado.api.dto.PageableDTO;
import co.com.asulado.api.dto.ScheduledPaymentDTO;
import co.com.asulado.api.mapper.ScheduledPaymentMapper;
import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;

class HandlerTest {

    @Mock
    private ScheduledPaymentInputPort scheduledPaymentInputPort;

    @Mock
    private ScheduledPaymentMapper scheduledPaymentMapper;

    @InjectMocks
    private Handler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

 private ServerRequest buildRequest(Map<String, String> queryParams) {
     var httpRequest = org.springframework.mock.http.server.reactive.MockServerHttpRequest
             .get("/scheduled-payments")
             .queryParams(new org.springframework.util.LinkedMultiValueMap<>() {{
                 queryParams.forEach(this::add);
             }})
             .build();

     var exchange = org.springframework.mock.web.server.MockServerWebExchange.from(httpRequest);

     return ServerRequest.create(
             exchange,
             Collections.emptyList()
     );
 }

    @Test
    void listenListScheduledPaymentsByFilters_whenPaymentsFound() {
        ScheduledPayment payment = new ScheduledPayment();
        Pageable<ScheduledPayment> pageable = new Pageable<>(List.of(payment), 0, 10, 1);
        PageableDTO<ScheduledPaymentDTO> dto = new PageableDTO<>(
                List.of(new ScheduledPaymentDTO(
                        BigDecimal.ONE, "202501", "Credit", "123", "John Doe",
                        "ACTIVE", BigDecimal.TEN, "Customer1"
                )),
                0, 10, 1, 1, true
        );

        when(scheduledPaymentInputPort.findByFilters(0, 10, "", "", "", null))
                .thenReturn(Flux.just(pageable));

        when(scheduledPaymentMapper.toResponse(pageable)).thenReturn(dto);

        ServerRequest request = buildRequest(Map.of());

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(scheduledPaymentInputPort).findByFilters(0, 10, "", "", "", null);
        verify(scheduledPaymentMapper).toResponse(pageable);
    }

    @Test
    void listenListScheduledPaymentsByFilters_whenNoPaymentsFound() {
        Pageable<ScheduledPayment> pageable = new Pageable<>(Collections.emptyList(), 0, 10, 0);
        PageableDTO<ScheduledPaymentDTO> dto = new PageableDTO<>(
                Collections.emptyList(), 0, 10, 0, 0, true
        );

        when(scheduledPaymentInputPort.findByFilters(0, 10, "", "", "", null))
                .thenReturn(Flux.just(pageable));

        when(scheduledPaymentMapper.toResponse(pageable)).thenReturn(dto);

        ServerRequest request = buildRequest(Map.of());

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(scheduledPaymentInputPort).findByFilters(0, 10, "", "", "", null);
        verify(scheduledPaymentMapper).toResponse(pageable);
    }

    @Test
    void listenListScheduledPaymentsByFilters_withAllQueryParams() {
        Map<String, String> params = Map.of(
                "page", "1",
                "size", "5",
                "period", "2025-01",
                "identification", "12345",
                "identificationType", "CC",
                "id", "10"
        );

        ScheduledPayment payment = new ScheduledPayment();
        Pageable<ScheduledPayment> pageable = new Pageable<>(List.of(payment), 1, 5, 1);
        PageableDTO<ScheduledPaymentDTO> dto = new PageableDTO<>(
                List.of(new ScheduledPaymentDTO(BigDecimal.ONE, "2025-01", "Credit",
                        "12345", "John Doe", "ACTIVE", BigDecimal.TEN, "Customer1")),
                1, 5, 1, 1, true
        );

        when(scheduledPaymentInputPort.findByFilters(1, 5, "2025-01", "12345", "CC", 10L))
                .thenReturn(Flux.just(pageable));

        when(scheduledPaymentMapper.toResponse(pageable)).thenReturn(dto);

        ServerRequest request = buildRequest(params);

        Mono<ServerResponse> responseMono = handler.listenListScheduledPaymentsByFilters(request);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(scheduledPaymentInputPort).findByFilters(1, 5, "2025-01", "12345", "CC", 10L);
        verify(scheduledPaymentMapper).toResponse(pageable);
    }
}
