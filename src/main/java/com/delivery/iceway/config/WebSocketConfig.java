package com.delivery.iceway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 브로커를 활성화하고, "/topic" 프리픽스를 가진 대상 주제(destination)에 메시지를 브로드캐스트할 수 있도록 설정합니다.
        config.enableSimpleBroker("/topic");
    
        // 클라이언트에서 서버로 메시지를 보낼 때 사용할 프리픽스를 설정합니다.
        // 클라이언트에서 메시지를 서버로 보낼 때는 "/app" 프리픽스를 사용하게 됩니다.
        config.setApplicationDestinationPrefixes("/app");
    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket 엔드포인트를 등록하고, SockJS를 사용하여 브라우저에서 WebSocket을 지원하지 않을 때 대체 솔루션을 제공합니다.
        // 클라이언트는 "/ws" 경로로 WebSocket에 연결할 수 있게 됩니다.
        registry.addEndpoint("/ws").withSockJS();
    }
}
