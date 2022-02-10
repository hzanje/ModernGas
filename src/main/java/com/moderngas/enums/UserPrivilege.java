package com.moderngas.enums;

import org.springframework.util.ObjectUtils;

public enum UserPrivilege {

    USER_PRIVILEGE_ORDER("ROLE_PRIVILEGE_ORDER", "Order"),
    USER_PRIVILEGE_USER("ROLE_PRIVILEGE_USER", "User"),
    USER_PRIVILEGE_EMPLOYEE("ROLE_PRIVILEGE_EMPLOYEE", "Employee"),
    USER_PRIVILEGE_INVENTORY("ROLE_PRIVILEGE_INVENTORY", "Inventory"),
    USER_PRIVILEGE_RESOURCE_CENTRE("ROLE_PRIVILEGE_RESOURCE_CENTER", "Resource Center"),
    USER_PRIVILEGE_VEHICLE("USER_PRIVILEGE_VEHICLE", "Vehicle"),
    USER_PRIVILEGE_ACCOUNTS("ROLE_PRIVILEGE_ACCOUNT", "Account");

    private final String privilege;

    private final String name;

    UserPrivilege(String privilege, String name) {
        this.privilege = privilege;
        this.name = name;
    }

    public static UserPrivilege getByPrivilege(String privilege) {
        if (!ObjectUtils.isEmpty(privilege)) {
            for (UserPrivilege userPrivilege : UserPrivilege.values()) {
                if (userPrivilege.getPrivilege().equals(privilege)) {
                    return userPrivilege;
                }
            }
        }
        return null;
    }

    public String getPrivilege() {
        return privilege;
    }

    public String getName() {
        return name;
    }

    public static UserPrivilege getUserPrivilegeByName(String name) {
        if (!ObjectUtils.isEmpty(name)) {
            for (UserPrivilege userPrivilege : UserPrivilege.values()) {
                if (userPrivilege.getName().equals(name)) {
                    return userPrivilege;
                }
            }
        }
        return null;
    }

}
