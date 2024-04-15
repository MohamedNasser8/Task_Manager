package com.task_manager.task.restImpl;

import com.task_manager.task.models.Task;
import com.task_manager.task.rest.TaskRest;
import com.task_manager.task.service.TaskService;
import com.task_manager.task.utils.TaskConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000") // Allow only this origin

public class TaskRestImpl implements TaskRest {
    @Autowired
    TaskService taskService;

    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        return taskService.getAllTasks();
    }

    @Override
    public ResponseEntity<String> createTask(Map<String, String>requestMap) {
        try {
            return taskService.createTask(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(TaskConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteTask(long id) {
        return taskService.deleteTask(id);
    }

    @Override
    public ResponseEntity<String> updateTask(Long taskId, Map<String, String> requestMap) {
        return taskService.updateTask(taskId, requestMap);
    }
}
