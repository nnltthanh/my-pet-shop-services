package ct553.backend.product.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOverviewResponse {

    private Long total;

    private List<ProductOverview> data;

    public static ProductOverviewResponse fromProducts(Long total, List<Product> products) {
        return ProductOverviewResponse.builder()
                .total(total)
                .data(products.stream().map(ProductOverview::fromProduct).toList())
                .build();
    }

    public static ProductOverviewResponse fromPetProducts(Long total, List<PetProduct> products) {
        return ProductOverviewResponse.builder()
                .total(total)
                .data(products.stream().map(ProductOverview::fromPetProduct).toList())
                .build();
    }

}
