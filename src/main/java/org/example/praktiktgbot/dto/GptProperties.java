package org.example.praktiktgbot.dto;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("api")
@Configuration
public class GptProperties {
    private String apiKey;
    private String apiUrl;
}
