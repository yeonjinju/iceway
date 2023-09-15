package com.delivery.iceway.status;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.delivery.iceway.domain.Delivery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/status")
@RequiredArgsConstructor
public class StatusController {

    private final StatusMapper statusMapper;

    @GetMapping
    public String getStatus() {
        return "status";
    }

    /**
     * "/requestData" 메시지 매핑을 통해 클라이언트로부터 요청을 처리하고,
     * 대시보드 클라이언트에게 전체 딜리버리 목록을 보냅니다.
     *
     * @return 전체 딜리버리 목록
     */
    @MessageMapping("/requestData")
    @SendTo("/topic/location")
    public List<Delivery> handleRequestData() {
        // 딜리버리 테이블을 전체 셀렉한 값을 가져옵니다.
        return statusMapper.getAllDelivery();
    }

    @MessageMapping("/requestRecall")
    @SendTo("/topic/location")
    public List<Delivery> handleRequestRecall(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
    try {
        Map<String, Object> messageData = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        Integer selectedMarker = (Integer) messageData.get("recallSelected");

        Timestamp recalltime = new Timestamp(System.currentTimeMillis());

        // selectedMarker 값을 사용하여 recall_status 업데이트를 수행합니다.
        statusMapper.recallUpdate(selectedMarker, recalltime);
        statusMapper.deleteRecall(selectedMarker);

    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }
        // 전체 테이블을 전체 셀렉한 값을 가져옵니다.
        return statusMapper.getAllDelivery();
    }
}
