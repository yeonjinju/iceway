package com.delivery.iceway.amqp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Recall;
import com.delivery.iceway.status.StatusMapper;
import com.delivery.iceway.websocket.WebSocketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageReceiver {

    private List<Integer> processedIds = new ArrayList<>();
    private final WebSocketService webSocketService;
    private final StatusMapper statusMapper;

    /**
     * RabbitMQ에서 메시지를 수신하여 처리하는 메서드입니다.
     *
     * @param message 수신된 메시지
     */
    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        try {
            // 만약 메시지가 "Coordinate insert" 또는 "Coordinate update"인 경우
            if (message.equals("Coordinate insert") || message.equals("Coordinate update")) {
                // 모든 딜리버리 정보를 가져옵니다.
                List<Delivery> delivery = statusMapper.getAllDelivery();
                // 각 딜리버리 정보에 대해 처리를 수행합니다.
                for (Delivery d : delivery) {
                    // 온도가 -10 이상이고 이전에 처리되지 않은 경우에만 실행합니다.
                    if (d.getTemperature() >= -10 && !processedIds.contains(d.getId())) {
                        // Recall 객체를 생성하고 데이터베이스에 추가합니다.
                        Recall recall = Recall.builder()
                                .name(d.getCustomers_name())
                                .delivery_id(d.getId())
                                .build();
                        statusMapper.insertRecall(recall);
                        // 처리된 딜리버리의 ID를 기록합니다.
                        processedIds.add(d.getId());
                        // Recall 업데이트 메서드를 호출하여 웹 소켓을 통해 변경 내용을 클라이언트에게 알립니다.
                        webSocketService.recallsUpdate(statusMapper.getRecalls());
                    }
                }
                // 전체 딜리버리 목록을 사용하여 대시보드 클라이언트에게 업데이트를 알립니다.
                webSocketService.notifyDashboardUpdate(delivery);
            }
        } catch (Exception e) {
            // 예외 처리: 메시지 처리 중에 오류가 발생한 경우 오류 로그를 기록합니다.
            log.error("Error occurred while processing message: " + message, e);
        }
    }
}