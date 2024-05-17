package com.BudgetTracking.UserService.Config;

import com.BudgetTracking.UserService.Service.UserDetailsServiceImp;
import com.BudgetTracking.UserService.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserDetailsServiceImp userDetailsServiceImp;
    private final PasswordEncoder passwordEncoder;
    private final CustomLogoutHandler logoutHandler;

    public SecurityConfig(@Lazy UserDetailsServiceImp userDetailsServiceImp, @Lazy PasswordEncoder passwordEncoder, CustomLogoutHandler logoutHandler, @Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsServiceImp = userDetailsServiceImp;
        this.passwordEncoder = passwordEncoder;
        this.logoutHandler = logoutHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public void setUserDetailsServiceImp(UserDetailsServiceImp userDetailsServiceImp) {
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @Autowired
    public void setJwtAuthenticationFilter(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

   return http
           .csrf(AbstractHttpConfigurer::disable)
           .authorizeHttpRequests(
                   req -> req.requestMatchers("/login/**","/register/**","/admin/**")
                           .permitAll()
                           .anyRequest()
                           .authenticated()
           ).userDetailsService(userDetailsServiceImp)
           .sessionManagement(session ->session
                   .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
           .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
           .logout(l-> l.logoutUrl("/logout")
                   .addLogoutHandler(logoutHandler)
                   .logoutSuccessHandler(
                           (request, response, authentication) -> SecurityContextHolder.clearContext()
                   ))
           .build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }





}
