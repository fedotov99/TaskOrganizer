package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public abstract class UserTasksService {
    @Autowired
    protected SubordinateUserRepository subordinateUserRepository;
    @Autowired
    private ManagerUserRepository managerUserRepository;

    public abstract void updateTaskInLocalUserTaskList (User user, String taskID);

    public abstract void completeTask (User user, String taskID, String report);

    public abstract void deleteTaskFromLocalUserTaskList(User user, String taskID);


}
