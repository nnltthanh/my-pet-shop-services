package ct553.backend.product.entity;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum InventoryStatus {

    ON_HAND("Còn hàng"),
    INCOMING("Sắp có hàng"),
    SOLD_OUT("Hết hàng");

    @Getter
    public String value;

    public static InventoryStatus from(String value) {
        return Stream.of(InventoryStatus.values())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}