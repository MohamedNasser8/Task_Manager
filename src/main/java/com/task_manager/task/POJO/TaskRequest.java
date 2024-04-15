package com.task_manager.task.POJO;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class TaskRequest {
    String title;
    String description;

    @Enumerated(EnumType.STRING)
    TaskPriority priority;

    LocalDateTime dueDate;
    LocalDateTime startDate;

    String email;
    String username;
}

enum TaskPriority {
    LOW, MEDIUM, HIGH
}