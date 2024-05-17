package com.BudgetTracking.UserService.Service;

import com.BudgetTracking.UserService.repository.UserRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@ComponentScan(basePackages = {"com.BudgetTracking.UserService.Service", "com.BudgetTracking.UserService.repository"})
@ComponentScan(basePackages = "com.BudgetTracking.UserService.Controller")
@Service
public class UserDetailsServiceImp implements UserDetailsService {
    private final UserRepository repository;

    public UserDetailsServiceImp(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return repository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
