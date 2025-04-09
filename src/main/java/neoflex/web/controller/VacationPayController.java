package neoflex.web.controller;

import neoflex.domain.strategy.VacationPayStrategy;
import neoflex.domain.strategy.VacationPayStrategyBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("")
public class VacationPayController {

    @Autowired
    private ObjectFactory<VacationPayStrategyBuilder> builderFactory;

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
