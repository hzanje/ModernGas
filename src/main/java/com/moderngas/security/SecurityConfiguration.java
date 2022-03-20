package com.moderngas.security;

import com.moderngas.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    MethodSecurityConfig methodSecurityConfig;


    public SecurityConfiguration(UserDetailsServiceImpl userDetailsServiceImpl,
                                 UserRepo userRepo) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.userRepo = userRepo;
    }



    private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setRoleHierarchy(methodSecurityConfig.roleHierarchy());
        return defaultWebSecurityExpressionHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /* Remove csrf and state in session (Not needed in JWT) */
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                /* Add Jwt filter : Authentication and Authorization */
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), this.userRepo))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepo))
                .authorizeRequests()
                .expressionHandler(webExpressionHandler())
                //.accessDecisionManager(defaultAccessDecisionManager(roleHierarchy()))
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/base/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsServiceImpl);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
