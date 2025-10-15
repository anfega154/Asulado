package co.com.asulado.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routers.paths")
public class ScheduledPaymentPath {
    private String scheduledPayments;
}
