package ct553.backend.statistic;

import java.time.LocalDate;
import java.util.List;

import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.product.service.PetCustomerServiceProduct;
import ct553.backend.statistic.StatisticTimeData.StatisticFilterPeriod;
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
public class StatisticTimeDataReport {

    private StatisticFilterPeriod period;

    private String startDate;

    private String endDate;

    List<Order> ordersByPeriods;

    List<OrderDetail> productsByPeriods;

    List<PetCustomerServiceProduct> servicesByPeriods;

    List<DateRevenueReport> revenuesByPeriods;

}
