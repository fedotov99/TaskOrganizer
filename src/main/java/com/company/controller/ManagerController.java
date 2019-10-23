package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.service.ManagerTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ManagerController {
    @Autowired
    private ManagerTasksService managerTasksService;

    // create
    @RequestMapping("/create/manager")
    public String createManagerUser(@RequestParam String name) {
        ManagerUser mu = managerTasksService.createManagerUser(name);
        return mu.toString();
    }

    // retrieve
    @RequestMapping("/get/manager")
    public ManagerUser getManagerUserByID(@RequestParam String id) {
        return managerTasksService.getByUserID(id);
    }

    @RequestMapping("/get/manager")
    public ManagerUser getManagerUserByName(@RequestParam String name) {
        return managerTasksService.getByName(name);
    }

    @RequestMapping("/getAll/managers")
    public List<ManagerUser> getAllManagers(){
        return managerTasksService.getAll();
    }

    // update
    @RequestMapping("/update/manager")
    public String updateManagerUser(@RequestParam String id, @RequestParam String name, @RequestParam Map<String, SubordinateUser> subordinateList, @RequestParam Map<String, Task> uncheckedTasksList) {
        ManagerUser mu = managerTasksService.updateManagerUser(id, name, subordinateList, uncheckedTasksList);
        return mu.toString();
    }

    // delete
    @RequestMapping("/delete/manager")
    public String delete(@RequestParam String id) {
        managerTasksService.deleteById(id);
        return "Deleted manager id: " + id;
    }

    @RequestMapping("/deleteAll/managers")
    public String deleteAllManagers(){
        managerTasksService.deleteAll();
        return "Deleted all managers";
    }
}
