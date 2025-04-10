package neoflex.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурационный класс Spring, создающий бин {@link WebClient}
 * с базовым URL для обращения к сервису определения рабочих и выходных дней
 */
@Configuration
public class WebClientConfig {

    /**
     * Создаёт и настраивает {@link WebClient} с базовым URL "https://isdayoff.ru".
     * Этот клиент используется для получения информации о статусе дней (рабочий/выходной)
     *
     * @return настроенный экземпляр {@link WebClient}
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://isdayoff.ru")
                .build();
    }
}
