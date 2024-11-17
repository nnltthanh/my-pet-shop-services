package ct553.backend.statistic;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.order.control.OrderRepository;
import ct553.backend.product.ProductRepository;
import ct553.backend.statistic.StatisticTimeData.StatisticFilterPeriod;

@Service
public class StatisticService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductRepository productRepository;

    public StatisticOverview getStatisticOverview() {
        LocalDate currentDate = LocalDate.now();
        LocalDate currentMonthStart = currentDate.withDayOfMonth(1);
        LocalDate currentMonthEnd = currentMonthStart.withDayOfMonth(currentMonthStart.lengthOfMonth());

        StatisticOverview statisticOverview = new StatisticOverview();
        statisticOverview.setRevenueOfMonth(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(currentMonthStart, currentMonthEnd));
        statisticOverview.setNumberOfOrdersOfMonth(this.orderRepository.countOrdersInCurrentMonth(currentMonthStart, currentMonthEnd));
        statisticOverview.setNumberOfProductsOfMonth(this.orderRepository.countNonServiceProductsInCurrentMonth(currentMonthStart, currentMonthEnd));
        statisticOverview.setNumberOfServiceProductsOfMonth(this.orderRepository.countServiceProductsInCurrentMonth(currentMonthStart, currentMonthEnd));

        return statisticOverview;
    }


    public StatisticTimeData getStatisticByFilter(StatisticFilterPeriod period) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startCurrentDate = null;
        LocalDate endCurrentDate = null;
        LocalDate startPreviousDate = null;
        LocalDate endPreviousDate = null;
        switch (period) {
            case YEAR:
                startCurrentDate = currentDate.with(TemporalAdjusters.firstDayOfYear());
                endCurrentDate = currentDate.with(TemporalAdjusters.lastDayOfYear());
                startPreviousDate = currentDate.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                endPreviousDate = currentDate.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                break;

            case QUARTER:
                startCurrentDate = this.buildQuarter(currentDate);
                endCurrentDate = startCurrentDate.plusMonths(3).minusDays(1);
                startPreviousDate = startCurrentDate.minusMonths(3);
                endPreviousDate = startPreviousDate.plusMonths(3).minusDays(1);
                break;

            case MONTH:
                startCurrentDate = currentDate.with(TemporalAdjusters.firstDayOfMonth());
                endCurrentDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
                startPreviousDate = currentDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                endPreviousDate = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                break;

            case WEEK:
                startCurrentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endCurrentDate = startCurrentDate.plusDays(6);
                startPreviousDate = currentDate.minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endPreviousDate = startPreviousDate.plusDays(6);
                break;
            default:
                break;
        }

        if (period.equals(StatisticFilterPeriod.YEAR)) {
            return buildStatisticTimeDataByYear(currentDate.getYear(), currentDate.getYear() - 1);
        } else if (period.equals(StatisticFilterPeriod.WEEK)) {
            return buildStatisticTimeDataByWeek(startCurrentDate, endCurrentDate, startPreviousDate, endPreviousDate);
        }

        return buildStatisticTimeDataByMonth(startCurrentDate, endCurrentDate, startPreviousDate, endPreviousDate);
    }

    private StatisticTimeData buildStatisticTimeDataByWeek(LocalDate startCurrentDate, LocalDate endCurrentDate, LocalDate startPreviousDate, LocalDate endPreviousDate) {
        StatisticTimeData data = new StatisticTimeData();
        List<BigDecimal> currentRevenues = new ArrayList<>();
        List<Long> currentNumberOfOrders = new ArrayList<>();
        List<Long> currentNumberOfProducts = new ArrayList<>();
        List<Long> currentNumberOfServices = new ArrayList<>();

        System.out.println(startCurrentDate + " " + endCurrentDate + " " + startPreviousDate + " " + endPreviousDate);

        LocalDate currentWeekStart = startCurrentDate;

        while (currentWeekStart.isBefore(endCurrentDate.plusDays(1))) {
            currentRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(currentWeekStart, currentWeekStart));
            currentNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(currentWeekStart, currentWeekStart));
            currentNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(currentWeekStart, currentWeekStart));
            currentNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(currentWeekStart, currentWeekStart));
            currentWeekStart = currentWeekStart.plusDays(1);
        }

        List<BigDecimal> previousRevenues = new ArrayList<>();
        List<Long> previousNumberOfOrders = new ArrayList<>();
        List<Long> previousNumberOfProducts = new ArrayList<>();
        List<Long> previousNumberOfServices = new ArrayList<>();

        LocalDate previousWeekStart = startPreviousDate;

        while (previousWeekStart.isBefore(endPreviousDate.plusDays(1))) {
            previousRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(previousWeekStart, previousWeekStart));
            previousNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(previousWeekStart, previousWeekStart));
            previousNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(previousWeekStart, previousWeekStart));
            previousNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(previousWeekStart, previousWeekStart));
            previousWeekStart = previousWeekStart.plusDays(1);
        }

        data.setCurrentTimeRevenue(currentRevenues);
        data.setCurrentNumberOfOrders(currentNumberOfOrders);
        data.setCurrentNumberOfProducts(currentNumberOfProducts);
        data.setCurrentNumberOfServices(currentNumberOfServices);

        data.setPreviousTimeRevenue(previousRevenues);
        data.setPreviousNumberOfOrders(previousNumberOfOrders);
        data.setPreviousNumberOfProducts(previousNumberOfProducts);
        data.setPreviousNumberOfServices(previousNumberOfServices);

        data.setPeriod(StatisticFilterPeriod.WEEK);
        return data;
    }


    private StatisticTimeData buildStatisticTimeDataByYear(int currentYear, int lastYear) {
        StatisticTimeData data = new StatisticTimeData();
        List<BigDecimal> currentRevenues = new ArrayList<>();
        List<Long> currentNumberOfOrders = new ArrayList<>();
        List<Long> currentNumberOfProducts = new ArrayList<>();
        List<Long> currentNumberOfServices = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);

            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            currentRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            currentNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            currentNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            currentNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(firstDayOfMonth, lastDayOfMonth));

        }

        List<BigDecimal> previousRevenues = new ArrayList<>();
        List<Long> previousNumberOfOrders = new ArrayList<>();
        List<Long> previousNumberOfProducts = new ArrayList<>();
        List<Long> previousNumberOfServices = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(lastYear, month);

            LocalDate firstDayOfMonth = yearMonth.atDay(1);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            previousRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            previousNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            previousNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(firstDayOfMonth, lastDayOfMonth));
            previousNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(firstDayOfMonth, lastDayOfMonth));

        }

        data.setCurrentTimeRevenue(currentRevenues);
        data.setCurrentNumberOfOrders(currentNumberOfOrders);
        data.setCurrentNumberOfProducts(currentNumberOfProducts);
        data.setCurrentNumberOfServices(currentNumberOfServices);

        data.setPreviousTimeRevenue(previousRevenues);
        data.setPreviousNumberOfOrders(previousNumberOfOrders);
        data.setPreviousNumberOfProducts(previousNumberOfProducts);
        data.setPreviousNumberOfServices(previousNumberOfServices);

        data.setPeriod(StatisticFilterPeriod.YEAR);
        return data;
    }

    private StatisticTimeData buildStatisticTimeDataByMonth(LocalDate startCurrentDate, LocalDate endCurrentDate, LocalDate startPreviousDate, LocalDate endPreviousDate) {
        StatisticTimeData data = new StatisticTimeData();
        List<BigDecimal> currentRevenues = new ArrayList<>();
        List<Long> currentNumberOfOrders = new ArrayList<>();
        List<Long> currentNumberOfProducts = new ArrayList<>();
        List<Long> currentNumberOfServices = new ArrayList<>();

        System.out.println(startCurrentDate + " " + endCurrentDate + " " + startPreviousDate + " " + endPreviousDate);

        LocalDate currentWeekStart = startCurrentDate;

        while (currentWeekStart.isBefore(endCurrentDate.plusDays(1))) {
            LocalDate currentWeekEnd = currentWeekStart.plusDays(6);
            if (currentWeekEnd.isAfter(endCurrentDate)) {
                currentWeekEnd = endCurrentDate;
            }
            currentRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(currentWeekStart, currentWeekEnd));
            currentNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(currentWeekStart, currentWeekEnd));
            currentNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(currentWeekStart, currentWeekEnd));
            currentNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(currentWeekStart, currentWeekEnd));
            currentWeekStart = currentWeekEnd.plusDays(1);
        }

        List<BigDecimal> previousRevenues = new ArrayList<>();
        List<Long> previousNumberOfOrders = new ArrayList<>();
        List<Long> previousNumberOfProducts = new ArrayList<>();
        List<Long> previousNumberOfServices = new ArrayList<>();

        LocalDate previousWeekStart = startPreviousDate;

        while (previousWeekStart.isBefore(endPreviousDate.plusDays(1))) {
            LocalDate previousWeekEnd = previousWeekStart.plusDays(6);
            if (previousWeekEnd.isAfter(endPreviousDate)) {
                previousWeekEnd = endPreviousDate;
            }
            previousRevenues.add(this.orderRepository.sumOfFinishedOrdersInCurrentMonth(previousWeekStart, previousWeekEnd));
            previousNumberOfOrders.add(this.orderRepository.countOrdersInCurrentMonth(previousWeekStart, previousWeekEnd));
            previousNumberOfProducts.add(this.orderRepository.countNonServiceProductsInCurrentMonth(previousWeekStart, previousWeekEnd));
            previousNumberOfServices.add(this.orderRepository.countServiceProductsInCurrentMonth(previousWeekStart, previousWeekEnd));
            previousWeekStart = previousWeekEnd.plusDays(1);
        }

        data.setCurrentTimeRevenue(currentRevenues);
        data.setCurrentNumberOfOrders(currentNumberOfOrders);
        data.setCurrentNumberOfProducts(currentNumberOfProducts);
        data.setCurrentNumberOfServices(currentNumberOfServices);

        data.setPreviousTimeRevenue(previousRevenues);
        data.setPreviousNumberOfOrders(previousNumberOfOrders);
        data.setPreviousNumberOfProducts(previousNumberOfProducts);
        data.setPreviousNumberOfServices(previousNumberOfServices);

        data.setPeriod(StatisticFilterPeriod.MONTH);
        return data;
    }

    private LocalDate buildQuarter(LocalDate currentDate) {

        // First day of the current quarter
        LocalDate firstDayOfCurrentQuarter;
        int currentMonth = currentDate.getMonthValue();
        if (currentMonth >= 1 && currentMonth <= 3) {
            firstDayOfCurrentQuarter = LocalDate.of(currentDate.getYear(), Month.JANUARY, 1);
        } else if (currentMonth >= 4 && currentMonth <= 6) {
            firstDayOfCurrentQuarter = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);
        } else if (currentMonth >= 7 && currentMonth <= 9) {
            firstDayOfCurrentQuarter = LocalDate.of(currentDate.getYear(), Month.JULY, 1);
        } else {
            firstDayOfCurrentQuarter = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 1);
        }

        return firstDayOfCurrentQuarter;
    }

    public StatisticTimeDataReport getReportStatisticByFilter(StatisticFilterPeriod period) {
        LocalDate currentDate = LocalDate.now();
        LocalDate startCurrentDate = null;
        LocalDate endCurrentDate = null;
        switch (period) {
            case YEAR:
                startCurrentDate = currentDate.with(TemporalAdjusters.firstDayOfYear());
                endCurrentDate = currentDate.with(TemporalAdjusters.lastDayOfYear());
                break;

            case QUARTER:
                startCurrentDate = this.buildQuarter(currentDate);
                endCurrentDate = startCurrentDate.plusMonths(3).minusDays(1);
                break;

            case MONTH:
                startCurrentDate = currentDate.with(TemporalAdjusters.firstDayOfMonth());
                endCurrentDate = currentDate.with(TemporalAdjusters.lastDayOfMonth());
                break;

            case WEEK:
                startCurrentDate = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                endCurrentDate = startCurrentDate.plusDays(6);
                break;
            default:
                break;
        }
        StatisticTimeDataReport data = new StatisticTimeDataReport();
        data.setOrdersByPeriods(orderRepository.getOrdersInCurrentMonth(startCurrentDate, endCurrentDate));
        data.setProductsByPeriods(orderRepository.getNonServiceProductsInCurrentMonth(startCurrentDate, endCurrentDate));
        data.setServicesByPeriods(orderRepository.getServiceProductsInCurrentMonth(startCurrentDate, endCurrentDate));
        data.setRevenuesByPeriods(
            orderRepository.getFinishedOrdersInCurrentMonth(startCurrentDate, endCurrentDate)
            // .stream()
            // .map(o -> {
            //     return new DateRevenueReport(o.getCreateDate(), o.getTotal());
            // })
            // .sorted(Comparator.comparing(DateRevenueReport::getDate))
            // .toList()
            );
        data.setPeriod(period);

        return data;
    }
    
}
