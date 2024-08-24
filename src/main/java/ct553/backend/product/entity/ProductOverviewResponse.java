package ct553.backend.product.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOverviewResponse {
    
    private Long total;
    
    private List<Product> data;

}
