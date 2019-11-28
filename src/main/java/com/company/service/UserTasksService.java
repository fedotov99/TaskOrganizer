package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Qualifier("userDetails")
@Service
public abstract class UserTasksService implements UserDetailsService {
    @Autowired
    protected SubordinateUserRepository subordinateUserRepository;
    @Autowired
    private ManagerUserRepository managerUserRepository;

    public abstract void updateTaskInLocalUserTaskList (User user, String taskID);

    public abstract void completeTask (User user, String taskID, String report);

    public abstract void deleteTaskFromLocalUserTaskList(User user, String taskID);

    @Override
    public UserDetails loadUserByUsername(String email) // actually email instead of username
            throws UsernameNotFoundException {
        User user = subordinateUserRepository.findByEmail(email);
        if (user != null) {
            return user;
        }
        else {
            user = managerUserRepository.findByEmail(email);
        }
        throw new UsernameNotFoundException(
                "User '" + email + "' not found");
    }
}
