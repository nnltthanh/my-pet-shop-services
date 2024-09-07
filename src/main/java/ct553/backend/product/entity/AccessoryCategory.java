package ct553.backend.product.entity;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccessoryCategory {
    
    GENERAL_ACCESSORY(0),
    FOOD_ACCESSORY(1),
    PET_FOOD(2),
    PET_SANITARY(3), // ve sinh
    PET_MEDICINE(4),
    PET_HOUSE(5),
    PET_CLOTHING(6),
    OTHER(7);

    @Getter
    public final int type;

    public static AccessoryCategory from(int type) {
        return Stream.of(AccessoryCategory.values())
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}