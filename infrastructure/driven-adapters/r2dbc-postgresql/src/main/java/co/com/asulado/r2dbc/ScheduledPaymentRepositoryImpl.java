package co.com.asulado.r2dbc;

import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Repository
public class ScheduledPaymentRepositoryImpl {

    private final DatabaseClient databaseClient;

    public ScheduledPaymentRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Flux<ScheduledPayment> findByFilters(int page, int size, String period, String identification, String identificationType) {

        StringBuilder sql = new StringBuilder("""
                    SELECT p.id AS paymentId,
                           p.period AS period,
                           pt.name AS payType,
                           (c.identification_type || ' ' || c.identification) AS identification,
                           c.name AS name,
                           ps.name AS state,
                           p.amount AS amount,
                           comp.name AS customer
                    FROM payment p
                    JOIN payment_type pt ON pt.id = p.payment_type_id
                    JOIN payment_status ps ON ps.id = p.status_id
                    JOIN customer c ON c.id = p.customer_id
                    JOIN company comp ON comp.id = c.company_id
                    LEFT JOIN scheduled_payment sp ON sp.payment_id = p.id
                    WHERE 1=1
                """);

        if (period != null && !period.isBlank()) {
            sql.append(" AND p.period = :period");
        }
        if (identification != null && !identification.isBlank()) {
            sql.append(" AND c.identification = :identification");
        }
        if (identificationType != null && !identificationType.isBlank()) {
            sql.append(" AND c.identification_type = :identificationType");
        }

        sql.append(" ORDER BY p.id LIMIT :limit OFFSET :offset");

        return databaseClient.sql(sql.toString())
                .bind("limit", size)
                .bind("offset", page * size)
                .filter((statement, executeFunction) -> {
                    if (period != null && !period.isBlank()) {
                        statement = statement.bind("period", period);
                    }
                    if (identification != null && !identification.isBlank()) {
                        statement = statement.bind("identification", identification);
                    }
                    if (identificationType != null && !identificationType.isBlank()) {
                        statement = statement.bind("identificationType", identificationType);
                    }
                    return executeFunction.execute(statement);
                })
                .map((row, metadata) -> {
                    ScheduledPayment payment = new ScheduledPayment();
                    payment.setPaymentId(row.get("paymentId", BigDecimal.class));
                    payment.setPeriod(row.get("period", String.class));
                    payment.setPayType(row.get("payType", String.class));
                    payment.setIdentification(row.get("identification", String.class));
                    payment.setName(row.get("name", String.class));
                    payment.setState(row.get("state", String.class));
                    payment.setAmount(row.get("amount", BigDecimal.class));
                    payment.setCostumer(row.get("customer", String.class));
                    return payment;
                })
                .all();
    }

}
