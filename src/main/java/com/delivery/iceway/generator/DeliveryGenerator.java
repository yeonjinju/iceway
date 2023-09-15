package com.delivery.iceway.generator;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.delivery.iceway.amqp.MessageService;
import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Info;
import com.delivery.iceway.domain.Info.Route;
import com.delivery.iceway.domain.Info.Section;
import com.delivery.iceway.kakao.KakaoDirections;
import com.delivery.iceway.sensor.SensorMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryGenerator {

    private static ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    private final MessageService messageService;
    private final ScheduledExecutorService executor;
    private final KakaoDirections kakaoDirections;
    private final SensorMapper sensorMapper;
    private final ScheduledService schedul;

    /**
     * 주어진 배송 ID에 대한 좌표를 주기적으로 전송하는 작업을 스케줄링합니다.
     * 해당 배송 ID의 상품 온도와 Kakao Directions API로 가져온 배송 정보를 이용합니다.
     *
     * @param id 배송 ID
     */
    public void sendCoordinates(int id) {
        try {
            int temperature = sensorMapper.getProduct(id).getTemperature();
            Route route = validateInfoAndGetRoute(kakaoDirections.fetchDeliveryInfoFromKakao(id));
            List<Info.Coordinate> coordinates = extractCoordinates(route);
            scheduleCoordinateSending(coordinates, route.getSummary().getDuration(), id, temperature);
        } catch (Exception e) {
            log.error("An error occurred", e);
        }
    }

    /**
     * Kakao Directions API로부터 받은 Info 객체를 검증하고, 첫 번째 Route 객체를 반환합니다.
     *
     * @param info 검증할 Info 객체
     * @return 검증된 첫 번째 Route 객체
     * @throws IllegalArgumentException Info 객체가 null이거나 빈 Routes를 가질 경우
     */
    private Route validateInfoAndGetRoute(Info info) {
        if (info == null || info.getRoutes() == null || info.getRoutes().isEmpty()) {
            throw new IllegalArgumentException("Invalid Info object");
        }
        return info.getRoutes().get(0);
    }

    /**
     * 주어진 배송 좌표와 시간을 이용하여, 좌표 전송 작업을 스케줄링합니다.
     *
     * @param coordinates       배송 경로의 좌표 목록
     * @param durationInSeconds 예상 배송 시간 (초)
     * @param id                배송 ID
     * @param temperature       상품의 초기 온도
     */
    private void scheduleCoordinateSending(List<Info.Coordinate> coordinates, double durationInSeconds, int id,
            int temperature) {
        // 실제 배송 시간으로 할 시
        // long totalDeliveryTimeInMillis = (long) (durationInSeconds * 1000);
        // long delayPerCoordinate = totalDeliveryTimeInMillis / coordinates.size();
        AtomicReference<ScheduledFuture<?>> scheduledFutureRef = new AtomicReference<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime estimatedArrival = now.plusSeconds((long) durationInSeconds);
        Timestamp estimatedArrivalTimestamp = Timestamp.valueOf(estimatedArrival);

        Runnable task = createTask(coordinates, scheduledFutureRef, id, temperature, estimatedArrivalTimestamp);
        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(task, 0, 2000,
                TimeUnit.MILLISECONDS);
        schedul.addSF(scheduledFuture);
        scheduledFutureRef.set(scheduledFuture);
    }

    /**
     * 주어진 Route 객체에서 좌표 목록을 추출합니다.
     *
     * @param route 좌표 목록을 추출할 Route 객체
     * @return 좌표 목록
     */
    private List<Info.Coordinate> extractCoordinates(Route route) {
        List<Info.Coordinate> coordinates = new ArrayList<>();
        coordinates.add(route.getSummary().getOrigin());
        for (Section section : route.getSections()) {
            for (Info.Road road : section.getRoads()) {
                for (int i = 0; i < road.getVertexes().size(); i += 2) {
                    double latitude = road.getVertexes().get(i);
                    double longitude = road.getVertexes().get(i + 1);
                    coordinates.add(new Info.Coordinate(latitude, longitude));
                }
            }
        }
        coordinates.add(route.getSummary().getDestination());
        return coordinates;
    }

    /**
     * 상품의 온도 변화 로직을 구현합니다.
     * 70% 확률로 온도가 유지되고, 30% 확률로 온도가 1도 상승합니다.
     *
     * @param temperature 현재 온도
     * @return 변화 후 온도
     */
    private int converter(int temperature) {
        return threadLocalRandom.nextInt(10) < 8 ? temperature : temperature + 1;
    }

    /**
     * 배달 작업을 위한 Runnable 객체를 생성합니다.
     *
     * @param coordinates          좌표 목록
     * @param scheduledFutureRef   스케쥴링된 작업의 참조
     * @param id                   배달 ID
     * @param initialTemperature   초기 온도
     * @param estimatedArrivalTime 예상 도착 시간
     * @return 배달 작업을 위한 Runnable 객체
     */

    private Runnable createTask(List<Info.Coordinate> coordinates,
            AtomicReference<ScheduledFuture<?>> scheduledFutureRef, int id, int initialTemperature,
            Timestamp estimatedArrivalTime) {

        return new Runnable() {
            private int index = 0;
            private boolean isProcessed = false;
            private boolean resetFlag = false;
            private int currentTemperature = initialTemperature;
            private Timestamp arrivalTime = estimatedArrivalTime;
            private Timestamp deliveryTime = new Timestamp(System.currentTimeMillis());

            @Override
            public void run() {
                log.info("index: " + index);
                try {
                    Optional<Boolean> optionalStatus = sensorMapper.getRecallStatus(id);
                    boolean recallStatus = optionalStatus.orElse(Boolean.FALSE);
                    if (recallStatus == true && isProcessed == true && resetFlag == false) {
                        index = 0;
                        currentTemperature = initialTemperature;
                        deliveryTime = new Timestamp(System.currentTimeMillis());
                        resetFlag = true;
                    }
                    if (index < coordinates.size()) {
                        deliveryTime.setTime(System.currentTimeMillis());
                        if (isProcessed == false) {
                            Delivery delivery = Delivery.builder()
                                    .id(id)
                                    .latitude(coordinates.get(index).getY())
                                    .longitude(coordinates.get(index).getX())
                                    .temperature(currentTemperature)
                                    .arrival_time(arrivalTime)
                                    .delivery_time(deliveryTime)
                                    .build();
                            sensorMapper.insertDelivery(delivery);
                            isProcessed = true;
                            messageService.sendMessage("Coordinate insert");
                            index++;
                        } else {
                            currentTemperature = converter(currentTemperature);
                            Delivery delivery = Delivery.builder()
                                    .id(id)
                                    .latitude(coordinates.get(index).getY())
                                    .longitude(coordinates.get(index).getX())
                                    .temperature(currentTemperature)
                                    .delivery_time(deliveryTime)
                                    .build();
                            sensorMapper.updateDelivery(delivery);
                            messageService.sendMessage("Coordinate update");
                            index++;
                        }
                    } else {
                        Delivery delivery = Delivery.builder()
                                .id(id)
                                .delivery_time(arrivalTime)
                                .build();
                        sensorMapper.endDelivery(delivery);
                        cancelScheduledFuture(scheduledFutureRef);
                        messageService.sendMessage("Coordinate end");
                    }
                } catch (Exception e) {
                    log.error("Coordinate processing failure: ", e);
                    cancelScheduledFuture(scheduledFutureRef);
                }
            }
        };
    }

    /**
     * 스케줄링된 작업을 취소합니다.
     * 만약 작업이 이미 시작되어 있다면, 작업은 현재 진행 중인 부분까지 완료됩니다.
     *
     * @param scheduledFutureRef 스케줄링 작업을 관리하기 위한 AtomicReference 객체
     */
    private void cancelScheduledFuture(AtomicReference<ScheduledFuture<?>> scheduledFutureRef) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureRef.get();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true); // true로 변경하여 작업을 중지
        }
    }
}
