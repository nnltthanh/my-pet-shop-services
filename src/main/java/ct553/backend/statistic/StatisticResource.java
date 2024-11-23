package ct553.backend.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.auth.RoleName;
import ct553.backend.statistic.StatisticTimeData.StatisticFilterPeriod;
import ct553.backend.statistic.StatisticTimeData.StatisticFilterType;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.ADMIN})
public class StatisticResource {
    
    @Autowired
    StatisticService statisticService;

    @GetMapping
    public StatisticOverview getStatisticOverview() {
        return this.statisticService.getStatisticOverview();
    }

    @GetMapping("/period/{period}")
    public StatisticTimeData getStatisticOverviewByFilter(@PathVariable StatisticFilterPeriod period) {
        return this.statisticService.getStatisticByFilter(period);
    }
    
    @GetMapping("/period/{period}/report")
    public StatisticTimeDataReport getStatisticReportByFilter(@PathVariable StatisticFilterPeriod period) {
        return this.statisticService.getReportStatisticByFilter(period);
    }

}
