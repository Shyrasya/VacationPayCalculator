package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.mock;

public class PayStrategyBuilderTest {

    private DayStatusService dayStatusService;

    @BeforeEach
    void setUp(){
        dayStatusService = mock(DayStatusService.class);
    }

    @Test
    void testBuild_WhenCorrectPayStrategyByDays(){
        double averageSalary = 60000.0;
        int vacationDays = 10;

        PayStrategy payStrategy = new PayStrategyBuilder(dayStatusService)
                .withAverageSalary(averageSalary)
                .withVacationDays(vacationDays)
                .build();

        assertTrue(payStrategy instanceof PayStrategyByDays);
    }

    @Test
    void testBuild_WhenCorrectPayStrategyByDates(){
        double averageSalary = 60000.0;
        LocalDate startDay = LocalDate.of(2025, 4, 14);
        LocalDate endDay = LocalDate.of(2025, 4, 20);

        PayStrategy payStrategy = new PayStrategyBuilder(dayStatusService)
                .withAverageSalary(averageSalary)
                .withStartEndDates(startDay, endDay)
                .build();

        assertTrue(payStrategy instanceof PayStrategyByDates);
    }

    @Test
    void testBuild_ZeroSalaryException(){
        double averageSalary = -50000.0;
        int vacationDays = 7;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new PayStrategyBuilder(dayStatusService)
                .withAverageSalary(averageSalary)
                .withVacationDays(vacationDays)
                .build());

        assertEquals("Средняя зарплата за 12 месяцев должна быть положительной!", exception.getMessage());
    }

    @Test
    void testBuild_StartDateAfterEndDateException(){
        double averageSalary = 80000.0;
        LocalDate startDate = LocalDate.of(2025, 4, 14);
        LocalDate endDate = LocalDate.of(2025, 4, 10);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
           new PayStrategyBuilder(dayStatusService)
                   .withAverageSalary(averageSalary)
                   .withStartEndDates(startDate, endDate)
                   .build();
        });

        assertEquals("Конечная дата отпуска не может быть раньше даты старта!", exception.getMessage());
    }


    @Test
    void testBuild_ZeroVacationDaysException(){
        double averageSalary = 80000.0;
        int vacationDays = -5;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new PayStrategyBuilder(dayStatusService)
                    .withAverageSalary(averageSalary)
                    .withVacationDays(vacationDays)
                    .build();
        });

        assertEquals("Количество дней отпуска должно быть положительным!", exception.getMessage());
    }

    @Test
    void testBuild_AverageSalaryIsMissingException(){
        int vacationDays = 9;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->{
            new PayStrategyBuilder(dayStatusService)
                    .withVacationDays(vacationDays)
                    .build();
        });

        assertEquals("Средняя зарплата не указана!", exception.getMessage());
    }

    @Test
    void testBuild_VacationDaysAreMissingException(){
        double averageSalary = 60000.0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
           new PayStrategyBuilder(dayStatusService)
                   .withAverageSalary(averageSalary)
                   .build();
        });

        assertEquals("Недостаточно данных для расчета!", exception.getMessage());
    }

    @Test
    void testBuild_StartAndDaysAreMissingException(){
        double averageSalary = 40000.0;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
           new PayStrategyBuilder(dayStatusService)
                   .withAverageSalary(averageSalary)
                   .build();
        });

        assertEquals("Недостаточно данных для расчета!", exception.getMessage());
    }


    @Test
    void testBuild_DayStatusServiceIsMissingException(){
        double averageSalary = 90000.0;
        LocalDate startDate = LocalDate.of(2025, 4, 14);
        LocalDate endDate = LocalDate.of(2025, 4, 19);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            new PayStrategyBuilder(null)
                    .withAverageSalary(averageSalary)
                    .withStartEndDates(startDate, endDate)
                    .build();
        });

        assertEquals("DayStatusService не проинициализирован!", exception.getMessage());
    }
}
