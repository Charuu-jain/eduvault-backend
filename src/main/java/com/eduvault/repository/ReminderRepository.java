package com.eduvault.repository;

import com.eduvault.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findAllByOrderByDueDateAsc();
    Optional<Reminder> findFirstByOrderByDueDateAsc();
}