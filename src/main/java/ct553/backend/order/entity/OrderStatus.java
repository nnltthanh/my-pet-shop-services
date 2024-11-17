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

    public String getVietnameseValue() {
        if (this == OrderStatus.CREATED) {
            return "Tạo đơn";
        }
        if (this == OrderStatus.PAYMENT) {
            return "Đã thanh toán";
        }
        if (this == OrderStatus.PROCESSING) {
            return "Đang xử lý";
        }
        if (this == OrderStatus.SHIPPING) {
            return "Đang giao hàng";
        }
        if (this == OrderStatus.DELIVERED) {
            return "Đã giao hàng";
        }
        if (this == OrderStatus.CANCELLED) {
            return "Huỷ đơn";
        }
        if (this == OrderStatus.REFUNDED) {
            return "Hoàn tiền";
        }
        if (this == OrderStatus.ON_HOLD) {
            return "Tạm giữ";
        }
        if (this == OrderStatus.FINISHED) {
            return "Hoàn thành";
        }
        return "";
    }

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
