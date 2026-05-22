package org.example.praktiktgbot.Controller;

import lombok.AllArgsConstructor;
import org.example.praktiktgbot.Services.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@RestController("/")
public class WebhookController {
    private TelegramBot bot;

    @PostMapping
    public BotApiMethod<?> receive(@RequestBody Update update){
        return bot.onWebhookUpdateReceived(update);
    }
}