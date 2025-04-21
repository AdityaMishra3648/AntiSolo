package com.AntiSolo.AntiSolo.Configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Arrays;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${frontend.url}")
    private String frontendurl;

//    private final WebSocketAuthInterceptor webSocketAuthInterceptor;
//
//    public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
//        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/chat") // WebSocket connection endpoint
//                .setAllowedOrigins("*")
//                .withSockJS(); // Enable SockJS fallback
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic", "/queue","/user"); // Enables a broker for pub-sub
//        registry.setApplicationDestinationPrefixes("/app"); // Messages sent to @MessageMapping
//        registry.setUserDestinationPrefix("/user"); // For private messages
//    }
//
//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(webSocketAuthInterceptor);
//    }


    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    public WebSocketConfig(WebSocketAuthInterceptor webSocketAuthInterceptor) {
        this.webSocketAuthInterceptor = webSocketAuthInterceptor;
    }




    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue","/user","/group"); // Support both public and private messaging
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user"); // Prefix for private messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat") // Endpoint for WebSocket connection
//                .addInterceptors(new JwtHandshakeInterceptor())
                .setAllowedOrigins(
                        frontendurl
//                        "http://127.0.0.1:5500",
//                        "http://localhost:3000",
//                        "http://localhost:5173",
//                        "http://your-domain.com",
//                        "https://your-secure-domain.com",
//                        "http://localhost:9000",
//                        "http://localhost:4200",
//                        "http://localhost:8080",
//                        "http://localhost:8081"
                )
//                .setAllowedOriginPatterns("*") // If your frontend uses dynamic subdomains
                .withSockJS();

    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration){
        registration.interceptors((webSocketAuthInterceptor));
    }

}
