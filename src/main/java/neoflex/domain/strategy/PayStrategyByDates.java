package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;

import java.time.LocalDate;

/**
 * Стратегия расчёта отпускных по датам начала и окончания отпуска
 */
public class PayStrategyByDates implements PayStrategy {

    /**
     * Средняя зарплата за 12 месяцев
     */
    private final Double averageSalary;

    /**
     * Дата начала отпуска
     */
    private final LocalDate startDate;

    /**
     * Дата окончания отпуска
     */
    private final LocalDate endDate;

    /**
     * Сервис, определяющий, является ли день рабочим
     */
    private final DayStatusService dayStatusService;

    /**
     * Конструктор стратегии расчёта отпускных по датам
     *
     * @param averageSalary    средняя зарплата за 12 месяцев
     * @param startDate        дата начала отпуска
     * @param endDate          дата окончания отпуска
     * @param dayStatusService сервис, определяющий рабочие дни
     */
    public PayStrategyByDates(Double averageSalary, LocalDate startDate, LocalDate endDate, DayStatusService dayStatusService) {
        this.averageSalary = averageSalary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dayStatusService = dayStatusService;
    }

    /**
     * Рассчитывает сумму отпускных на основе количества рабочих дней
     * между {@code startDate} и {@code endDate} (включительно)
     *
     * @return рассчитанная сумма отпускных, округлённая до двух знаков после запятой
     */
    @Override
    public double calculate() {
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        int workDays = countWorkDays(startDate, endDate);
        return roundToTwoDigits(oneDayPay * workDays);
    }

    /**
     * Подсчитывает количество рабочих дней между двумя датами включительно,
     * используя {@link DayStatusService}
     *
     * @param startDate дата начала
     * @param endDate   дата окончания
     * @return количество рабочих дней
     */
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
