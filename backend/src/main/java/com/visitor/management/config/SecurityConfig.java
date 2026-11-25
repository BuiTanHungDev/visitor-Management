package com.visitor.management.config;

import com.visitor.management.security.filter.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Để frontend gọi backend
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder(12);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {

                http
                                // API sử dụng JWT nên không cần CSRF.
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                // Không sử dụng HTTP Session.
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Tắt các cơ chế xác thực mặc định.
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .logout(AbstractHttpConfigurer::disable)

                                // Quy định quyền truy cập.
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                HttpMethod.POST,
                                                                "/api/v1/auth/register",
                                                                "/api/v1/auth/login",
                                                                "/api/v1/auth/otp/request",
                                                                "/api/v1/auth/otp/verify")
                                                .permitAll()
                                                .anyRequest().authenticated())

                                // Xử lý lỗi xác thực và phân quyền.
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(
                                                                (request, response, exception) -> response
                                                                                .sendError(401, "Unauthorized"))
                                                .accessDeniedHandler(
                                                                (request, response, exception) -> response
                                                                                .sendError(403, "Forbidden")))

                                // Đưa JWT Filter vào Spring Security Filter Chain.
                                .addFilterBefore(
                                                jwtAuthenticationFilter,
                                                UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        /**
         * Ngăn Spring Boot đăng ký JWT Filter thêm một lần
         * ở Servlet Container bên ngoài Spring Security.
         */
        @Bean
        public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(JwtAuthenticationFilter filter) {

                FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();

                registration.setFilter(filter);
                registration.setEnabled(false);

                return registration;
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(List.of(
                                "http://localhost:5173"));

                configuration.setAllowedMethods(List.of(
                                "GET",
                                "POST",
                                "PUT",
                                "PATCH",
                                "DELETE",
                                "OPTIONS"));

                configuration.setAllowedHeaders(List.of(
                                "Authorization",
                                "Content-Type",
                                "Accept"));

                configuration.setAllowCredentials(false);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

                source.registerCorsConfiguration("/**", configuration);

                return source;
        }
}