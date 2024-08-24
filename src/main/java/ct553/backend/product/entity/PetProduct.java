package ct553.backend.product.entity;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ct553.backend.Gender;
import ct553.backend.pet.entity.PetCategory;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Component
@Entity
@DiscriminatorValue("pet_product")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class PetProduct extends Product {
   
    @Column
    private float weight;

    @Column
    private float age;

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
    private String healthRecord;

    @Column
    private String vaccination;

}
