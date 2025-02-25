package com.main.article.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/swagger-ui/**").permitAll()
                            .requestMatchers("/swagger-ui.html").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/users/authenticate").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN")

                            .requestMatchers(HttpMethod.POST, "/api/articles/create").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/articles/{id}").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/api/articles/{id}").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET, "/api/articles/getAll&Pagination").authenticated()
                            .requestMatchers(HttpMethod.GET, "/api/articles/getAllLists").authenticated()

                            .requestMatchers(HttpMethod.GET, "/api/statistics/publishedCount7").hasRole("ADMIN")

                            .anyRequest().authenticated();
                })
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
