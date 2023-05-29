package com.integration.continuos.organizationservice.controller;

import com.integration.continuos.organizationservice.client.SiteClient;
import com.integration.continuos.organizationservice.client.UserClient;
import com.integration.continuos.organizationservice.model.Organization;
import com.integration.continuos.organizationservice.service.OrganizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    private final SiteClient siteClient;

    private final UserClient userClient;

    public OrganizationController(OrganizationService organizationService, SiteClient siteClient, UserClient userClient) {
        this.organizationService = organizationService;
        this.siteClient = siteClient;
        this.userClient = userClient;
    }

    @PostMapping
    public Organization add(@RequestBody Organization organization) {
        return organizationService.save(organization);
    }

    @GetMapping
    public List<Organization> findAll() {
        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(organizationService.get(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-sites")
    public Organization findByIdWithSites(@PathVariable("id") Long id) {
        Organization organization = organizationService.get(id);
        organization.setSites(siteClient.findByOrganization(organization.getId()));
        return organization;
    }

    @GetMapping("/{id}/with-sites-with-users")
    public Organization findByIdWithSitesAndUsers(@PathVariable("id") Long id) {
        Organization organization = organizationService.get(id);
        organization.setSites(siteClient.findByOrganizationWithUsers(organization.getId()));
        return organization;
    }

    @GetMapping("/{id}/with-users")
    public Organization findByIdWithUsers(@PathVariable("id") Long id) {
        Organization organization = organizationService.get(id);
        try {
            organization.setUsers(userClient.getUser(organization.getId()));
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return organization;
    }
}
