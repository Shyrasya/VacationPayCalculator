package neoflex.config;

import neoflex.domain.service.DayStatusService;
import neoflex.domain.strategy.PayStrategyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Конфигурационный класс Spring, отвечающий за создание бинов для стратегии расчета отпускных.
 * Включает создание бина {@link PayStrategyBuilder} с областью прототипа для каждого запроса.
 */
@Configuration
public class PayStrategyConfig {

    /**
     * Создаёт и настраивает бин {@link PayStrategyBuilder} с областью прототипа,
     * что позволяет создавать новый экземпляр {@link PayStrategyBuilder} при каждом запросе.
     * Бин зависит от {@link DayStatusService}, который используется для получения информации о рабочих днях.
     *
     * @param dayStatusService Сервис для получения статуса дня (рабочий или выходной).
     * @return настроенный экземпляр {@link PayStrategyBuilder}
     */
    @Bean
    @Scope("prototype")
    public PayStrategyBuilder payStrategyBuilder(DayStatusService dayStatusService) {
        return new PayStrategyBuilder(dayStatusService);
    }
}
