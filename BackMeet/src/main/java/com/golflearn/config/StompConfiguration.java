package com.golflearn.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker//Stomp 사용 선언
@Configuration
public class StompConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {//Client에서 websocket연결할 때 사용할 API 경로를 설정
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
    	//Client에서 SEND 요청을 처리
    	registry.setApplicationDestinationPrefixes("/app");
    	
        //해당하는 경로를 SUBSCRIBE하는 Client에게 메세지를 전달
        registry.enableSimpleBroker("/queue", "/topic");
        //두 경로가 prefix(api 경로 맨 앞)에 붙은 경우, messageBroker가 잡아서 해당 채팅방의 클라이언트에게 메시지 전달
        //topic은 주로 1대 다 메세징일때 이용

    }
}