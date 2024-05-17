package com.BudgetTracking.UserService.Service;

import com.BudgetTracking.UserService.Model.User;
import com.BudgetTracking.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

  //  @PreAuthorize("hasRole('Admin')") // Ensure only admins can call this method
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found"));
    }

  //  @PreAuthorize("hasRole('Admin')") // Ensure only admins can call this method
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
