package com.BudgetTracking.UserService.Controller;

import com.BudgetTracking.UserService.Model.User;
import com.BudgetTracking.UserService.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@ResponseBody
public class AdminController {

    @Autowired
    private AdminService adminService;

   // @PreAuthorize("hasAuthority('ROLE_Admin')") // Restrict access to admins only
    @GetMapping("/getUsers/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = adminService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

  //  @PreAuthorize("hasRole('ROLE_Admin')") // Restrict access to admins only
    @DeleteMapping("/deleteUsers/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
