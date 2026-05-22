# Telegram AI Bot

Серверна частина Telegram-бота-помічника з інтеграцією AI API на базі Spring Boot.

## Опис проєкту

Проєкт реалізує Telegram-бота, який працює через webhook, приймає повідомлення користувачів, передає запити до AI API, повертає відповідь користувачу та зберігає історію повідомлень у PostgreSQL.

## Основний функціонал

- обробка Telegram-команд;
- режим взаємодії з AI API;
- збереження історії запитів;
- отримання розкладу занять;
- робота через webhook;
- інтеграція з PostgreSQL;
- інтеграційне тестування.

## Технології

- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- Telegram Bot API
- AI API
- Gson
- Gradle
- JUnit 5
- MockMvc
- ngrok
- Dockerfile

## Конфігурація

Для запуску потрібно створити `.env` файл у корені проєкту:

```env
TELEGRAM_TOKEN=your_telegram_token
TELEGRAM_USERNAME=your_bot_username
GEMINI_API_KEY=your_gemini_api_key
WEBHOOK_PATH=https://your-ngrok-url

DB_URL=jdbc:postgresql://localhost:5432/bot
DB_USERNAME=postgres
DB_PASSWORD=root
```

## Запуск ngrok

Для роботи webhook необхідно запустити HTTPS-тунель через ngrok.

Команда запуску:

```bash
ngrok http 8080