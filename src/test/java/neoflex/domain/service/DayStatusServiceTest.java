package neoflex.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DayStatusServiceTest {

    @Mock
    private WebClient mockWebClient;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private DayStatusService dayStatusService;

    private static final String MOCKED_YEAR_RESPONSE_2025 =
            "1111111100110000011000001100000110000011000001100000" +
                    "11000001100000110000011000001100000110000011" +
                    "00000110000011000001100011110001111000001100" +
                    "00011000001100000110001111000001100000110000" +
                    "01100000110000011000001100000110000011000001" +
                    "10000011000001100000110000011000001100000110" +
                    "00001100000110000011000001100000011100011000" +
                    "00110000011000001100000110000011000001100000" +
                    "11001";

    @BeforeEach
    void setUp() {
        dayStatusService = new DayStatusService(mockWebClient);
    }

    @Test
    void isWorkDay_WhenWorkDay() {
        when(mockWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(MOCKED_YEAR_RESPONSE_2025));

        boolean result = dayStatusService.isWorkDay(LocalDate.of(2025, 1, 13));

        assertTrue(result);
    }

    @Test
    void isWorkDay_WhenHoliday() {
        when(mockWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(MOCKED_YEAR_RESPONSE_2025));

        boolean result = dayStatusService.isWorkDay(LocalDate.of(2025, 1, 1));

        assertFalse(result);
    }

    @Test
    void isWorkDay_WhenNullExternalResponse() {
        when(mockWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.justOrEmpty(null));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            dayStatusService.isWorkDay(LocalDate.of(2025, 4, 10));
        });
        assertEquals("Пустой или некорректный ответ от сервиса!", exception.getMessage());
    }
}
