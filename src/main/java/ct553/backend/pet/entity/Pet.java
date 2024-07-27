package ct553.backend.pet.entity;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ct553.backend.Gender;
import ct553.backend.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Entity
@DiscriminatorValue("pet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Pet extends Product {
   
    @Column
    private float weight;

    @Column
    private float age;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull(message = "Category is missing")
    private PetCategory category;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private String origin;

    @Column
    private String healthRecord;

    @Column
    private String vaccination;

}
