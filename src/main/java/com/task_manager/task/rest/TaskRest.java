package com.task_manager.task.rest;

import com.task_manager.task.POJO.TaskRequest;
import com.task_manager.task.models.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/task")
public interface TaskRest {
    @GetMapping("getTasks")
    ResponseEntity<List<Task>> getAllTasks();
    @PostMapping("create")
    ResponseEntity<String> createTask(@RequestBody Map<String, String> requestMap);

    @DeleteMapping("delete/{id}")
    ResponseEntity<String> deleteTask(@PathVariable("id") long id);

    @PutMapping("update/{taskId}")
    ResponseEntity<String> updateTask(@PathVariable Long taskId,
                                      @RequestBody Map<String, String> requestMap);

}
