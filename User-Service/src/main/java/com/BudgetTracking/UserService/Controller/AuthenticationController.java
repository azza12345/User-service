package com.BudgetTracking.UserService.Controller;

import com.BudgetTracking.UserService.Model.AuthenticationResponse;
import com.BudgetTracking.UserService.Model.User;
import com.BudgetTracking.UserService.Service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid
            @RequestBody User request
            ){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid
            @RequestBody User request
    ){
        return  ResponseEntity.ok(authService.authenticate(request));
    }






//    @DeleteMapping("/admin/users/{userId}")
//    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
//        boolean deleted = authService.deleteUserById(userId);
//        if (deleted) {
//            return ResponseEntity.ok("User deleted successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }

//    @DeleteMapping("/admin/users/{userId}")
//    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
//        // Check if the authenticated user is an admin
////        if (!isAdminAuthenticated()) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
////        }
//
//        // Delete user by ID
//        boolean deleted = authService.deleteUserById(userId);
//        if (deleted) {
//            return ResponseEntity.ok("User deleted successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }



}
