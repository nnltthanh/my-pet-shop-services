package ct553.backend.product;

import java.math.BigDecimal;
import java.util.Date;

import ct553.backend.imagedata.ImageData;
import ct553.backend.product.pet.PetProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductOverview {

    private Long id;

    private String name;

    private String engName;

    private BigDecimal price;

    private String description;

    private ImageData imageData;

    private Date updatedAt;

    private String productType;

    private long countRating;

    private double rating;

    private long countSold;

    public static ProductOverview fromProduct(Product product) {
        return ProductOverview.builder()
                .id(product.getId())
                .name(product.getName())
                .engName(product.getEngName())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageData(product.getImageData())
                .updatedAt(product.getUpdatedAt())
                .productType("pet_product")
                .countRating(product.getCountRating())
                .rating(product.getRating())
                .countSold(product.getCountSold())
                .build();
    }

    public static ProductOverview fromPetProduct(PetProduct product) {
        return ProductOverview.builder()
                .id(product.getId())
                .name(product.getName())
                .engName(product.getEngName())
                .price(product.getPrice())
                .description(product.getDescription())
                .imageData(product.getImageData())
                .updatedAt(product.getUpdatedAt())
                .productType(product.getCategory().getBreed().getValue())
                .countRating(product.getCountRating())
                .rating(product.getRating())
                .countSold(product.getCountSold())
                .build();
    }

}
