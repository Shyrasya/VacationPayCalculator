package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;

import java.time.LocalDate;

public class PayStrategyBuilder {
    private Double averageSalary;
    private Integer vacationDays;
    private LocalDate startDate;
    private LocalDate endDate;


    private final DayStatusService dayStatusService;

    public PayStrategyBuilder(DayStatusService dayStatusService) {
        this.dayStatusService = dayStatusService;
    }

    public PayStrategyBuilder withAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
        return this;
    }

    public PayStrategyBuilder withVacationDays(Integer vacationDays) {
        this.vacationDays = vacationDays;
        return this;
    }

    public PayStrategyBuilder withStartEndDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public PayStrategy build() {
        validateAverageSalary(averageSalary);
        if (startDate != null && endDate != null) {
            validateVacationDates(startDate, endDate);
            if (dayStatusService == null) {
                throw new IllegalStateException("DayStatusService is not initialized!");
            }
            return new PayStrategyByDates(averageSalary, startDate, endDate, dayStatusService);
        } else if (vacationDays != null) {
            validateVacationDays(vacationDays);
            return new PayStrategyByDays(averageSalary, vacationDays);
        }
        throw new IllegalArgumentException("Не достаточно данных для расчета!");
    }

    private void validateAverageSalary(double averageSalary) {
        if (averageSalary <= 0) {
            throw new IllegalArgumentException("Средняя зарплата за 12 месяцев должна быть больше нуля!");
        }
    }

    private void validateVacationDays(int vacationDays) {
        if (vacationDays <= 0) {
            throw new IllegalArgumentException("Количество дней отпуска должно быть больше нуля!");
        }
    }

    private void validateVacationDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Конечная дата отпуска не может быть раньше даты старта!");
        }
    }

}
