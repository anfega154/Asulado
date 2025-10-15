package co.com.asulado.r2dbc;

import co.com.asulado.r2dbc.entity.ScheduledPaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.FetchSpec;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.mockito.stubbing.Answer;

class ScheduledPaymentRepositoryImplTest {

    private DatabaseClient databaseClient;
    private DatabaseClient.GenericExecuteSpec spec;
    private FetchSpec fetchSpec;
    private ScheduledPaymentRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        databaseClient = mock(DatabaseClient.class);
        spec = mock(DatabaseClient.GenericExecuteSpec.class);
        fetchSpec = mock(FetchSpec.class);
        repository = new ScheduledPaymentRepositoryImpl(databaseClient);
    }

    @Test
    void testFindByFilters_withAllParams() {
        when(databaseClient.sql(anyString())).thenReturn(spec);
        when(spec.bind(anyString(), any())).thenReturn(spec);
        when(spec.map(any(BiFunction.class))).thenReturn(fetchSpec);

        ScheduledPaymentEntity mockEntity = new ScheduledPaymentEntity();
        mockEntity.setPaymentId(BigDecimal.ONE);
        mockEntity.setPeriod("202401");
        mockEntity.setPayType("Credit");
        mockEntity.setIdentification("CC 12345");
        mockEntity.setName("John Doe");
        mockEntity.setState("Active");
        mockEntity.setAmount(BigDecimal.valueOf(1000));
        mockEntity.setCustomer("Asulado");

        when(fetchSpec.all()).thenReturn(Flux.just(mockEntity));

        Flux<ScheduledPaymentEntity> result = repository.findByFilters(
                0, 10, "202401", "12345", "CC", 1L
        );

        StepVerifier.create(result)
                .assertNext(entity -> {
                    assertEquals("202401", entity.getPeriod());
                    assertEquals("Asulado", entity.getCustomer());
                    assertEquals(BigDecimal.ONE, entity.getPaymentId());
                })
                .verifyComplete();

        verify(databaseClient).sql(contains("SELECT"));
        verify(spec, times(6)).bind(anyString(), any());
        verify(spec).map(any(BiFunction.class));
    }

    @Test
    void testFindByFilters_withNullAndBlankParams() {
        when(databaseClient.sql(anyString())).thenReturn(spec);
        when(spec.bind(anyString(), any())).thenReturn(spec);
        when(spec.map(any(BiFunction.class))).thenReturn(fetchSpec);
        when(fetchSpec.all()).thenReturn(Flux.empty());

        Flux<ScheduledPaymentEntity> result = repository.findByFilters(
                1, 5, " ", null, "", null
        );

        StepVerifier.create(result)
                .verifyComplete();

        verify(spec, times(2)).bind(anyString(), any());
    }

    @Test
    void testGetStringBuilder_withAllConditions() {
        StringBuilder sql = invokeGetStringBuilder("202401", "12345", "CC", 5L);

        String sqlString = sql.toString();
        assertTrue(sqlString.contains("p.period = :period"));
        assertTrue(sqlString.contains("c.identification = :identification"));
        assertTrue(sqlString.contains("c.identification_type = :identificationType"));
        assertTrue(sqlString.contains("p.id = :id"));
        assertTrue(sqlString.contains("ORDER BY p.id LIMIT :limit OFFSET :offset"));
    }

    @Test
    void testGetStringBuilder_withNullValues() {
        StringBuilder sql = invokeGetStringBuilder(null, " ", null, null);
        String sqlString = sql.toString();

        assertFalse(sqlString.contains(":period"));
        assertFalse(sqlString.contains(":identificationType"));
        assertTrue(sqlString.contains("ORDER BY p.id"));
    }

    @Test
    void testMapperFunctionIsExecuted() {
        // Mock de la fila y metadatos
        Row row = mock(Row.class);
        RowMetadata metadata = mock(RowMetadata.class);

        // Configurar retornos del row.get(...)
        when(row.get("paymentId", BigDecimal.class)).thenReturn(BigDecimal.ONE);
        when(row.get("period", String.class)).thenReturn("202401");
        when(row.get("payType", String.class)).thenReturn("Credit");
        when(row.get("identification", String.class)).thenReturn("CC 12345");
        when(row.get("name", String.class)).thenReturn("John Doe");
        when(row.get("state", String.class)).thenReturn("Active");
        when(row.get("amount", BigDecimal.class)).thenReturn(BigDecimal.valueOf(1000));
        when(row.get("customer", String.class)).thenReturn("Asulado");

        when(databaseClient.sql(anyString())).thenReturn(spec);
        when(spec.bind(anyString(), any())).thenReturn(spec);

        when(spec.map(any(BiFunction.class))).thenAnswer((Answer<FetchSpec<ScheduledPaymentEntity>>) invocation -> {
            @SuppressWarnings("unchecked")
            var mapper = (BiFunction<Row, RowMetadata, ScheduledPaymentEntity>) invocation.getArgument(0);
            ScheduledPaymentEntity mapped = mapper.apply(row, metadata);

            FetchSpec<ScheduledPaymentEntity> localFetchSpec = mock(FetchSpec.class);
            when(localFetchSpec.all()).thenReturn(Flux.just(mapped));
            return localFetchSpec;
        });

        Flux<ScheduledPaymentEntity> result = repository.findByFilters(
                0, 10, "202401", "12345", "CC", 1L
        );

        StepVerifier.create(result)
                .assertNext(payment -> {
                    assertEquals("202401", payment.getPeriod());
                    assertEquals("Asulado", payment.getCustomer());
                    assertEquals(BigDecimal.valueOf(1000), payment.getAmount());
                })
                .verifyComplete();

        verify(row, times(8)).get(anyString(), any());
    }

    private StringBuilder invokeGetStringBuilder(String period, String identification, String identificationType, Long id) {
        try {
            var method = ScheduledPaymentRepositoryImpl.class.getDeclaredMethod(
                    "getStringBuilder", String.class, String.class, String.class, Long.class
            );
            method.setAccessible(true);
            return (StringBuilder) method.invoke(null, period, identification, identificationType, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
