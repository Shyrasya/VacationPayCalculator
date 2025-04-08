package neoflex.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DayStatusService {
    private final WebClient webClient;

    public DayStatusService(WebClient webClient) {
        this.webClient = webClient;
    }

    private static final String API_URL_TEMPLATE = "api/getdata?year=%d&pre=1";

    private final Map<Integer, Map<LocalDate, Boolean>> yearsCache = new HashMap<>();

    public boolean isWorkDay(LocalDate date) {
        int year = date.getYear();
        yearsCache.computeIfAbsent(year, this::getDaysFromWebClient);
        return yearsCache.get(year).get(date);
    }

    private Map<LocalDate, Boolean> getDaysFromWebClient(int year) {
        String response = webClient.get()
                .uri(String.format(API_URL_TEMPLATE, year))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Map<LocalDate, Boolean> daysMap = new HashMap<>();

        if (response != null && !response.isEmpty()) {
            LocalDate current = LocalDate.of(year, 1, 1);
            for (char c : response.toCharArray()) {
                boolean isWorkDay = c == '0';
                daysMap.put(current, isWorkDay);
                current = current.plusDays(1);
            }
        }
        return daysMap;
    }
}
