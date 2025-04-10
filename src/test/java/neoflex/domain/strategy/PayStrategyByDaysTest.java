package neoflex.domain.strategy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PayStrategyByDaysTest {

    @Test
    void testCalculate_WhenThreeWorkDays(){
        double averageSalary = 29300.0;
        int vacationDays = 3;

        PayStrategy payStrategy = new PayStrategyByDays(averageSalary, vacationDays);
        double result = payStrategy.calculate();
        assertEquals(3000.0, result);
    }

    @Test
    void testCalculate_WhenZeroVacationDays(){
        double averageSalary = 29300.0;
        int vacationDays = 0;

        PayStrategy payStrategy = new PayStrategyByDays(averageSalary, vacationDays);
        double result = payStrategy.calculate();
        assertEquals(0.0, result);
    }

    @Test
    void testCalculate_WhenOneVacationDay(){
        double averageSalary = 29300.0;
        int vacationDays = 1;

        PayStrategy payStrategy = new PayStrategyByDays(averageSalary, vacationDays);
        double result = payStrategy.calculate();
        assertEquals(1000.0, result);
    }

    @Test
    void testCalculate_WhenVacationDaysMoreMonth(){
        double averageSalary = 29300.0;
        int vacationDays = 40;

        PayStrategy payStrategy = new PayStrategyByDays(averageSalary, vacationDays);
        double result = payStrategy.calculate();
        assertEquals(40000.0, result);
    }

    @Test
    void testCalculate_WhenSalaryHasFloatingPoint(){
        double averageSalary = 30000.0;
        int vacationDays = 5;

        PayStrategy payStrategy = new PayStrategyByDays(averageSalary, vacationDays);
        double result = payStrategy.calculate();
        assertEquals(5119.45, result);
    }
}
