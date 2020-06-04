package com.company.controller;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthRespond authenticate(@RequestBody AuthBody authBody) {
        String role = "";
        User user = subordinateTasksService.getByEmail(authBody.getEmail()).block();
        if (user == null) {
            user = managerTasksService.getByEmail(authBody.getEmail()).block();
            role = ManagerUser.getRole();
        }
        else {
            role = SubordinateUser.getRole();
        }

        if (passwordEncoder.matches(authBody.getPassword(), user.getPassword())) {
            return new AuthRespond(user.getUserID(), role, true);
        }
        else
            return new AuthRespond("", "", false);
    }
}
