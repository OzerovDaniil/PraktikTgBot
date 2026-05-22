package org.example.praktiktgbot;

import org.example.praktiktgbot.Configs.AppConfig;
import org.example.praktiktgbot.Services.TelegramBot;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebhookControllerIntegrationTest {

    @MockitoBean
    private AppConfig appConfig;

    @MockitoBean
    private SetWebhook setWebhook;

    @MockitoBean
    private TelegramBot telegramBot;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void webhook_shouldAcceptPostRequest() throws Exception {
        String updateJson = """
            {
              "update_id": 123,
              "message": {
                "message_id": 1,
                "chat": {"id": 123, "type": "private"},
                "text": "/start",
                "date": 1234567890
              }
            }
            """;

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }
}