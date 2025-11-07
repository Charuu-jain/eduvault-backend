package com.eduvault.controller;

import com.eduvault.model.Reminder;
import com.eduvault.repository.ReminderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderRepository reminderRepository;

    public ReminderController(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @GetMapping
    public List<Reminder> getAllReminders() {
        return reminderRepository.findAllByOrderByDueDateAsc();
    }

    @PostMapping
    public Reminder addReminder(@RequestBody Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    @DeleteMapping("/{id}")
    public void deleteReminder(@PathVariable Long id) {
        reminderRepository.deleteById(id);
    }

    @GetMapping("/next")
    public Reminder getNextReminder() {
        return reminderRepository.findFirstByOrderByDueDateAsc().orElse(null);
    }
}