package com.prangyajeet.mvep.config;

import com.prangyajeet.mvep.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        System.out.println("========== CUSTOM SECURITY CONFIG LOADED ==========");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/cashfree/webhook",
                                "/error"
                        ).permitAll()
                        
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/products/**"
                        ).permitAll()

                        // =======================
                        // ADMIN APIs
                        // =======================
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // =======================
                        // VENDOR APIs
                        // =======================
                        .requestMatchers("/api/vendor/**")
                        .hasRole("VENDOR")

                        // =======================
                        // CUSTOMER APIs
                        // =======================
                        .requestMatchers("/api/customer/**")
                        .hasRole("CUSTOMER")

                        // =======================
                        // NOTIFICATION APIs
                        // =======================

                        // Logged-in user's notifications
                        .requestMatchers(HttpMethod.GET,
                                "/api/notifications/me")
                        .hasAnyRole("CUSTOMER", "VENDOR", "ADMIN")

                        // View notification by ID
                        .requestMatchers(HttpMethod.GET,
                                "/api/notifications/*")
                        .hasAnyRole("CUSTOMER", "VENDOR", "ADMIN")

                        // Mark notification as read
                        .requestMatchers(HttpMethod.PUT,
                                "/api/notifications/*/read")
                        .hasAnyRole("CUSTOMER", "VENDOR", "ADMIN")

                        // Delete notification
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/notifications/*")
                        .hasAnyRole("CUSTOMER", "VENDOR", "ADMIN")

                        // Create notification
                        .requestMatchers(HttpMethod.POST,
                                "/api/notifications")
                        .authenticated()

                        // Any other request
                        .anyRequest()
                        .authenticated()
                )

                .exceptionHandling(exception ->

                        exception

                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED.value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized");
                                        }
                                )

                                .accessDeniedHandler(
                                        (request, response, accessDeniedException) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN.value()
                                            );
                                            response.getWriter()
                                                    .write("Access Denied");
                                        }
                                )
                );

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}