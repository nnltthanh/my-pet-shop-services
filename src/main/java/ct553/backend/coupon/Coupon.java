package ct553.backend.coupon;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Component;

import ct553.backend.employee.Employee;
import ct553.backend.product.entity.ProductDetail;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @Column(length = 20)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private CouponType type;

    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    @Column
    private float percent;

    @Column(name = "start_date")
    @Temporal(value = TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(value = TemporalType.DATE)
    private Date endDate;

    @Column(name = "min_spend", precision = 10, scale = 2)
    private BigDecimal minSpend;

    // cappedAt > minSpend
    @Column(name = "capped_at", precision = 10, scale = 2)
    private BigDecimal cappedAt;

    @Column(name = "uses_per_coupon")
    private int usesPerCoupon;

    @Column(length = 20)
    private String status;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updatedAt;

    @OneToMany(mappedBy = "coupon")
    private Set<ProductDetail> productDetails;

}