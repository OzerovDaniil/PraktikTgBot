package org.example.praktiktgbot;

import org.example.praktiktgbot.Configs.AppConfig;
import org.example.praktiktgbot.Repo.UserRequestRepository;
import org.example.praktiktgbot.Services.UserRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserRequestServiceIntegrationTest {

    @MockitoBean
    private AppConfig appConfig;

    @MockitoBean
    private SetWebhook setWebhook;

    @Autowired
    private UserRequestService userRequestService;

    @Autowired
    private UserRequestRepository userRequestRepository;

    @BeforeEach
    void setUp() {
        userRequestRepository.deleteAll();
    }

    @Test
    void saveRequest_shouldSaveToDatabase() {
        userRequestService.saveRequest(123L, "Привіт", "Привіт! Як справи?");
        assertEquals(1, userRequestRepository.count());
    }

    @Test
    void saveRequest_shouldSaveCorrectData() {
        userRequestService.saveRequest(123L, "що таке ООП?", "ООП це...");
        var saved = userRequestRepository.findAll().get(0);
        assertEquals(123L, saved.getChatId());
        assertEquals("що таке ООП?", saved.getMessage());
        assertEquals("ООП це...", saved.getResponse());
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void getUserHistory_shouldReturnHistory_whenExists() {
        userRequestService.saveRequest(123L, "що таке ООП?", "ООП це...");
        String history = userRequestService.getUserHistory(123L);
        assertTrue(history.contains("що таке ООП?"));
        assertTrue(history.contains("ООП це..."));
        assertTrue(history.contains("👤:"));
        assertTrue(history.contains("🤖:"));
    }

    @Test
    void getUserHistory_shouldReturnEmpty_whenNoHistory() {
        String history = userRequestService.getUserHistory(999L);
        assertTrue(history.contains("порожня"));
    }

    @Test
    void getUserHistory_shouldNotMixDifferentUsers() {
        userRequestService.saveRequest(111L, "Питання від user1", "Відповідь 1");
        userRequestService.saveRequest(222L, "Питання від user2", "Відповідь 2");
        String history = userRequestService.getUserHistory(111L);
        assertTrue(history.contains("Питання від user1"));
        assertFalse(history.contains("Питання від user2"));
    }

    @Test
    void getUserHistory_shouldReturnMax5Records() {
        for (int i = 1; i <= 7; i++) {
            userRequestService.saveRequest(123L, "Питання " + i, "Відповідь " + i);
        }
        String history = userRequestService.getUserHistory(123L);
        int count = history.split("👤:").length - 1;
        assertEquals(5, count);
    }

    @Test
    void saveMultipleRequests_shouldAllBeSaved() {
        userRequestService.saveRequest(123L, "Питання 1", "Відповідь 1");
        userRequestService.saveRequest(123L, "Питання 2", "Відповідь 2");
        userRequestService.saveRequest(123L, "Питання 3", "Відповідь 3");
        assertEquals(3, userRequestRepository.count());
    }
}