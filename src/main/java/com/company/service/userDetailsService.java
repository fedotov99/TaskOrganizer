package com.company.service;

import com.company.model.ManagerUser;
import com.company.model.SubordinateUser;
import com.company.model.User;
import com.company.repository.ManagerUserRepository;
import com.company.repository.SubordinateUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

public class userDetailsService implements UserDetailsService {
    @Autowired
    protected SubordinateUserRepository subordinateUserRepository;
    @Autowired
    private ManagerUserRepository managerUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) // actually email instead of username
            throws UsernameNotFoundException {

        User user = subordinateUserRepository.findByEmail(email);
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(SubordinateUser.getRole())));
        }

        user = managerUserRepository.findByEmail(email);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(ManagerUser.getRole())));
        }
        else {
            throw new UsernameNotFoundException(
                    "User '" + email + "' not found");
        }
    }
}
