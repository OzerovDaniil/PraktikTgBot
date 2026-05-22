package org.example.praktiktgbot.Repo;

import org.example.praktiktgbot.Model.LessonSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LessonScheduleRepository extends JpaRepository<LessonSchedule, Long> {
    List<LessonSchedule> findByLocalDateTimeBetween(LocalDate start, LocalDate end);
}
