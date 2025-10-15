package co.com.asulado.api;

import co.com.asulado.api.config.ScheduledPaymentPath;
import co.com.asulado.model.pageable.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
@Tag(name = "Scheduled Payment API", description = "API para la gestión de pagos programados")
public class RouterRest {

    private final ScheduledPaymentPath scheduledPaymentPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/scheduled-payments",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenListScheduledPaymentsByFilters",
                    operation = @Operation(
                            operationId = "listScheduledPaymentsByFilters",
                            summary = "Listar pagos programados por filtros",
                            description = "Obtiene una lista paginada de pagos programados filtrados por período, identificación, tipo de identificación o ID.",
                            parameters = {
                                    @Parameter(name = "page", in = ParameterIn.QUERY, description = "Número de página", schema = @Schema(type = "integer", example = "0")),
                                    @Parameter(name = "size", in = ParameterIn.QUERY, description = "Tamaño de página", schema = @Schema(type = "integer", example = "10")),
                                    @Parameter(name = "period", in = ParameterIn.QUERY, description = "Período de pago (ej: 2025-01)", schema = @Schema(type = "string")),
                                    @Parameter(name = "identification", in = ParameterIn.QUERY, description = "Número de identificación del cliente", schema = @Schema(type = "string")),
                                    @Parameter(name = "identificationType", in = ParameterIn.QUERY, description = "Tipo de identificación (CC, NIT, etc.)", schema = @Schema(type = "string")),
                                    @Parameter(name = "id", in = ParameterIn.QUERY, description = "ID del pago programado", schema = @Schema(type = "integer"))
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Pagos programados encontrados",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    array = @ArraySchema(schema = @Schema(implementation = Pageable.class))
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "404",
                                            description = "No se encontraron pagos programados",
                                            content = @Content(schema = @Schema(example = """
                                                    {
                                                      "message": "No se encontraron pagos programados",
                                                      "content": []
                                                    }
                                                    """))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET(scheduledPaymentPath.getScheduledPayments()), handler::listenListScheduledPaymentsByFilters);
    }
}
