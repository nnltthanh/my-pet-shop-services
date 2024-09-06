package ct553.backend.payment;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentSupplier {

    CASH(0),
    VN_PAY(1),
    MOMO(2);

    @Getter
    public int supplier;

    public int getValue() {
        return this.supplier;
    }

    public static PaymentSupplier from(int supplier) {
        return Stream.of(PaymentSupplier.values())
                .filter(p -> p.getSupplier() == supplier)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    
}