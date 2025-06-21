package com.example.OnlineService.config;

import com.example.OnlineService.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)// можно включить позже при необходимости
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/authorisation/**", "/registration/**", "/css/**", "/js/**").permitAll() // открытые маршруты
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/authorisation")
                        .loginProcessingUrl("/authorisation") // POST-запрос для логина
                        .defaultSuccessUrl("/start-page", true)
                        .failureUrl("/authorisation?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/authorisation?logout=true")
                        .permitAll()
                );

        return http.build();
    }
}
