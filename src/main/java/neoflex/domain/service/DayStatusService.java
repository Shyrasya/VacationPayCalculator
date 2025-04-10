package neoflex.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для определения, является ли день рабочим.
 * Использует внешний API для получения календаря выходных и рабочих дней по году.
 */
@Service
public class DayStatusService {

    /**
     * WebClient для выполнения HTTP-запросов к внешнему API,
     * возвращающему календарь рабочих и выходных дней
     */
    private final WebClient webClient;

    /**
     * Конструктор DayStatusService, инициализирующий WebClient для работы с внешним API
     *
     * @param webClient настроенный WebClient, используемый для запросов
     */
    public DayStatusService(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Шаблон URL запроса к API с указанием года и параметра предварительной обработки.
     * Параметр {@code %d} будет заменён на интересующий год
     */
    private static final String API_URL_TEMPLATE = "api/getdata?year=%d&pre=1";

    /**
     * Кешированный календарь рабочих/нерабочих дней по годам
     */
    private final Map<Integer, Map<LocalDate, Boolean>> yearsCache = new HashMap<>();

    /**
     * Проверяет, является ли указанный день рабочим
     *
     * @param date дата для проверки
     * @return true — если день рабочий, false — если выходной
     */
    public boolean isWorkDay(LocalDate date) {
        int year = date.getYear();
        yearsCache.computeIfAbsent(year, this::getDaysFromWebClient);
        return yearsCache.get(year).get(date);
    }

    /**
     * Получает данные о рабочих/выходных днях с внешнего API и преобразует их в карту
     *
     * @param year год, для которого требуется получить данные
     * @return карта, где ключ — дата, значение — true если рабочий день, false — если выходной
     * @throws IllegalStateException если ответ от API пустой или некорректный
     */
    private Map<LocalDate, Boolean> getDaysFromWebClient(int year) {
        String response = webClient.get()
                .uri(String.format(API_URL_TEMPLATE, year))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response == null || response.isEmpty()) {
            throw new IllegalStateException("Пустой или некорректный ответ от сервиса!");
        }

        Map<LocalDate, Boolean> daysMap = new HashMap<>();
        LocalDate current = LocalDate.of(year, 1, 1);
        for (char c : response.toCharArray()) {
            boolean isWorkDay = c == '0';
            daysMap.put(current, isWorkDay);
            current = current.plusDays(1);
        }
        return daysMap;
    }
}
