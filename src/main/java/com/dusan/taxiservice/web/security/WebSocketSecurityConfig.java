package com.dusan.taxiservice.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static com.dusan.taxiservice.web.api.websocket.SocketDestinations.*;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpSubscribeDestMatchers(
                        USER_PREFIX + QUEUE_PREFIX + ERROR,
                        TOPIC_PREFIX + CREATED_RIDE,
                        TOPIC_PREFIX + UPDATED_RIDE,
                        TOPIC_PREFIX + CANCELED_RIDE
                ).hasAnyRole("DRIVER", "DISPATCHER")
                .simpSubscribeDestMatchers(
                        USER_PREFIX + QUEUE_PREFIX + FORMED_RIDE,
                        USER_PREFIX + QUEUE_PREFIX + PROCESSED_RIDE
                ).hasRole("DRIVER")
                .simpSubscribeDestMatchers(
                        TOPIC_PREFIX + FAILED_RIDE,
                        TOPIC_PREFIX + SUCCESSFUL_RIDE
                ).hasRole("DISPATCHER")
                .simpDestMatchers(
                        APP_PREFIX + FORMED_RIDE,
                        APP_PREFIX + PROCESSED_RIDE)
                .hasRole("DISPATCHER")
                .simpDestMatchers(
                        APP_PREFIX + ACCEPTED_RIDE,
                        APP_PREFIX + FAILED_RIDE,
                        APP_PREFIX + SUCCESSFUL_RIDE
                ).hasRole("DRIVER")
                .anyMessage().denyAll();

    }
}
