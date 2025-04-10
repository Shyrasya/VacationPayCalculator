package neoflex.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

/**
 * Глобальный обработчик исключений для REST-контроллеров.
 * Возвращает читабельные сообщения об ошибках с соответствующим HTTP-статусом.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Обработка {@link IllegalArgumentException}
     *
     * @param e исключение
     * @return сообщение об ошибке
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException e) {
        return Map.of("error", e.getMessage());
    }

    /**
     * Обработка {@link IllegalStateException}
     *
     * @param e исключение
     * @return сообщение об ошибке
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleIllegalStateException(IllegalStateException e) {
        return Map.of("error", e.getMessage());
    }

    /**
     * Обработка ошибок преобразования параметров запроса
     *
     * @param e исключение
     * @return сообщение об ошибке с указанием неверного параметра
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return Map.of("error", "Неверный формат параметра: " + e.getName());
    }

    /**
     * Обработка ошибок при запросах через WebClient
     *
     * @param e исключение
     * @return сообщение об ошибке, полученной от внешнего API
     */
    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Map<String, String> handleWebClientException(WebClientResponseException e) {
        return Map.of("error", "Ошибка при запросе к внешнему API: " + e.getMessage());
    }

    /**
     * Обработка непредвиденных {@link RuntimeException}
     *
     * @param e исключение
     * @return сообщение об ошибке сервера
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntimeException(RuntimeException e) {
        return Map.of("error", "Ошибка сервера: " + e.getMessage());
    }

    /**
     * Общий обработчик для всех остальных исключений
     *
     * @param e исключение
     * @return сообщение об ошибке сервера
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception e) {
        return Map.of("error", "Ошибка сервера: " + e.getMessage());
    }
}
