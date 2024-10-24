package ct553.backend.order.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import ct553.backend.customer.Customer;
import ct553.backend.payment.Payment;
import ct553.backend.shipment.Shipment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@Table(name = "customer_order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // @ManyToOne
    // @JoinColumn(name = "coupon_id")
    // private Coupon coupon; // check later

    // @ManyToOne
    // @JoinColumn(name = "staff_id")
    // private Staff staff;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    // @ManyToOne
    // @JoinColumn(name = "warehouse_id")
    // private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    // @JsonIgnore
    private Customer customer;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    // @JsonIgnore
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public static Order from(OrderCreationRequest request, Customer customer) {
        return Order.builder()
                .customer(customer)
                .total(request.getTotal())
                .payment(request.getPayment())
                .shipment(request.getShipment())
                .status(request.getStatus())
                .note(request.getNote())
                .build();
    }

}
