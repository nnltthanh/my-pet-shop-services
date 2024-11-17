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

    public String getVietnameseValue() {
        if (this == ServiceProductType.SPA_GROOMING) {
            return "Spa - Cắt tỉa lông";
        }
        if (this == ServiceProductType.PET_HOTEL) {
            return "Khách sạn thú cưng";
        }
        if (this == ServiceProductType.HOSPITAL) {
            return "Khám chữa bệnh";
        }
        return "Khác";
    }

    public static ServiceProductType from(int type) {
        return Stream.of(ServiceProductType.values())
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
