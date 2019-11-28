package com.company.service;

import com.company.model.*;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

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
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_SUBORDINATE")));
        }

        user = managerUserRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER")));
        }
        else {
            throw new UsernameNotFoundException(
                    "User '" + email + "' not found");
        }
    }
}
