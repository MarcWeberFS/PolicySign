package ch.zhaw.policysign.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/upload").permitAll()
                .requestMatchers("/api/signature").permitAll()
                .requestMatchers("/api/signature/**").permitAll()
                .requestMatchers("/signature/**").permitAll()
                .requestMatchers("/signature").permitAll()
                .requestMatchers("/api/upload/all").permitAll()
                .requestMatchers("/api/users").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/api/documents/**").permitAll()
                .requestMatchers("/api/upload/user/**").permitAll()
                .requestMatchers("/api/upload/download/**").permitAll()






                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())
            .cors();

        return http.build();
    }
}
