package ct553.backend.statistic;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticOverview {

    private BigDecimal revenueOfMonth;

    private Long numberOfOrdersOfMonth;

    private Long numberOfProductsOfMonth;

    private Long numberOfServiceProductsOfMonth;

}
