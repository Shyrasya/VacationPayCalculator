package neoflex.domain.strategy;

import neoflex.domain.service.DayStatusService;

import java.time.LocalDate;

/**
 * Строитель для создания подходящей стратегии расчёта отпускных
 * на основе введённых данных: средней зарплаты, количества дней или дат отпуска.
 */
public class PayStrategyBuilder {

    /**
     * Средняя зарплата за 12 месяцев
     */
    private Double averageSalary;

    /**
     * Количество дней отпуска
     */
    private Integer vacationDays;

    /**
     * Дата начала отпуска
     */
    private LocalDate startDate;

    /**
     * Дата окончания отпуска
     */
    private LocalDate endDate;

    /**
     * Сервис для определения рабочих и нерабочих дней
     */
    private final DayStatusService dayStatusService;

    /**
     * Конструктор, принимающий {@link DayStatusService}
     *
     * @param dayStatusService сервис определения рабочих дней
     */
    public PayStrategyBuilder(DayStatusService dayStatusService) {
        this.dayStatusService = dayStatusService;
    }

    /**
     * Устанавливает среднюю зарплату
     *
     * @param averageSalary средняя зарплата за 12 месяцев
     * @return текущий билдер
     */
    public PayStrategyBuilder withAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
        return this;
    }

    /**
     * Устанавливает количество дней отпуска
     *
     * @param vacationDays количество дней отпуска
     * @return текущий билдер
     */
    public PayStrategyBuilder withVacationDays(Integer vacationDays) {
        this.vacationDays = vacationDays;
        return this;
    }

    /**
     * Устанавливает даты начала и окончания отпуска
     *
     * @param startDate дата начала отпуска
     * @param endDate   дата окончания отпуска
     * @return текущий билдер
     */
    public PayStrategyBuilder withStartEndDates(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    /**
     * Создаёт соответствующую стратегию расчёта отпускных
     * на основе предоставленных данных
     *
     * @return реализация {@link PayStrategy}
     * @throws IllegalArgumentException если не хватает данных или данные некорректны
     * @throws IllegalStateException    если отсутствует {@link DayStatusService} при расчёте по датам
     */
    public PayStrategy build() {
        if (averageSalary == null) {
            throw new IllegalArgumentException("Средняя зарплата не указана!");
        } else {
            validateAverageSalary(averageSalary);
        }

        if (startDate != null && endDate != null) {
            validateVacationDates(startDate, endDate);
            if (dayStatusService == null) {
                throw new IllegalStateException("DayStatusService не проинициализирован!");
            }
            return new PayStrategyByDates(averageSalary, startDate, endDate, dayStatusService);
        } else if (vacationDays != null) {
            validateVacationDays(vacationDays);
            return new PayStrategyByDays(averageSalary, vacationDays);
        }
        throw new IllegalArgumentException("Недостаточно данных для расчета!");
    }

    /**
     * Проверяет корректность средней зарплаты
     *
     * @param averageSalary средняя зарплата
     * @throws IllegalArgumentException если зарплата отрицательная
     */
    private void validateAverageSalary(double averageSalary) {
        if (averageSalary < 0) {
            throw new IllegalArgumentException("Средняя зарплата за 12 месяцев должна быть положительной!");
        }
    }

    /**
     * Проверяет корректность количества дней отпуска
     *
     * @param vacationDays количество дней отпуска
     * @throws IllegalArgumentException если значение отрицательное
     */
    private void validateVacationDays(int vacationDays) {
        if (vacationDays < 0) {
            throw new IllegalArgumentException("Количество дней отпуска должно быть положительным!");
        }
    }

    /**
     * Проверяет, что конечная дата отпуска не раньше начальной
     *
     * @param startDate дата начала
     * @param endDate   дата окончания
     * @throws IllegalArgumentException если даты некорректны
     */
    private void validateVacationDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Конечная дата отпуска не может быть раньше даты старта!");
        }
    }
}