package ct553.backend.product.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonFormat;

import ct553.backend.Gender;
import ct553.backend.pet.entity.PetCategory;
import ct553.backend.pet.healthrecord.HealthRecord;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@DiscriminatorValue("pet_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@EntityListeners(AuditingEntityListener.class)
public class PetProduct extends Product {

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is missing")
    private PetCategory category;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Column
    private String color;

    @Column
    private String origin;
    
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private LocalDate dateOfBirth;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "petProduct", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<HealthRecord> healthRecord = new ArrayList<>();

    @Transient
    private HealthRecord latestHealthRecord;

    public PetProduct(Long id) {
        super(id);
    }

    public PetProduct(PetProduct petProduct) {
        super(petProduct);
        this.quantity = petProduct.quantity;
        this.category = petProduct.category;
        this.gender = petProduct.gender;
        this.color = petProduct.color;
        this.origin = petProduct.origin;
        this.dateOfBirth = petProduct.dateOfBirth;
        this.healthRecord = new ArrayList<>(petProduct.healthRecord);
        this.latestHealthRecord = petProduct.latestHealthRecord;
    }

    public PetProduct(PetProduct petProduct, InventoryStatus inventoryStatus, long countSold) {
        super(petProduct, inventoryStatus, countSold);
        this.quantity = petProduct.quantity;
        this.category = petProduct.category;
        this.gender = petProduct.gender;
        this.color = petProduct.color;
        this.origin = petProduct.origin;
        this.dateOfBirth = petProduct.dateOfBirth;
        this.healthRecord = new ArrayList<>(petProduct.healthRecord);
        this.latestHealthRecord = petProduct.latestHealthRecord;
    }

}
