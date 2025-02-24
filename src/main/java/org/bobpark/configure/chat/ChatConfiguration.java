package org.bobpark.configure.chat;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.util.MimeTypeUtils;

import reactor.util.retry.Retry;

import org.bobpark.configure.chat.properties.ChatProperties;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(ChatProperties.class)
public class ChatConfiguration {

    private final ChatProperties properties;

    @Bean
    public RSocketRequester getRSocketRequester(RSocketStrategies rSocketStrategies) {
        return RSocketRequester.builder()
            .rsocketConnector(connector -> connector.reconnect(Retry.backoff(10, Duration.ofMillis(500))))
            .rsocketStrategies(rSocketStrategies)
            .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
            .tcp(properties.rsHost(), properties.port());
    }
}
