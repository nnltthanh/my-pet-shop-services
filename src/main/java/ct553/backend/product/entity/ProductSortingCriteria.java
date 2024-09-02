package ct553.backend.product.entity;

import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSortingCriteria {

    private List<String> ascValues;

    private List<String> descValues;

    public boolean isEmptySortingCriteria() {
        return CollectionUtils.isEmpty(ascValues) && CollectionUtils.isEmpty(descValues);
    }

}
