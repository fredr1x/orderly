package pp.geoservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import pp.geoservice.dto.YandexGeoResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class YandexGeoService {

    private final WebClient webClient;

    @Value("${yandex.http-geocoder.api-key}")
    private String apiKey;

    public Mono<YandexGeoResponse> reverse(double longitude, double latitude) {

        URI uri = UriComponentsBuilder
                .fromUriString("https://geocode-maps.yandex.ru/v1/")
                .queryParam("apikey", apiKey)
                .queryParam("geocode", longitude + "," + latitude)
                .queryParam("format", "json")
                .build()
                .toUri();

        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    log.error("Yandex API error, status: {}, body: {}", response.statusCode(), body);
                                    return Mono.error(new RuntimeException("Yandex error: " + body));
                                })
                )
                .bodyToMono(YandexGeoResponse.class)
                .doOnError(e -> log.error("Error calling Yandex: {}", e.getMessage(), e))
                .doOnNext(r -> log.info("Yandex response: {}", r));
    }
}
