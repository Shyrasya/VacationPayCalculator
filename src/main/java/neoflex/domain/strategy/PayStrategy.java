package neoflex.domain.strategy;

/**
 * Стратегия расчёта отпускных выплат
 */
public interface PayStrategy {

    /**
     * Среднее количество дней в месяце, используемое в расчетах
     */
    double AVERAGE_MONTH_DAYS = 29.3;

    /**
     * Выполняет расчёт отпускных на основе реализованной стратегии
     *
     * @return рассчитанная сумма отпускных, округлённая до двух знаков после запятой
     */
    double calculate();

    /**
     * Округляет переданную сумму до двух знаков после запятой.
     *
     * @param pay сумма, которую нужно округлить.
     * @return округлённая сумма.
     */
    default double roundToTwoDigits(double pay) {
        return Math.round(pay * 100.0) / 100.0;
    }
}
