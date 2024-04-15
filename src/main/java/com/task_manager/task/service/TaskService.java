package com.task_manager.task.service;

import com.task_manager.task.POJO.TaskRequest;
import com.task_manager.task.models.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TaskService {
    ResponseEntity<String> createTask(Map<String, String> requestMap);

    ResponseEntity<String> deleteTask(long id);

    ResponseEntity<String> updateTask(Long taskId, Map<String, String> requestMap);

    ResponseEntity<List<Task>> getAllTasks();
}
