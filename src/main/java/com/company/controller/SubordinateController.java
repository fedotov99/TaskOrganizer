package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.PositionType;
import com.company.model.SubordinateUser;
import com.company.service.SubordinateTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubordinateController {
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    // create
    @PostMapping("/subordinate")
    public String createSubordinateUser(@RequestParam String name, @RequestParam ManagerUser manager, @RequestParam int score, @RequestParam PositionType position) {
        SubordinateUser su = subordinateTasksService.createSubordinateUser(name, manager, score, position);
        return su.toString();
    }

    // retrieve
    @GetMapping("/subordinate/{id}")
    public SubordinateUser getSubordinateUserByID(@PathVariable String id) {
        return subordinateTasksService.getByUserID(id);
    }

    @GetMapping("/subordinate/{name}")
    public SubordinateUser getSubordinateUserByName(@PathVariable String name) {
        return subordinateTasksService.getByName(name);
    }

    @GetMapping("/subordinate")
    public List<SubordinateUser> getAllSubordinates(){
        return subordinateTasksService.getAll();
    }

    // update
    @PutMapping("/subordinate/{id}")
    public String updateSubordinateUser(@PathVariable String id, @RequestParam String name, @RequestParam ManagerUser manager, @RequestParam int score, @RequestParam PositionType position) {
        SubordinateUser su = subordinateTasksService.updateSubordinateUser(id, name, manager, score, position);
        return su.toString();
    }

    // delete
    @DeleteMapping("/subordinate/{id}")
    public String delete(@PathVariable String id) {
        subordinateTasksService.deleteById(id);
        return "Deleted subordinate id: " + id;
    }

    @DeleteMapping("/subordinate")
    public String deleteAllSubordinates(){
        subordinateTasksService.deleteAll();
        return "Deleted all subordinates";
    }
}
