package ct553.backend.product.service;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServiceProductType {
    
    SPA_GROOMING(0),
    PET_HOTEL(1),
    HOSPITAL(2),
    OTHER(3);

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
