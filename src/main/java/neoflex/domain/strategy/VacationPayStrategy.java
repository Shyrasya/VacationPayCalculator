package neoflex.domain.strategy;

public interface VacationPayStrategy {

    double AVERAGE_MONTH_DAYS = 29.3;
    double calculate();

    default double roundToTwoDigits(double pay) {
        return Math.round(pay * 100.0) / 100.0;
    }
}
