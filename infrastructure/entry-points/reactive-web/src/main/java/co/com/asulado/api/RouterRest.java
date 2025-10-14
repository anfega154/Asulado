package co.com.asulado.api;

import co.com.asulado.api.config.ScheduledPaymentPath;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final ScheduledPaymentPath scheduledPaymentPath;
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(scheduledPaymentPath.getScheduledPayments()), handler::listenGETUseCase);
    }
}
