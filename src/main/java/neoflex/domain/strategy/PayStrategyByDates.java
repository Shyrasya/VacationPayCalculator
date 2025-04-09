package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;

import java.time.LocalDate;

public class PayStrategyByDates implements PayStrategy {
    private final Double averageSalary;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final DayStatusService dayStatusService;

    public PayStrategyByDates(Double averageSalary, LocalDate startDate, LocalDate endDate, DayStatusService dayStatusService) {
        this.averageSalary = averageSalary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayStatusService = dayStatusService;
    }

    @Override
    public double calculate() {
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        int workDays = countWorkDays(startDate, endDate);
        return roundToTwoDigits(oneDayPay * workDays);
    }

    private int countWorkDays(LocalDate startDate, LocalDate endDate) {
        int workDays = 0;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (dayStatusService.isWorkDay(currentDate)) {
                workDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workDays;
    }


}
