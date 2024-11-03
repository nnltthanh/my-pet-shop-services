package ct553.backend.product.service;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import ct553.backend.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Component
@Entity
@DiscriminatorValue("service_product")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ServiceProduct extends Product {

    @Column
    @Enumerated(EnumType.ORDINAL)
    private ServiceProductType type;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private ServiceProductLevel level;

    @OneToMany(mappedBy = "serviceProduct")
    Set<PetCustomerServiceProduct> petServices;

}
