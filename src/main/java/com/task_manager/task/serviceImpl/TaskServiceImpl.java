package com.task_manager.task.serviceImpl;


import com.task_manager.task.dao.TaskRep;
import com.task_manager.task.dao.UserRep;
import com.task_manager.task.googleInegration.GoogleService;
import com.task_manager.task.models.Task;
import com.task_manager.task.models.User;
import com.task_manager.task.service.TaskService;
import com.task_manager.task.utils.TaskConstants;
import com.task_manager.task.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRep taskRep;

    @Autowired
    UserRep userRep;

    @Autowired
    GoogleService googleService;

    public Task mapTaskRequestToTask(Map<String, String> requestMap, Task newTask) {
        newTask.setLastUpdated(LocalDateTime.now());
        String day = requestMap.get("day");
        String startHour = requestMap.get("startHour");
        if (day != null && startHour != null) {
            newTask.setStartDate(getFormattedDateTime(day, startHour.substring(0, 5)));
        }
        String endHour = requestMap.get("endHour");
        if (day != null && endHour != null) {
            newTask.setDueDate(getFormattedDateTime(day, endHour.substring(0, 5)));
        }
        String description = requestMap.get("description");
        if (description != null) {
            newTask.setDescription(description);
        }
        String title = requestMap.get("title");
        if (title != null) {
            newTask.setTitle(title);
        }
        return newTask;
    }

    public LocalDateTime getFormattedDateTime(String day, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String dateTimeString = day + " " + time;
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, dateFormatter);


        return localDateTime;
    }

    @Override
    @CacheEvict(cacheNames = "all_tasks", allEntries = true)
    public ResponseEntity<String> createTask(Map<String, String> requestMap) {
        log.info("Inside create task");
        try {
//            Creates a new Task object.
            Task newTask = mapTaskRequestToTask(requestMap, new Task());

//            If the user is not found, a new user is created and saved.
            Optional<User> userOptional = userRep.findByEmail(requestMap.get("email"));
            User user = userOptional.orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(requestMap.get("email"));
                newUser.setTasks(new ArrayList<>());
                return newUser;
            });
//            Attempts to refresh the user's access token if it's expired.
            try {
                if (user.getToken().isExpired()) {
                    String newAccessToken = googleService.refreshAccessToken(user.getToken().getRefreshToken());
                    user.getToken().setExpiresAt(3599);
                    user.getToken().setAccessToken(newAccessToken);
                    userRep.save(user);
                } else {
                    try {
//                        add the new task to the user's Google Calendar
                        googleService.addEventToCalendar(user.getToken().getAccessToken(), newTask);
                    } catch (Exception ex) {
                        newTask.setSynced(false);
                    }
                }
            } catch (Exception ex) {
                newTask.setSynced(false);
                ex.printStackTrace();
                newTask.setUser(user);
                taskRep.save(newTask);
                return new ResponseEntity<>("You should authorize with google," +
                        " your task won't be reserved in the calendar", HttpStatus.CREATED);
            }

            newTask.setUser(user);
            taskRep.save(newTask);

            return TaskUtils.getResponseEntity(newTask.getSynced()
                    ? "Created Successfully and added to your calendar"
                    : "Created Successfully offline", HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TaskUtils.getResponseEntity(TaskConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @CacheEvict(cacheNames = "all_tasks", allEntries = true)
    public ResponseEntity<String> deleteTask(long id) {
        log.info("Inside delete task");
        try {
            Optional<Task> task = taskRep.findById(id);
            if (task.isPresent()) {
                taskRep.deleteById(id);
                return TaskUtils.getResponseEntity("deleted successfully", HttpStatus.OK);
            } else {
                return TaskUtils.getResponseEntity("Id is not found", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TaskUtils.getResponseEntity(TaskConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @CacheEvict(cacheNames = "all_tasks", allEntries = true)
    @Cacheable(cacheNames = "update_tasks")
    public ResponseEntity<String> updateTask(Long taskId, Map<String, String> requestMap) {
        log.info("Inside update task");
        Optional<Task> taskOptional = taskRep.findById(taskId);
        try {
            if (taskOptional.isEmpty())
                return TaskUtils.getResponseEntity("Id not found", HttpStatus.BAD_REQUEST);
            Task task = taskOptional.get();
            task = mapTaskRequestToTask(requestMap, task);

            taskRep.save(task);
            return TaskUtils.getResponseEntity("Task updated Successfully", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return TaskUtils.getResponseEntity(TaskConstants.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Cacheable(cacheNames = "all_tasks")
    @Override
    public ResponseEntity<List<Task>> getAllTasks() {
        log.info("Inside get Tasks");
        List<Task> tasksList = taskRep.findAll();
        return new ResponseEntity<>(tasksList, HttpStatus.OK);
    }

}
