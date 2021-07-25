package com.moderngas.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
}
