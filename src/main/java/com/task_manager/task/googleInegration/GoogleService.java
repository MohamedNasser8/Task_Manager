package com.task_manager.task.googleInegration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GoogleService {
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${google.auth.clientId}")
    private String clientId ;

    @Value("${google.auth.clientSecret}")
    private String clientSecret;

    public void addEventToCalendar(String accessToken, Task task) throws JsonProcessingException {
        log.info("Inside create add event to google calendar");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> event = new HashMap<>();
        event.put("summary", task.getTitle());
        event.put("description", task.getDescription());

        Map<String, Object> startDateTime = new HashMap<>();
        DateTimeFormatter googleDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        ZonedDateTime StartZonedDateTime = task.getStartDate().atZone(ZoneId.systemDefault());
        ZonedDateTime endZonedDateTime = task.getDueDate().atZone(ZoneId.systemDefault());
        String formattedStart = StartZonedDateTime.format(googleDateTimeFormatter);
        String formattedEnd = endZonedDateTime.format(googleDateTimeFormatter);

        startDateTime.put("dateTime", formattedStart);
        startDateTime.put("timeZone", StartZonedDateTime.getZone().toString());

        Map<String, Object> endDateTime = new HashMap<>();
        endDateTime.put("dateTime", formattedEnd);
        endDateTime.put("timeZone", endZonedDateTime.getZone().toString());
        event.put("start", startDateTime);
        event.put("end", endDateTime);


        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(event);
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://www.googleapis.com/calendar/v3/calendars/primary/events",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

    }

    public String refreshAccessToken(String refreshToken) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);
        map.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token", request, String.class);

        // Extract the new access token from the response
        String responseBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(responseBody);
        return root.path("access_token").asText();
    }

}
