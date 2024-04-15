package com.task_manager.task.taskSync;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.task_manager.task.dao.TaskRep;
import com.task_manager.task.dao.UserRep;
import com.task_manager.task.googleInegration.GoogleService;
import com.task_manager.task.models.Task;
import com.task_manager.task.models.Token;
import com.task_manager.task.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.List;

@Service
@Slf4j
public class TaskSynchronizer{
    @Autowired
    TaskRep taskRep;

    @Autowired
    UserRep userRep;

    @Autowired
    GoogleService googleService;

    @Autowired
    CacheManager cacheManager;
    @Scheduled(fixedRate = 60000)
    public boolean isInternetAvailable() {
        log.info("Inside Sync offline tasks to google Calendar");
        try {
//            checking internet connectivity
            InetAddress address = InetAddress.getByName("google.com");
            boolean connected = address.isReachable(2000);

//            iterates through a list of tasks
            List<Task> taskList = taskRep.findAll();
            taskList.forEach(task -> {
                User currentUser = task.getUser();

//              If expired, it attempts to refresh the token
                if(currentUser.getToken().isExpired()){
                    String newAccessToken = null;
                    try {
                        newAccessToken = googleService.refreshAccessToken(currentUser.getToken().getRefreshToken());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    currentUser.getToken().setAccessToken(newAccessToken);
                    currentUser.getToken().setExpiresAt(3599);
                    userRep.save(currentUser);
                }
                Token userToken = currentUser.getToken();
//                If the token is valid and the task is not yet synced ,
//                it adds the task to the Google Calendar
                if(!userToken.isExpired() && !task.getSynced()){
                    try {
                        googleService.addEventToCalendar(currentUser.getToken().getAccessToken(), task);

                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    task.setSynced(true); // Mark the task as synced
                    taskRep.save(task); // Save the updated task
                    cacheManager.getCache("all_tasks").clear();
                }
            });
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Scheduled(fixedRate = 300000)
    public void scheduleCacheUpdate(){
        log.info("Inside Remove Cache");
//        the cache named "all_tasks" is cleared
        cacheManager.getCache("all_items").clear();
    }

}
