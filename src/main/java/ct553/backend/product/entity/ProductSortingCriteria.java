package ct553.backend.product.entity;

import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSortingCriteria {

    private Sort.Direction updatedAt;

    private Sort.Direction alphabet;

    private Sort.Direction rating;

    private Sort.Direction price;

    public boolean isEmptySortingCriteria() {
        return this.updatedAt == null && this.alphabet == null && this.rating == null && this.price == null;
    }

}
