package ct553.backend.statistic;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateRevenueReport {
    private LocalDate date;
    private BigDecimal revenue;
}
