package com.task_manager.task.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "task")
@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime dueDate;



    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    private Boolean synced = true;



    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

}

enum TaskPriority {
    LOW, MEDIUM, HIGH
}

enum TaskStatus {
    TODO, IN_PROGRESS, COMPLETED
}