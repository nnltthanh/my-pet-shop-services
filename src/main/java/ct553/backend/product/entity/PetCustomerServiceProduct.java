package ct553.backend.product.entity;

import java.time.LocalDateTime;

import ct553.backend.pet.entity.PetCustomer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_customer_service_product")
public class PetCustomerServiceProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_customer_service_product_id")
    private Long id;
    
    @Column
    private LocalDateTime serveFrom;

    @Column
    private LocalDateTime serveTo;

    @ManyToOne
    @JoinColumn(name = "pet_customer_id")
    PetCustomer serveFor;

    @ManyToOne
    @JoinColumn(name = "product_id")
    ServiceProduct serviceProduct;

}
