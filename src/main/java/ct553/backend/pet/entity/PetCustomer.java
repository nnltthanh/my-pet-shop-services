package ct553.backend.pet.entity;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;

import ct553.backend.Gender;
import ct553.backend.customer.Customer;
import ct553.backend.pet.healthrecord.HealthRecord;
import ct553.backend.product.entity.PetCustomerServiceProduct;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@Data
@Builder
@Table(name = "pet_customer")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PetCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "serveFor")
    Set<PetCustomerServiceProduct> petServices;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "Reference customer is missing")
    private Customer customer;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column
    private String color;

    @Column
    private String origin;
    
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateOfBirth;

    @OneToOne
    private HealthRecord healthRecord;

}
