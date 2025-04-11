package neoflex.web.controller;

import neoflex.domain.strategy.PayStrategy;
import neoflex.domain.strategy.PayStrategyBuilder;
import neoflex.web.controller.VacationPayController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.AutoCloseable;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VacationPayControllerTest {

    @Mock
    private ObjectFactory<PayStrategyBuilder> builderFactory;

    @Mock
    private PayStrategyBuilder payStrategyBuilder;

    @Mock
    private PayStrategy payStrategy;

    @InjectMocks
    private VacationPayController vacationPayController;

    private MockMvc mockMvc;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp(){
        closeable = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vacationPayController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCalculateVacationPay_WhenCorrectVacationDays() throws Exception {
        double averageSalary = 60000.0;
        int vacationDays = 10;
        when(builderFactory.getObject()).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withAverageSalary(averageSalary)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withVacationDays(vacationDays)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withStartEndDates(null, null)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.build()).thenReturn(payStrategy);
        when(payStrategy.calculate()).thenReturn(20477.82);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                .param("averageSalary", String.valueOf(averageSalary))
                .param("vacationDays", String.valueOf(vacationDays))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("20477.82"));

        verify(payStrategyBuilder).withAverageSalary(averageSalary);
        verify(payStrategyBuilder).withVacationDays(vacationDays);
        verify(payStrategy).calculate();
    }

    @Test
    void testCalculateVacationPay_WhenCorrectStartEndDate() throws Exception {
        double averageSalary = 100000.0;
        LocalDate startDate = LocalDate.of(2025,4,14);
        LocalDate endDate = LocalDate.of(2025, 4, 20);
        when(builderFactory.getObject()).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withAverageSalary(averageSalary)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withVacationDays(null)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withStartEndDates(startDate, endDate)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.build()).thenReturn(payStrategy);
        when(payStrategy.calculate()).thenReturn(17064.85);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", String.valueOf(averageSalary))
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("17064.85"));

        verify(payStrategyBuilder).withAverageSalary(averageSalary);
        verify(payStrategyBuilder).withStartEndDates(startDate, endDate);
        verify(payStrategy).calculate();
    }

    @Test
    void testCalculateVacationPay_WhenVacationDaysAndStartEndDate() throws Exception {
        double averageSalary = 100000.0;
        int vacationDays = 10;
        LocalDate startDate = LocalDate.of(2025,4,14);
        LocalDate endDate = LocalDate.of(2025, 4, 20);
        when(builderFactory.getObject()).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withAverageSalary(averageSalary)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withVacationDays(vacationDays)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.withStartEndDates(startDate, endDate)).thenReturn(payStrategyBuilder);
        when(payStrategyBuilder.build()).thenReturn(payStrategy);
        when(payStrategy.calculate()).thenReturn(17064.85);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", String.valueOf(averageSalary))
                        .param("vacationDays", String.valueOf(vacationDays))
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("17064.85"));

        verify(payStrategyBuilder).withAverageSalary(averageSalary);
        verify(payStrategyBuilder).withVacationDays(vacationDays);
        verify(payStrategyBuilder).withStartEndDates(startDate, endDate);
        verify(payStrategy).calculate();
    }

    @Test
    void testCalculateVacationPay_WhenNoParams() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/calculate"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCalculateVacationPay_WhenIncorrectDate() throws Exception {
        double averageSalary = 50000.0;
        String startDate = "2025-go-11";
        LocalDate endDate = LocalDate.of(2025, 4, 20);

        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", String.valueOf(averageSalary))
                        .param("startDate", startDate)
                        .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }
}
