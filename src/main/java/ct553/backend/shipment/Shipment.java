package ct553.backend.shipment;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import ct553.backend.address.Address;
import ct553.backend.product.entity.ProductDetail;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@Table(name = "shipment")
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ship_date", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date shipDate;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "method", length = 100)
    private String method;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(precision = 10, scale = 2)
    private BigDecimal shipCost;

}
