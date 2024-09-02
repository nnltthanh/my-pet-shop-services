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
public class PetProductOverviewResponse {

    private Long total;

    private List<PetProduct> data;

    public static PetProductOverviewResponse from(Long total, List<PetProduct> products) {
        return PetProductOverviewResponse.builder()
                .total(total)
                .data(products)
                .build();
    }

}
