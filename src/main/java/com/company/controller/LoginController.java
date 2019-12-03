package com.company.controller;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import com.company.service.UserTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class LoginController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    @PostMapping("/login")
    public AuthRespond authenticate(@RequestBody AuthBody authBody) {
        String role = "";
        User user = subordinateTasksService.getByEmail(authBody.getEmail());
        if (user == null) {
            user = managerTasksService.getByEmail(authBody.getEmail());
            role = ManagerUser.getRole();
        }
        else {
            role = SubordinateUser.getRole();
        }

        if (user.getPassword().equals(authBody.getPassword())) {
            return new AuthRespond(user.getUserID(), role, true);
        }
        else
            return new AuthRespond("", "", false);
    }
}
