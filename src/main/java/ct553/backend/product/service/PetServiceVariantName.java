package ct553.backend.product.service;

public enum PetServiceVariantName {

    WEIGHT,
    SIZE,
    COLOR,
    TIME,
    FURRY_LENGTH,
    OTHER;

    public String getVietnameseValue() {
        if (this == PetServiceVariantName.WEIGHT) {
            return "Cân nặng";
        }

        if (this == PetServiceVariantName.SIZE) {
            return "Kích cỡ";
        }

        if (this == PetServiceVariantName.COLOR) {
            return "Màu sắc";
        }

        if (this == PetServiceVariantName.TIME) {
            return "Thời gian";
        }

        if (this == PetServiceVariantName.FURRY_LENGTH) {
            return "Độ dài lông";
        }

        return "Khác";
    }

}
