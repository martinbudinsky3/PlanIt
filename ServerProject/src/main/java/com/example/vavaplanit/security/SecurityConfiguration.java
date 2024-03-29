package com.example.vavaplanit.security;

import com.example.vavaplanit.security.filters.CustomAuthenticationFilter;
import com.example.vavaplanit.security.filters.CustomUsernamePasswordAuthenticationFilter;
import com.example.vavaplanit.security.handlers.JwtAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/login", "/auth/register").permitAll();
        http.authorizeRequests().antMatchers("/events/{eventId}/**")
                .access("@eventPolicy.check(authentication, #eventId)");
        http.authorizeRequests().antMatchers("/repetitions/{repetitionId}/**")
                .access("@repetitionPolicy.check(authentication, #repetitionId)");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManagerBean(), authenticationFailureHandler()));
        http.addFilterBefore(new CustomAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }
}
