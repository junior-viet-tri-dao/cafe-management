package com.vn.security;

import com.vn.auth.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] ENABLE_PERMIT = {
            "/login", "/css/**", "/js/**", "/img/**", "/uploads/**"
    };

    private static final String[] ADMIN_PERMIT = {
            "/admin/**"
    };

    private static final String[] USER_PERMIT = {
            "/"
    };

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    public void config(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable();
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(ENABLE_PERMIT).permitAll()
                .requestMatchers(ADMIN_PERMIT).hasAnyAuthority("Admin")
                .requestMatchers(USER_PERMIT).hasAnyAuthority("Customer", "Employee", "Admin")
                .anyRequest().authenticated()
        );

        http.formLogin(
                form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
        ).logout(logout -> logout.logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailService;
    }
}
