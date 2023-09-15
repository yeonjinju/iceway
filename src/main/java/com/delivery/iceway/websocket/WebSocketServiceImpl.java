package com.delivery.iceway.websocket;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.delivery.iceway.domain.Delivery;
import com.delivery.iceway.domain.Recall;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessageSendingOperations messagingTemplate;
    
    /**
     * 대시보드 업데이트를 알리기 위해 WebSocket을 통해 클라이언트에게 메시지를 전송합니다.
     *
     * @param deliveryList 대시보드에 업데이트할 배송 목록
     */
    @Override
    public void notifyDashboardUpdate(List<Delivery> deliveryList) {
        // "/topic/location" 주제로 대시보드 클라이언트에게 deliveryList를 메시지로 보냅니다.
        messagingTemplate.convertAndSend("/topic/location", deliveryList);
    }
    
    @Override
    public void recallsUpdate(List<Recall> recalls) {
        messagingTemplate.convertAndSend("/topic/location", recalls);
    }
}
