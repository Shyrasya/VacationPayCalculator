package neoflex.web.controller;

import neoflex.domain.strategy.PayStrategyBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST-контроллер для расчёта отпускных
 * Предоставляет endpoint для получения суммы отпускных по зарплате и количеству дней или датам отпуска
 */
@RestController
@RequestMapping("")
public class VacationPayController {

    /**
     * Фабрика для создания новых экземпляров {@link PayStrategyBuilder}
     */
    private final ObjectFactory<PayStrategyBuilder> builderFactory;

    /**
     * Конструктор контроллера с внедрением зависимости {@link PayStrategyBuilder} через ObjectFactory
     *
     * @param builderFactory фабрика билдера стратегии расчёта
     */
    public VacationPayController(ObjectFactory<PayStrategyBuilder> builderFactory) {
        this.builderFactory = builderFactory;
    }

    /**
     * HTTP GET endpoint для расчёта отпускных
     * Можно указать либо количество дней отпуска, либо даты начала и конца отпуска
     *
     * @param averageSalary средняя зарплата за 12 месяцев (обязательный параметр)
     * @param vacationDays  количество дней отпуска (опционально)
     * @param startDate     дата начала отпуска (опционально, в формате ISO, например, 2024-01-01)
     * @param endDate       дата окончания отпуска (опционально, в формате ISO)
     * @return рассчитанная сумма отпускных
     * @throws IllegalArgumentException если недостаточно данных или данные некорректны
     */
    @GetMapping("/calculate")
    public double calculateVacationPay(
            @RequestParam Double averageSalary,
            @RequestParam(required = false) Integer vacationDays,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return builderFactory.getObject()
                .withAverageSalary(averageSalary)
                .withVacationDays(vacationDays)
                .withStartEndDates(startDate, endDate)
                .build()
                .calculate();
    }
}
