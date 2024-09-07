package ct553.backend.product.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ct553.backend.pet.entity.PetBreed;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchingCriteria {
    
    @Nullable
    private BigDecimal priceFrom = new BigDecimal(0);

    @Nullable
    private BigDecimal priceTo = new BigDecimal(Integer.MAX_VALUE);

    @Nullable
    private List<PetBreed> breeds = new ArrayList<>(Arrays.asList(PetBreed.values()));
    
    private List<AccessoryCategory> accessoryCategories = new ArrayList<>(Arrays.asList(AccessoryCategory.values()));

    public boolean isEmptySearchingCriteria() {
        return this.priceFrom == null && this.priceTo == null && !CollectionUtils.isEmpty(breeds);
    }

    public boolean hasBreeds() {
        return !CollectionUtils.isEmpty(breeds);
    }
    
}
