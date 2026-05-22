package org.example.praktiktgbot.Configs;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.praktiktgbot.dto.TelegramProperties;
import org.example.praktiktgbot.dto.WebhookProperties;
import org.example.praktiktgbot.Repo.WebHookUpdateResponse;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@AllArgsConstructor
@Slf4j
public class AppConfig {
    private TelegramProperties telegramProperties;
    private WebhookProperties webhookProperties;

    @Bean
    public SetWebhook setWebhookInstance(WebhookProperties properties) {
        return SetWebhook.builder().url(properties.getPath()).build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updateTelegramWebhookPath(){
        RestTemplate restTemplate = new RestTemplate();
        WebHookUpdateResponse response = Objects.requireNonNull(
                restTemplate.getForObject("https://api.telegram.org/bot%s/setWebhook?url=%s".formatted(telegramProperties.getToken(), webhookProperties.getPath()),
                        WebHookUpdateResponse.class));
        log.info(response.getDescription());
    }
}