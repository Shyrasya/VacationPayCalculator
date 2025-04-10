package neoflex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения "Калькулятор отпускных".
 * Запускает Spring Boot-приложение
 */
@SpringBootApplication
public class VacationPayCalculatorApplication {

    /**
     * Точка входа в приложение
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(VacationPayCalculatorApplication.class, args);
    }
}
