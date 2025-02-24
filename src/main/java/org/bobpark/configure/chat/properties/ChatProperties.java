package org.bobpark.configure.chat.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("chat")
public record ChatProperties(@DefaultValue("localhost") String rsHost,
                             @DefaultValue("8081") int port) {
}
