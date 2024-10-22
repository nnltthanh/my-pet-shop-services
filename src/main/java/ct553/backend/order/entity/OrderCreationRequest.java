package ct553.backend.order.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ct553.backend.payment.Payment;
import ct553.backend.shipment.Shipment;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class OrderCreationRequest {
   
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Payment payment;

    private Shipment shipment;

    private Long customerId;

    private String note;

    private List<Long> cartDetails = new ArrayList<>();

}
