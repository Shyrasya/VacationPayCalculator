package neoflex.config;

import neoflex.domain.service.DayStatusService;
import neoflex.domain.strategy.PayStrategyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PayStrategyConfig {

    @Bean
    @Scope("prototype")
    public PayStrategyBuilder payStrategyBuilder(DayStatusService dayStatusService) {
        return new PayStrategyBuilder(dayStatusService);
    }
}
