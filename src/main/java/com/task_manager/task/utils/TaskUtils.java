package com.task_manager.task.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


public class TaskUtils {
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>
                ("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
