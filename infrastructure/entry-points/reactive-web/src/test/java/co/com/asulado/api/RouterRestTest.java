package co.com.asulado.api;

import co.com.asulado.api.config.ScheduledPaymentPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouterRestTest {

    @Mock
    private Handler handler;

    private ScheduledPaymentPath scheduledPaymentPath;
    private RouterRest routerRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scheduledPaymentPath = new ScheduledPaymentPath();
        scheduledPaymentPath.setScheduledPayments("/api/v1/scheduled-payments");
        routerRest = new RouterRest(scheduledPaymentPath);
    }

    @Test
    void shouldRouteToHandlerSuccessfully() {
        when(handler.listenListScheduledPaymentsByFilters(any()))
                .thenReturn(ServerResponse.ok().bodyValue("Pagos programados encontrados"));

        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);
        WebTestClient webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        webTestClient.get()
                .uri("/api/v1/scheduled-payments")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Pagos programados encontrados");

        verify(handler, times(1)).listenListScheduledPaymentsByFilters(any());
    }

    @Test
    void shouldReturnEmptyWhenRouteDoesNotMatch() {
        RouterFunction<ServerResponse> routerFunction = routerRest.routerFunction(handler);
        WebTestClient webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();

        webTestClient.get()
                .uri("/api/v1/other-endpoint")
                .exchange()
                .expectStatus().isNotFound();
    }
}
