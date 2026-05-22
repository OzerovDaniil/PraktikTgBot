package org.example.praktiktgbot.Services;

import org.example.praktiktgbot.Model.LessonSchedule;
import org.example.praktiktgbot.Repo.LessonScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonScheduleService {
    private final LessonScheduleRepository lessonScheduleRepository;

    public LessonScheduleService(LessonScheduleRepository lessonScheduleRepository) {
        this.lessonScheduleRepository = lessonScheduleRepository;
    }

    public String getScheduleForDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, formatter);
        } catch (Exception e) {
            return "Неправильний формат дати. Використовуйте yyyy-MM-DD.";
        }

        List<LessonSchedule> lessons = lessonScheduleRepository.findAll()
                .stream()
                .filter(lesson -> lesson.getLocalDateTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        if (lessons.isEmpty()) {
            return "На вибрану дату немає розкладу.";
        }

        StringBuilder response = new StringBuilder("Розклад на " + date + ":\n");
        for (LessonSchedule lesson : lessons) {
            response.append("- ").append(lesson.getNameLesson())
                    .append(" у ")
                    .append(lesson.getLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .append("\n");
        }
        return response.toString();
    }
}
