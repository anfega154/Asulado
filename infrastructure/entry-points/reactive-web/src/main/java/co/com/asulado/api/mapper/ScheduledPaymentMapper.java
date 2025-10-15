package co.com.asulado.api.mapper;

import co.com.asulado.api.dto.PageableDTO;
import co.com.asulado.api.dto.ScheduledPaymentDTO;
import co.com.asulado.model.pageable.Pageable;
import co.com.asulado.model.scheduledpayment.ScheduledPayment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScheduledPaymentMapper {
    PageableDTO<ScheduledPaymentDTO> toResponse(Pageable<ScheduledPayment> pageable);
}
