package ct553.backend.product.entity;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class ProductSortingCriteria {
    
    private Sort.Direction updatedAt;

    private Sort.Direction rating;

    private Sort.Direction price;

    public boolean isEmptySearchCriteria() {
        return this.updatedAt == null && this.rating == null && this.price == null;
    }
    
}
