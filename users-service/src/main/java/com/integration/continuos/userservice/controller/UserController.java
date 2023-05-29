package com.integration.continuos.userservice.controller;

import java.util.List;
import java.util.Optional;

import com.integration.continuos.userservice.model.User;
import com.integration.continuos.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User add(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable("id") Long id) {
        User user = userService.get(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/site/{siteId}")
    public List<User> findBySite(@PathVariable("siteId") Long siteId) {
        return userService.getBySiteId(siteId);
    }

    @GetMapping("/organization/{organizationId}")
    public List<User> findByOrganization(@PathVariable("organizationId") Long organizationId) {
        return userService.getByOrganizationId(organizationId);
    }

}
