package neoflex.web.controller;

import neoflex.domain.service.VacationPayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("")
public class VacationPayController {

    private final VacationPayService vacationPayService;

    public VacationPayController(VacationPayService vacationPayService) {
        this.vacationPayService = vacationPayService;
    }

    @GetMapping("/calculate")
    public double calculateVacationPay(
            @RequestParam double averageSalary,
            @RequestParam(required = false) Integer vacationDays,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return vacationPayService.calculatePayWithDates(averageSalary, startDate, endDate);
        } else if (vacationDays != null) {
            return vacationPayService.calculatePayWithDays(averageSalary, vacationDays);
        } else {
            throw new IllegalArgumentException("Не указаны параметры для расчета!");
        }
    }
}
