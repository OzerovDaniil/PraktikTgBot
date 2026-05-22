package org.example.praktiktgbot;

import org.example.praktiktgbot.Configs.AppConfig;
import org.example.praktiktgbot.Model.LessonSchedule;
import org.example.praktiktgbot.Repo.LessonScheduleRepository;
import org.example.praktiktgbot.Services.LessonScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LessonScheduleServiceIntegrationTest {

    @MockitoBean
    private AppConfig appConfig;

    @MockitoBean
    private org.telegram.telegrambots.meta.api.methods.updates.SetWebhook setWebhook;

    @Autowired
    private LessonScheduleService lessonScheduleService;

    @Autowired
    private LessonScheduleRepository lessonScheduleRepository;

    @BeforeEach
    void setUp() {
        lessonScheduleRepository.deleteAll();
    }

    @Test
    void getScheduleForDate_shouldReturnSchedule_whenLessonsExist() {
        LessonSchedule lesson = new LessonSchedule();
        lesson.setNameLesson("Математика");
        lesson.setLocalDateTime(LocalDateTime.of(2026, 5, 12, 8, 0));
        lessonScheduleRepository.save(lesson);

        String result = lessonScheduleService.getScheduleForDate("2026-05-12");

        assertTrue(result.contains("Математика"));
        assertTrue(result.contains("08:00"));
    }

    @Test
    void getScheduleForDate_shouldReturnMultipleLessons_whenSeveralExist() {
        LessonSchedule lesson1 = new LessonSchedule();
        lesson1.setNameLesson("Математика");
        lesson1.setLocalDateTime(LocalDateTime.of(2026, 5, 12, 8, 0));

        LessonSchedule lesson2 = new LessonSchedule();
        lesson2.setNameLesson("Фізика");
        lesson2.setLocalDateTime(LocalDateTime.of(2026, 5, 12, 9, 45));

        lessonScheduleRepository.save(lesson1);
        lessonScheduleRepository.save(lesson2);

        String result = lessonScheduleService.getScheduleForDate("2026-05-12");

        assertTrue(result.contains("Математика"));
        assertTrue(result.contains("Фізика"));
        assertTrue(result.contains("09:45"));
    }

    @Test
    void getScheduleForDate_shouldNotReturnOtherDays() {
        LessonSchedule lesson = new LessonSchedule();
        lesson.setNameLesson("Хімія");
        lesson.setLocalDateTime(LocalDateTime.of(2026, 5, 13, 8, 0));
        lessonScheduleRepository.save(lesson);

        String result = lessonScheduleService.getScheduleForDate("2026-05-12");

        assertFalse(result.contains("Хімія"));
        assertTrue(result.contains("нет расписания"));
    }

    @Test
    void getScheduleForDate_shouldReturnError_whenInvalidFormat() {
        String result = lessonScheduleService.getScheduleForDate("12/05/2026");

        assertTrue(result.contains("Неверный формат даты"));
    }

    @Test
    void getScheduleForDate_shouldReturnEmpty_whenNoLessons() {
        String result = lessonScheduleService.getScheduleForDate("2026-01-01");

        assertTrue(result.contains("нет расписания"));
    }
}