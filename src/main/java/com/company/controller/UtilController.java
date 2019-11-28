package com.company.controller;

import com.company.model.ManagerUser;
import com.company.service.ManagerTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UtilController {
    @Autowired
    private ManagerTasksService managerTasksService;
    @GetMapping("/util/manager")
    public List<ManagerUser> getAllManagers(){
        return managerTasksService.getAll();
    }

}
