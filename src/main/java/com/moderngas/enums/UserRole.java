package com.moderngas.enums;

import org.springframework.util.StringUtils;

public enum UserRole {

    USER_ROLE_USER("ROLE_USER"),
    USER_ROLE_ADMIN("ROLE_ADMIN"),
    USER_ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN");

    private final String role;

    public String getRole() {
        return role;
    }

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole getByRole(String role) {
        if (!StringUtils.isEmpty(role)) {
            for (UserRole userRole : UserRole.values()) {
                if (userRole.getRole().equals("ROLE_" + role)) {
                    return userRole;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String role) {
        if (!StringUtils.isEmpty(role)) {
            for (UserRole userRole : UserRole.values()) {
                if (userRole.getRole().equals("ROLE_" + role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
