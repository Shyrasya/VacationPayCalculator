package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VacationPayStrategyBuilder {
    private Double averageSalary;
    private Integer vacationDays;
    private LocalDate startDate;
    private LocalDate endDate;

    @Autowired
    private DayStatusService dayStatusService;

    public VacationPayStrategyBuilder withAverageSalary(Double averageSalary){
        this.averageSalary = averageSalary;
        return this;
    }

    public VacationPayStrategyBuilder withVacationDays(Integer vacationDays){
        this.vacationDays = vacationDays;
        return this;
    }

    public VacationPayStrategyBuilder withStartEndDates(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    public VacationPayStrategy build(){
        validateAverageSalary(averageSalary);
        if (startDate != null && endDate != null){
            validateVacationDates(startDate, endDate);
            if (dayStatusService == null) {
                throw new IllegalStateException("DayStatusService is not initialized!");
            }
            return new VacationPayStrategyByDates(averageSalary, startDate, endDate, dayStatusService);
        } else if (vacationDays != null){
            validateVacationDays(vacationDays);
            return new VacationPayStrategyByDays(averageSalary, vacationDays);
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
