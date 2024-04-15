package com.task_manager.task.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task_manager.task.dao.TokenRep;
import com.task_manager.task.dao.UserRep;
import com.task_manager.task.googleInegration.GoogleResponse;
import com.task_manager.task.models.Token;
import com.task_manager.task.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@RestController
public class GoogleAuthentication {

    @Autowired
    TokenRep tokenRep;

    @Autowired
    UserRep userRep;
    @Value("${google.auth.clientId}")
    private String clientId ;

    @Value("${google.auth.clientSecret}")
    private String clientSecret;

    @Value("${google.auth.redirectUri}")
    private String redirectURI;

    @Value("${google.auth.scope}")
    private String scope;

    @Value("${google.auth.tokenUri}")
    private String tokenUri;

        private RestTemplate restTemplate = new RestTemplate();

    @GetMapping(value = "/login/google")
    public RedirectView googleConnectionStatus(@RequestParam(value = "returnTo", required = false) String returnUrl) {
        String authorizationUri = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + clientId +
                "&redirect_uri=" + redirectURI +
                "&scope=" + scope +
                "&response_type=code"+
                "&include_granted_scopes=true"+
                "&access_type=offline"+
                "&state="+ returnUrl;
        return new RedirectView(authorizationUri);
    }

    @GetMapping(value = "/code/google", params = "code")
    public RedirectView oauth2Callback(@RequestParam(value = "code") String code,
                                                 @RequestParam(value = "state") String returnUrl) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", redirectURI);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        String responseBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        GoogleResponse googleResponse = mapper.readValue(responseBody, GoogleResponse.class);
//        System.out.println(googleResponse.getAccessToken());

        headers = new HttpHeaders();
        headers.setBearerAuth(googleResponse.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

        response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);

        String email= getUserEmail(googleResponse.getAccessToken());

        Optional<User> userOptional = userRep.findByEmail(email);
        User user = userOptional.orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setTasks(new ArrayList<>());
            return newUser;
        });
        Optional.ofNullable(user.getToken())
                .ifPresent(token -> {
                    user.setToken(null);
                    tokenRep.deleteById(token.getId());
                });
        Token token = createTokenFromGoogleResponse(googleResponse);
        user.setToken(token);
        token.setUser(user);
        userRep.save(user);

        return new RedirectView(returnUrl);
    }

    private Token createTokenFromGoogleResponse(GoogleResponse googleResponse) {
        Token token = new Token();
        token.setAccessToken(googleResponse.getAccessToken());
        token.setRefreshToken(googleResponse.getRefreshToken());
        token.setExpiresAt(googleResponse.getExpiresIn());
        return token;
    }

    public String getUserEmail(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://openidconnect.googleapis.com/v1/userinfo";

        ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, String.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("email").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
