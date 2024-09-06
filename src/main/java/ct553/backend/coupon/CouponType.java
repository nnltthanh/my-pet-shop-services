package ct553.backend.coupon;

import java.util.stream.Stream;

import ct553.backend.product.entity.ServiceProductType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CouponType {

    PRODUCT(0),
    PRODUCT_DETAIL(1),
    PRODUCT_TYPE(2);

    @Getter
    public int type;

    public int getValue() {
        return this.type;
    }

    public static ServiceProductType from(int type) {
        return Stream.of(ServiceProductType.values())
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
