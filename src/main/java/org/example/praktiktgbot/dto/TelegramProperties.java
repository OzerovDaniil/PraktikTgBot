package org.example.praktiktgbot.dto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("telegram")
@Configuration
public class TelegramProperties {
    private String token;
    private String username;
}
