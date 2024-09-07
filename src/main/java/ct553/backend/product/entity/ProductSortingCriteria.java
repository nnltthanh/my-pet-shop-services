package ct553.backend.product.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSortingCriteria {

    private List<String> asc = new ArrayList<>();

    private List<String> desc = new ArrayList<>();

    public boolean isEmptySortingCriteria() {
        return CollectionUtils.isEmpty(asc) && CollectionUtils.isEmpty(desc);
    }

}
