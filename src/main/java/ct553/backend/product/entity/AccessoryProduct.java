package ct553.backend.product.entity;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ct553.backend.pet.entity.PetBreed;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Component
@Entity
@DiscriminatorValue("accessory_product")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class AccessoryProduct extends Product {
    
    @Column
    private String color;

    @Column
    private String size;

    @Column
    private PetBreed suitableFor;

}
