package com.company.controller;

import com.company.model.*;
import com.company.service.ManagerTasksService;
import com.company.service.SubordinateTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Base64;

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

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
                .substring("Basic".length()).trim();
        return () ->  new String(Base64.getDecoder()
                .decode(authToken)).split(":")[0];
    }
}
