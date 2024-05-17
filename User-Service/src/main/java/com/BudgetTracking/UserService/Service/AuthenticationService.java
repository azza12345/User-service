package com.BudgetTracking.UserService.Service;

import com.BudgetTracking.UserService.Model.AuthenticationResponse;
import com.BudgetTracking.UserService.Model.Token;
import com.BudgetTracking.UserService.Model.User;
import com.BudgetTracking.UserService.repository.TokenRepository;
import com.BudgetTracking.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ComponentScan
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;



    public AuthenticationService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, UserRepository userRepository, TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    public AuthenticationResponse register(User request){
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user=repository.save(user);
        String jwt=jwtService.generateToken(user);

        saveUserToken(jwt, user);


        return new AuthenticationResponse(jwt);
    }



    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user=repository.findByEmail(request.getEmail()).orElseThrow();
        String token =jwtService.generateToken(user);

        revokeAllTokenListByUser(user);


        saveUserToken(token,user);

        return new AuthenticationResponse(token);

    }

    private void revokeAllTokenListByUser(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if(!validTokenListByUser.isEmpty()) {
            validTokenListByUser.forEach(t -> {
                t.setLoggedOut(true);
            });
        }
        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(  String jwt,User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }


    @Service
    public class AdminService {

        @Autowired
        private UserRepository userRepository;

        @PreAuthorize("hasRole('Admin')") // Ensure only admins can call this method
        public User getUserById(Long userId) {
            return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("not found"));
        }

        @PreAuthorize("hasRole('Admin')") // Ensure only admins can call this method
        public void deleteUser(Long userId) {
            userRepository.deleteById(userId);
        }
    }




//    public boolean deleteUserById(Long userId) {
//        // Check if the user exists
//        if (!repository.existsById(userId)) {
//            return false; // User not found
//        }
//
//        // Logic to check if the authenticated user is an admin
////        if (!isAdminAuthenticated()) {
////            throw new IllegalStateException("Only admins can delete users");
////        }
//
//        // Perform deletion if the authenticated user is an admin
//        repository.deleteById(userId);
//        return true; // User deleted successfully
//    }

//    public boolean deleteUserById(Long userId) {
//        if (userRepository.existsById(userId)) {
//            userRepository.deleteById(userId);
//            return true; // User deleted successfully
//        }
//        return false; // User not found
//    }




}

