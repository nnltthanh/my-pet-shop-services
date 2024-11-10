package ct553.backend.statistic;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
// @JsonSubTypes({
//     @JsonSubTypes.Type(value = StatisticMonth.class, name = "MONTH"),
//     @JsonSubTypes.Type(value = StatisticWeek.class, name = "WEEK"),
//     @JsonSubTypes.Type(value = StatisticYear.class, name = "YEAR"),
//     @JsonSubTypes.Type(value = StatisticQuarter.class, name = "QUARTER")
// })
public class StatisticTimeData {

    public enum StatisticFilterType {
        REVENUE,
        NUMBER_OF_ORDERS,
        NUMBER_OF_PRODUCTS,
        NUMBER_OF_SERVICES
    }

    public enum StatisticFilterPeriod {
        YEAR,
        QUARTER,
        MONTH,
        WEEK,
    }

    private List<String> labels;

    private StatisticFilterType type;

    private StatisticFilterPeriod period;

    private List<Long> previousNumberOfOrders; // last month / year ...

    private List<Long> currentNumberOfOrders; // current month / year ...

    private List<Long> previousNumberOfProducts; // last month / year ...

    private List<Long> currentNumberOfProducts; // current month / year ...

    private List<Long> previousNumberOfServices; // last month / year ...

    private List<Long> currentNumberOfServices; // current month / year ...

    private List<BigDecimal> previousTimeRevenue; // last month / year ...

    private List<BigDecimal> currentTimeRevenue; // current month / year ...

}
