package ct553.backend.product.entity;

import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ServiceProductLevel {
    
    CLASSIC(0),
    VIP(1);

    @Getter
    public int level;

    public int getValue() {
        return this.level;
    }

    public static ServiceProductLevel from(int level) {
        return Stream.of(ServiceProductLevel.values())
                .filter(p -> p.getLevel() == level)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

}
