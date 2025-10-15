package co.com.asulado.api.error;

import co.com.asulado.api.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Component
@Order(-2)
@RequiredArgsConstructor
public class GlobalErrorHandler implements WebExceptionHandler {

    private final List<ExceptionStrategy> strategies;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @NotNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NotNull Throwable ex) {
        ExceptionStrategy strategy = strategies.stream()
                .filter(s -> s.supports(ex))
                .findFirst()
                .orElseGet(DefaultExceptionStrategy::new);

        HttpStatus status = strategy.getStatus(ex);
        String error = strategy.getError(ex);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                OffsetDateTime.now().toString(),
                status.value(),
                error,
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                UUID.randomUUID().toString()
        );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsString(errorResponse).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            bytes = ("{\"error\":\"Error serializing error response\"}").getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)));
    }
}
