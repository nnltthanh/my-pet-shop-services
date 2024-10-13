package ct553.backend.product.entity;

import java.math.BigDecimal;

import ct553.backend.coupon.Coupon;
import ct553.backend.imagedata.ImageData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailDTO {

    private Long id;

    private BigDecimal price;

    private int quantity;

    private int sold;

    private ImageData imageData;

    private Coupon coupon;

    private InventoryStatus inventoryStatus;

    public static ProductDetailDTO from(ProductDetail productDetail) {
        return ProductDetailDTO.builder()
                    .id(productDetail.getId())
                    .quantity(productDetail.getQuantity())
                    .price(productDetail.getPrice())
                    .sold(productDetail.getSold())
                    .imageData(productDetail.getImageData())
                    .coupon(productDetail.getCoupon())
                    .inventoryStatus(productDetail.getInventoryStatus())
                    .build();
    }

}
