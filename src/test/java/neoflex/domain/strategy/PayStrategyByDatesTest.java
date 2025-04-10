package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PayStrategyByDatesTest {

    private DayStatusService dayStatusService;

    @BeforeEach
    void setUp(){
        dayStatusService = mock(DayStatusService.class);
    }

    @Test
    void testCalculate_WhenWorkDaysWithHolidays(){
        double averageSalary = 29300.0;
        LocalDate startDate = LocalDate.of(2025, 4, 10);
        LocalDate endDate = LocalDate.of(2025, 4, 14);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 10))).thenReturn(true);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 11))).thenReturn(true);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 12))).thenReturn(false);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 13))).thenReturn(false);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 14))).thenReturn(true);

        PayStrategy payStrategy = new PayStrategyByDates(averageSalary, startDate, endDate, dayStatusService);
        double result = payStrategy.calculate();

        assertEquals(3000.0, result);
    }

    @Test
    void testCalculate_WhenHolidays(){
        double averageSalary = 29300.0;
        LocalDate startDate = LocalDate.of(2025, 4, 12);
        LocalDate endDate = LocalDate.of(2025, 4, 13);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 12))).thenReturn(false);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 13))).thenReturn(false);

        PayStrategy payStrategy = new PayStrategyByDates(averageSalary, startDate, endDate, dayStatusService);
        double result = payStrategy.calculate();

        assertEquals(0.0, result);
    }

    @Test
    void testCalculate_WhenOneWorkDay(){
        double averageSalary = 29300.0;
        LocalDate startDate = LocalDate.of(2025, 4, 10);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 10))).thenReturn(true);

        PayStrategy payStrategy = new PayStrategyByDates(averageSalary, startDate, startDate, dayStatusService);
        double result = payStrategy.calculate();

        assertEquals(1000.0, result);
    }

    @Test
    void testCalculate_WhenOneHoliday(){
        double averageSalary = 29300.0;
        LocalDate startDay = LocalDate.of(2025, 4, 12);
        when(dayStatusService.isWorkDay(LocalDate.of(2025, 4, 12))).thenReturn(false);

        PayStrategy payStrategy = new PayStrategyByDates(averageSalary, startDay, startDay, dayStatusService);
        double result = payStrategy.calculate();

        assertEquals(0.0, result);
    }
}
