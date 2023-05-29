package com.integration.continuos.siteservice.controller;

import com.integration.continuos.siteservice.client.UserClient;
import com.integration.continuos.siteservice.model.Site;
import com.integration.continuos.siteservice.service.SiteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    private final UserClient userClient;

    public SiteController(SiteService siteService, UserClient userClient) {
        this.siteService = siteService;
        this.userClient = userClient;
    }

    @PostMapping
    public Site add(@RequestBody Site site) {
        return siteService.save(site);
    }

    @GetMapping
    public List<Site> findAll() {
        return siteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> findById(@PathVariable("id") Long id) {
        Site site = siteService.get(id);
        return new ResponseEntity<>(site, HttpStatus.OK);
    }

    @GetMapping("/organization/{organizationId}")
    public List<Site> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        return siteService.getByOrganizationId(organizationId);
    }

    @GetMapping("/organization/{organizationId}/with-users")
    public List<Site> findByOrganizationWithUsers(@PathVariable("organizationId") Long organizationId) {
        List<Site> sites = siteService.getByOrganizationId(organizationId);
        sites.forEach(s -> s.setUsers(userClient.getUser(s.getId())));
        return sites;
    }
}
