package com.delivery.iceway.generator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.delivery.iceway.IcewayApplication;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GeneratorController {

    private final IcewayApplication icewayApplication;

    /**
     * 스케쥴러를 시작합니다.
     */
    @PostMapping("/startScheduler")
    public ResponseEntity<?> startScheduler() {
        icewayApplication.startScheduler();
        return ResponseEntity.ok("{\"status\":\"success\"}");
    }

    /**
     * 스케쥴러를 중지합니다.
     */
    @PostMapping("/stopScheduler")
    public ResponseEntity<?> stopScheduler() {
        icewayApplication.stopScheduler();
        return ResponseEntity.ok("{\"status\":\"success\"}");
    }

    /**
     * 스케쥴러를 리셋합니다.
     */
    @PostMapping("/resetScheduler")
    public ResponseEntity<?> resetScheduler() {
        try {
            icewayApplication.resetScheduler(); // 데이터베이스 초기화
            return new ResponseEntity<>(HttpStatus.OK); // 200 OK 응답
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error 응답
        }
    }
}
