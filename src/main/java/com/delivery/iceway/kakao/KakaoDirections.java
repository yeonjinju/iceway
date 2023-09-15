package com.delivery.iceway.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.delivery.iceway.domain.Customers;
import com.delivery.iceway.domain.Info;
import com.delivery.iceway.sensor.SensorMapper;

import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoDirections {

    // Kakao API 키를 불러옵니다.
    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    // sensorMapper 객체를 주입받습니다.
    private final SensorMapper sensorMapper;

    // WebClient 인스턴스를 생성합니다.
    private final WebClient webClient = WebClient.builder().build();

    // 기본 출발지의 위도와 경도를 설정합니다.
    private final double defaultStartLatitude = 37.507194;
    private final double defaultStartLongitude = 127.022783;

    /**
     * Kakao API에서 경로 정보를 가져와 Info 객체로 변환하여 반환합니다.
     * 
     * @param id 배달 ID
     * @return Info 경로 정보 객체
     * @throws RuntimeException API 요청 또는 응답 처리 중 발생한 예외
     */
    public Info fetchDeliveryInfoFromKakao(int id) {
        try {
            // 배달 ID를 사용하여 배달 좌표 정보를 가져옵니다.
            Customers customers = sensorMapper.getDeliveryCoordinates(id);

            // WebClient를 사용하여 Kakao API에 경로 정보 요청을 보냅니다.
            Mono<Info> infoMono = webClient.get()
                    .uri(buildUrl(customers.getLatitude(), customers.getLongitude()))
                    .header("Authorization", "KakaoAK " + kakaoApiKey)
                    .retrieve()
                    .onStatus(statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                            response -> {
                                log.error("API request failed: {}", response.statusCode());
                                return Mono.error(new RuntimeException("API request failed: " + response.statusCode()));
                            })
                    .bodyToMono(Info.class);

            // 응답 데이터를 블록킹 방식으로 받습니다.
            Info info = infoMono.block();
            if (info == null) {
                log.error("No response data");
                throw new RuntimeException("No response data");
            }

            return info;
        } catch (Exception e) {
            log.error("Error occurred while retrieving shipping information from Kakao API", e);
            throw new RuntimeException("Error occurred while retrieving shipping information from Kakao API", e);
        }
    }

    /**
     * Kakao API 요청을 위한 URL을 생성합니다.
     *
     * @param arrivalLatitude  도착지 위도
     * @param arrivalLongitude 도착지 경도
     * @return 완성된 URL 문자열
     */
    private String buildUrl(double arrivalLatitude, double arrivalLongitude) {
        return "https://apis-navi.kakaomobility.com/v1/directions" +
                "?origin=" + defaultStartLongitude +
                "," + defaultStartLatitude +
                "&destination=" + arrivalLongitude +
                "," + arrivalLatitude +
                "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false&alternatives=false&road_details=false";
    }
}
