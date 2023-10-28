package com.eric.appointment.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(crsf -> crsf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .requestMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
                .requestMatchers("/appointments/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
                .requestMatchers("/appointments/new/**").hasRole("CUSTOMER")
                .requestMatchers("/providers/availability/**").hasRole("PROVIDER")
                .requestMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
                .requestMatchers("/providers/new").hasAnyRole("ADMIN")
                .requestMatchers("/providers/all").hasRole("ADMIN")
                .requestMatchers("/customers/all").hasRole("ADMIN")
                .requestMatchers("works/**").hasAnyRole("ADMIN")
                .requestMatchers("/login").permitAll()
                .requestMatchers("/css/**", "/customers/new/**", "/webjars/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/perform_logout")
                .logoutSuccessUrl("/login")
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedPage("/access-denied")
            );    
                
        return http.build();
    }
}