package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.Task;
import com.company.service.ManagerTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ManagerController {
    @Autowired
    private ManagerTasksService managerTasksService;

    // create
    @PostMapping("/manager")
    public String createManagerUser(@RequestParam String name) {
        ManagerUser mu = managerTasksService.createManagerUser(name);
        return mu.toString();
    }

    // retrieve
    @GetMapping("/manager/{id}")
    public ManagerUser getManagerUserByID(@PathVariable String id) {
        return managerTasksService.getByUserID(id);
    }

    @GetMapping("/manager/{name}")
    public ManagerUser getManagerUserByName(@PathVariable String name) {
        return managerTasksService.getByName(name);
    }

    @GetMapping("/manager")
    public List<ManagerUser> getAllManagers(){
        return managerTasksService.getAll();
    }

    // update
    @PutMapping("/manager/{id}")
    public String updateManagerUser(@PathVariable String id, @RequestParam String name, @RequestParam Map<String, SubordinateUser> subordinateList, @RequestParam Map<String, Task> uncheckedTasksList) {
        ManagerUser mu = managerTasksService.updateManagerUser(id, name, subordinateList, uncheckedTasksList);
        return mu.toString();
    }

    // delete
    @DeleteMapping("/manager/{id}")
    public String delete(@PathVariable String id) {
        managerTasksService.deleteById(id);
        return "Deleted manager id: " + id;
    }

    @DeleteMapping("/manager")
    public String deleteAllManagers(){
        managerTasksService.deleteAll();
        return "Deleted all managers";
    }
}
