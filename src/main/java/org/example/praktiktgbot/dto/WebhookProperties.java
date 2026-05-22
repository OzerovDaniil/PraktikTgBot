package org.example.praktiktgbot.dto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("webhook")
@Configuration
public class WebhookProperties {
    private String path;
}