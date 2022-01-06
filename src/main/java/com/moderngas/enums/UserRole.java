package com.moderngas.enums;

import org.springframework.util.ObjectUtils;

public enum UserRole {

    USER_ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    USER_ROLE_ADMIN("ROLE_ADMIN"),
    USER_ROLE_USER("ROLE_USER"),
    USER_ROLE_EMPLOYEE("ROLE_EMPLOYEE");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

    public static UserRole getByRole(String role) {
        if (!ObjectUtils.isEmpty(role)) {
            for (UserRole userRole : UserRole.values()) {
                if (userRole.getRole().equals("ROLE_" + role)) {
                    return userRole;
                }
            }
        }
        return null;
    }

    public static boolean isExist(String role) {
        if (!ObjectUtils.isEmpty(role)) {
            for (UserRole userRole : UserRole.values()) {
                if (userRole.getRole().equals("ROLE_" + role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getRole() {
        return role;
    }
}
