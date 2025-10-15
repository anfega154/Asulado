package co.com.asulado.api;

import co.com.asulado.api.api.BaseHandler;
import co.com.asulado.model.scheduledpayment.gateways.ScheduledPaymentInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler extends BaseHandler {

    private final ScheduledPaymentInputPort scheduledPaymentInputPort;
    public static final String MSG_NO_SCHEDULED_PAYMENTS_FOUND = "No se encontraron pagos programados";
    public static final String MSG_SCHEDULED_PAYMENTS_FOUND = "Pagos programados encontrados";

    public Mono<ServerResponse> listenListScheduledPaymentsByFilters(org.springframework.web.reactive.function.server.ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String period = request.queryParam("period").orElse("");
        String identification = request.queryParam("identification").orElse("");
        String identificationType = request.queryParam("identificationType").orElse("");
        Long id = request.queryParam("id").map(Long::valueOf).orElse(null);

        return scheduledPaymentInputPort.findByFilters(page, size, period, identification, identificationType, id)
                .collectList()
                .flatMap(scheduledPayments -> ok(scheduledPayments.isEmpty() ? MSG_NO_SCHEDULED_PAYMENTS_FOUND : MSG_SCHEDULED_PAYMENTS_FOUND, scheduledPayments));
    }

}
