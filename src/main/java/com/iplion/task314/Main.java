package com.iplion.task314;

import com.iplion.task314.model.User;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class Main {
    private static final String URL = "http://94.198.50.185:7081/api/users";
    private static final RestTemplate restTemplate = new RestTemplate();

    static {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public static void main(String[] args) {

        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);
        String sessionId = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionId);

        User myUser = new User();
        myUser.setId(3L);
        myUser.setName("James");
        myUser.setLastName("Brown");
        myUser.setAge(33);

        HttpEntity<User> request = new HttpEntity<>(myUser, headers);
        String code1 = restTemplate.postForObject(URL, request, String.class);

        myUser.setName("Thomas");
        myUser.setLastName("Shelby");

        ResponseEntity<String> updateResponse = restTemplate.exchange(URL, HttpMethod.PUT, request, String.class);
        String code2 = updateResponse.getBody();

        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(URL + "/3", HttpMethod.DELETE, deleteRequest, String.class);
        String code3 = deleteResponse.getBody();

        System.out.println("Final code: " + code1 + code2 + code3);
    }
}
