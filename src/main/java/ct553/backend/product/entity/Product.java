package ct553.backend.product.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import ct553.backend.imagedata.ImageData;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Validated
@Entity
@Data
@Builder(toBuilder = true)
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "Name is missing")
    private String name;

    @Column
    private String engName;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_data_id")
    private ImageData imageData;

    @Column(name = "created_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;

    @Transient
    private InventoryStatus inventoryStatus;

    @Transient 
    private long countSold;

    @Transient
    private List<ProductDetailDTO> productDetails = new ArrayList<>();

    @Transient
    public String getProductType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Product product) {
        this.id = product.id;
        this.name = product.name;
        this.engName = product.engName;
        this.price = product.price;
        this.description = product.description;
        this.imageData = product.imageData;
        this.createdAt = product.createdAt;
        this.updatedAt = product.updatedAt;
    }

    public Product(Product product, InventoryStatus inventoryStatus, long countSold) {
        this(product);
        this.inventoryStatus = inventoryStatus;
        this.countSold = countSold;
    }

}
