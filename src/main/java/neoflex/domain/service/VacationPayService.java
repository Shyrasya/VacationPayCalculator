package neoflex.domain.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class VacationPayService {
    private static final double AVERAGE_MONTH_DAYS = 29.3;

    public DayStatusService dayStatusService;

    public VacationPayService(DayStatusService dayStatusService) {
        this.dayStatusService = dayStatusService;
    }

    public double calculatePayWithDays(double averageSalary, int vacationDays) {
        validateAverageSalary(averageSalary);
        validateVacationDays(vacationDays);
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        return roundToTwoDigits(oneDayPay * vacationDays);
    }

    public double calculatePayWithDates(double averageSalary, LocalDate startDate, LocalDate endDate) {
        validateAverageSalary(averageSalary);
        validateVacationDates(startDate, endDate);
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        int vacationDays = countWorkingDays(startDate, endDate);
        return roundToTwoDigits(oneDayPay * vacationDays);
    }

    private int countWorkingDays(LocalDate startDate, LocalDate endDate) {
        int workingDays = 0;
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            if (dayStatusService.isWorkDay(currentDate)) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return workingDays;
    }

    private double roundToTwoDigits(double pay) {
        return Math.round(pay * 100.0) / 100.0;
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
