package neoflex.domain.strategy;

public class PayStrategyByDays implements PayStrategy {
    private final double averageSalary;
    private final int vacationDays;


    public PayStrategyByDays(double averageSalary, int vacationDays) {
        this.averageSalary = averageSalary;
        this.vacationDays = vacationDays;
    }

    @Override
    public double calculate() {
        double oneDayPay = averageSalary / AVERAGE_MONTH_DAYS;
        return roundToTwoDigits(oneDayPay * vacationDays);
    }
}
