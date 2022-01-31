package com.moderngas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(
        jsr250Enabled = true,
        securedEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();

    /**
     * Adding Role Hierarchy for the Data and Security
     */
    public void SecurityConfig() {
        roleHierarchy.setHierarchy("ROLE_SUPER_ADMIN > ROLE_ADMIN and " +
                "ROLE_ADMIN > ROLE_EMPLOYEE and " +
                "ROLE_ADMIN > ROLE_USER" +
                "ROLE_EMPLOYEE > Order");
    }

    @Bean
    public AffirmativeBased getAccessDecisionManager() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(expressionHandler);

        List<AccessDecisionVoter<? extends Object>> voters = new ArrayList<>();

        voters.add(webExpressionVoter);
        return new AffirmativeBased(voters);
    }
}
