package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import com.company.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private TaskService taskService;

    @PostMapping("/create/manager")
    public ManagerUser createManagerUser(@RequestBody ManagerUser manager) {
        ManagerUser mu = managerTasksService.createManagerUser(manager.getName(), manager.getEmail(), manager.getPassword());
        return mu;
    }

    @PostMapping("/create/subordinate")
    // TODO: get rid of @RequestParam String managerID, because it is in body
    public SubordinateUser createSubordinateUser(@RequestParam String managerID, @RequestBody SubordinateUser subordinate) {
        SubordinateUser su = subordinateTasksService.createSubordinateUser(subordinate.getName(), subordinate.getEmail(), subordinate.getPassword(), managerID, subordinate.getScore(), subordinate.getPosition());
        return su;
    }
}
