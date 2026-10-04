package com.nequard.auth;
import org.springframework.context.annotation.*;import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.web.SecurityFilterChain;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration public class AuthConfig {
 @Bean BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{http.csrf(c->c.disable()).authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/**","/api/v1/health","/actuator/health").permitAll().anyRequest().authenticated()).httpBasic(b->{});return http.build();}
}
