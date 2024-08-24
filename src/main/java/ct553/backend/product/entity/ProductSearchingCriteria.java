package ct553.backend.product.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchingCriteria {
    
    private BigDecimal priceFrom;

    private BigDecimal priceTo;

    public boolean isEmptySearchingCriteria() {
        return this.priceFrom == null && this.priceTo == null;
    }
    
}
