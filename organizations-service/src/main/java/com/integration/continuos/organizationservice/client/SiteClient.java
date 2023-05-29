package com.integration.continuos.organizationservice.client;

import com.integration.continuos.organizationservice.model.Site;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class SiteClient {

    private final RestTemplate restTemplate;

    public SiteClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Site> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        String baseUrl = "http://site:8082/api/sites/organization/" + organizationId;
        ResponseEntity<List<Site>> rateResponse =
                restTemplate.exchange(baseUrl,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Site>>() {
                        });
        return rateResponse.getBody();
    }

    public List<Site> findByOrganizationWithUsers(@PathVariable("organizationId") Long organizationId) {
        String baseUrl = "http://site:8082/api/sites/organization/" + organizationId + "/with-users";
        ResponseEntity<List<Site>> rateResponse =
                restTemplate.exchange(baseUrl,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Site>>() {
                        });
        return rateResponse.getBody();
    }

}
