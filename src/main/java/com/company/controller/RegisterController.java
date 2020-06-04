package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/create/manager")
    public Mono<ManagerUser> createManagerUser(@RequestBody ManagerUser manager) {
        ManagerUser userExists = managerTasksService.getByEmail(manager.getEmail()).block();
        if (userExists != null) {
            throw new BadCredentialsException("Manager with email: " + manager.getEmail() + " already exists");
        }

        return managerTasksService.createManagerUser(manager.getName(), manager.getEmail(), manager.getPassword());
    }

    @PostMapping("/create/subordinate")
    public Mono<SubordinateUser> createSubordinateUser(@RequestBody SubordinateUser subordinate) {
        SubordinateUser userExists = subordinateTasksService.getByEmail(subordinate.getEmail()).block();
        if (userExists != null) {
            throw new BadCredentialsException("Subordinate with email: " + subordinate.getEmail() + " already exists");
        }

        return subordinateTasksService.createSubordinateUser(subordinate.getName(), subordinate.getEmail(), subordinate.getPassword(), subordinate.getManagerID(), subordinate.getScore(), subordinate.getPosition());
    }
}
