package com.moderngas.enums;

import org.springframework.util.StringUtils;

public enum UserPrivilege {

    USER_PRIVILEGE_ORDER("Order"),
    USER_PRIVILEGE_USER("User"),
    USER_PRIVILEGE_EMPLOYEE("Employee"),
    USER_PRIVILEGE_INVENTORY("Inventory"),
    USER_PRIVILEGE_RESOURCE_CENTRE("Resource Centre"),
    USER_PRIVILEGE_ACCOUNTS("Account");

    private final String privilege;

    public String getPrivilege() {
        return privilege;
    }

    UserPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public static UserPrivilege getByPrivilege(String privilege) {
        if (!StringUtils.isEmpty(privilege)) {
            for (UserPrivilege userPrivilege : UserPrivilege.values()) {
                if (userPrivilege.getPrivilege().equals(privilege)) {
                    return userPrivilege;
                }
            }
        }
        return null;
    }
}
