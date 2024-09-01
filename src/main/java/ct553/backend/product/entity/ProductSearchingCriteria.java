package ct553.backend.product.entity;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ct553.backend.pet.PetBreed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchingCriteria {
    
    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    private List<PetBreed> breeds;

    public boolean isEmptySearchingCriteria() {
        return this.priceFrom == null && this.priceTo == null && !CollectionUtils.isEmpty(breeds);
    }

    public boolean hasBreeds() {
        return !CollectionUtils.isEmpty(breeds);
    }
    
}
