package neoflex.domain.strategy;

/**
 * Стратегия расчёта отпускных по количеству отпускных дней
 */
public class PayStrategyByDays implements PayStrategy {

    /**
     * Средняя зарплата за 12 месяцев
     */
    private final double averageSalary;

    /**
     * Количество отпускных дней
     */
    private final int vacationDays;

    /**
     * Конструктор стратегии расчёта по дням
     *
     * @param averageSalary средняя зарплата за 12 месяцев
     * @param vacationDays  количество дней отпуска
     */
    public PayStrategyByDays(double averageSalary, int vacationDays) {
        this.averageSalary = averageSalary;
        this.vacationDays = vacationDays;
    }

    /**
     * Рассчитывает сумму отпускных на основе количества отпускных дней
     *
     * @return рассчитанная сумма отпускных, округлённая до двух знаков после запятой
     */
    @Override
    public double calculate() {
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        return roundToTwoDigits(oneDayPay * vacationDays);
    }
}
