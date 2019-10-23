package com.company.controller;

import com.company.model.ManagerUser;
import com.company.model.PositionType;
import com.company.model.SubordinateUser;
import com.company.service.SubordinateTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubordinateController {
    @Autowired
    private SubordinateTasksService subordinateTasksService;

    // create
    @RequestMapping("/create/subordinate")
    public String createSubordinateUser(@RequestParam String name, @RequestParam ManagerUser manager, @RequestParam int score, @RequestParam PositionType position) {
        SubordinateUser su = subordinateTasksService.createSubordinateUser(name, manager, score, position);
        return su.toString();
    }

    // retrieve
    @RequestMapping("/get/subordinate")
    public SubordinateUser getSubordinateUserByID(@RequestParam String id) {
        return subordinateTasksService.getByUserID(id);
    }

    @RequestMapping("/get/subordinate")
    public SubordinateUser getSubordinateUserByName(@RequestParam String name) {
        return subordinateTasksService.getByName(name);
    }

    @RequestMapping("/getAll/subordinates")
    public List<SubordinateUser> getAllSubordinates(){
        return subordinateTasksService.getAll();
    }

    // update
    @RequestMapping("/update/subordinate")
    public String updateSubordinateUser(@RequestParam String id, @RequestParam String name, @RequestParam ManagerUser manager, @RequestParam int score, @RequestParam PositionType position) {
        SubordinateUser su = subordinateTasksService.updateSubordinateUser(id, name, manager, score, position);
        return su.toString();
    }

    // delete
    @RequestMapping("/delete/subordinate")
    public String delete(@RequestParam String id) {
        subordinateTasksService.deleteById(id);
        return "Deleted subordinate id: " + id;
    }

    @RequestMapping("/deleteAll/subordinates")
    public String deleteAllSubordinates(){
        subordinateTasksService.deleteAll();
        return "Deleted all subordinates";
    }
}
