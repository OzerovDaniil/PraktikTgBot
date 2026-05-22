package org.example.praktiktgbot.Services;

import org.example.praktiktgbot.dto.TelegramProperties;
import org.example.praktiktgbot.dto.WebhookProperties;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TelegramBot extends SpringWebhookBot {

    private final TelegramProperties telegramProperties;
    private final WebhookProperties webhookProperties;
    private final GptService gptService;
    private final UserRequestService userRequestService;
    private final LessonScheduleService lessonScheduleService;

    private final Map<Long, Boolean> waitingForQuestion = new HashMap<>();
    private final SetWebhook setWebhook;

    public TelegramBot(TelegramProperties telegramProperties,
                       WebhookProperties webhookProperties,
                       GptService gptService,
                       UserRequestService userRequestService,
                       LessonScheduleService lessonScheduleService,
                       SetWebhook setWebhook) {
        super(setWebhook, telegramProperties.getToken());
        this.telegramProperties = telegramProperties;
        this.webhookProperties = webhookProperties;
        this.gptService = gptService;
        this.userRequestService = userRequestService;
        this.lessonScheduleService = lessonScheduleService;
        this.setWebhook = setWebhook;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return null;
        }

        Long chatId = update.getMessage().getChatId();
        String userMessage = update.getMessage().getText().trim();
        String botResponse = "";

        if (waitingForQuestion.getOrDefault(chatId, false)) {
            if (userMessage.equals("/stopask")) {
                botResponse = "Ви вийшли з режиму питань до AI.";
                waitingForQuestion.put(chatId, false);
            } else {
                botResponse = gptService.answer(userMessage);
                if (botResponse == null || botResponse.isEmpty()) {
                    botResponse = "Виникла помилка при обробці запиту.";
                }
                userRequestService.saveRequest(chatId, userMessage, botResponse);
            }
        } else {
            switch (userMessage) {
                case "/start":
                    botResponse = "Привіт! Я бот-помічник. Чим можу допомогти?";
                    break;

                case "/help":
                    botResponse = """
                            Ось що я вмію:
                            📌 /start - Запуск
                            🧠 /ask - Питання AI (включає режим)
                            ⛔ /stopask - Вийти з режиму AI
                            📅 /schedule - Розклад на сьогодні
                            📜 /history - Історія запитів
                            ⛔ /stop - Зупинити бота
                            """;
                    break;

                case "/ask":
                    botResponse = "Введіть ваше запитання для AI. Щоб вийти, напишіть /stopask";
                    waitingForQuestion.put(chatId, true);
                    break;

                case "/stopask":
                    botResponse = "Ви не в режимі запитань до AI.";
                    break;

                case "/schedule":
                    botResponse = lessonScheduleService.getScheduleForDate(LocalDate.now().toString());
                    if (botResponse == null || botResponse.isEmpty()) {
                        botResponse = "Розклад порожній чи недоступний.";
                    }
                    break;

                case "/history":
                    botResponse = userRequestService.getUserHistory(chatId);
                    if (botResponse == null || botResponse.isEmpty()) {
                        botResponse = "Історія порожня чи недоступна.";
                    }
                    break;

                case "/stop":
                    botResponse = "Бот зупинено. Ви можете перезапустити його командою /start.";
                    break;

                default:
                    botResponse = "Я не розумію цієї команди. Введіть /help для списку доступних команд.";
                    break;
            }
        }

        return createMessage(chatId, botResponse);
    }

    private SendMessage createMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setParseMode("Markdown");

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        KeyboardRow row1 = new KeyboardRow(List.of(
                new KeyboardButton("/ask"),
                new KeyboardButton("/stopask")
        ));
        KeyboardRow row2 = new KeyboardRow(List.of(
                new KeyboardButton("/schedule"),
                new KeyboardButton("/history")
        ));
        KeyboardRow row3 = new KeyboardRow(List.of(
                new KeyboardButton("/help"),
                new KeyboardButton("/stop")
        ));

        keyboardMarkup.setKeyboard(List.of(row1, row2, row3));
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    @Override
    public String getBotUsername() {
        return telegramProperties.getUsername();
    }

    @Override
    public String getBotPath() {
        return webhookProperties.getPath();
    }
}