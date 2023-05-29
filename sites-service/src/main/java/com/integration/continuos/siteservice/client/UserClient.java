package com.integration.continuos.siteservice.client;

import com.integration.continuos.siteservice.model.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class UserClient {

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getUser(Long siteId) {

        String baseUrl = "http://user:8081/api/users/site/" + siteId;
        ResponseEntity<List<User>> rateResponse =
                restTemplate.exchange(baseUrl,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
                        });
        return rateResponse.getBody();
    }

}
