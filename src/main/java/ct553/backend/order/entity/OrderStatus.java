package ct553.backend.order.entity;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    CREATED,
    PAYMENT,
    PROCESSING,
    SHIPPING,
    DELIVERED,
    CANCELLED,
    REFUNDED,
    ON_HOLD,
    FINISHED;

    private static final Map<String, OrderStatus> stringToEnum = new HashMap<>();

    static {
        for (OrderStatus orderStatus : values()) {
            stringToEnum.put(orderStatus.name(), orderStatus);
        }
    }

    public static OrderStatus fromString(String value) {
        return stringToEnum.get(value);
    }
}
