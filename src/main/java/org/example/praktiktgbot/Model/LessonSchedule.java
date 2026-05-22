package org.example.praktiktgbot.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lesson_schedule")
@Getter
@Setter
public class LessonSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameLesson;

    private LocalDateTime localDateTime;

    public LessonSchedule() {
    }

    public LessonSchedule(Long id, String nameLesson, LocalDateTime localDateTime) {
        this.id = id;
        this.nameLesson = nameLesson;
        this.localDateTime = localDateTime;
    }
}
