package com.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
//public class SecurityConfig  extends WebSecurityConfigurerAdapter {
public class SecurityConfig {
    @Qualifier("custom")
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder() {
        return new StandardPasswordEncoder("53cr3t");
    }

    @Bean
    public SecurityWebFilterChain securitygWebFilterChain(
            ServerHttpSecurity http) {
        return http.authorizeExchange()
                .anyExchange()
                .permitAll()
//                .authenticated()
                .and().build();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(encoder());
//    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors()
//                .and()
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("**")
//                .permitAll();
//                .antMatchers("/login", "/create/manager", "/create/subordinate", "/util/manager")
//                .permitAll()
//                .antMatchers("/manager", "/manager/**")
//                .access("hasRole('ROLE_MANAGER')")
//                .antMatchers("/subordinate", "/subordinate/**")
//                .access("hasRole('ROLE_SUBORDINATE')")
//                .antMatchers("/task", "/task/**")
//                .denyAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }
}
