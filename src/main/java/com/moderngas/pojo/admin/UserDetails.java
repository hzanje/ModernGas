package com.moderngas.pojo.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDetails {

    private Long id;

    private boolean active;

    private String name;

    private Long mobileNumber;

    private String email;

    private String company_name;

    private List<String> assignedCylinder;

    private int totalOrders;

    public UserDetails(Long id, boolean active, String name, Long mobileNumber, String email, String company_name) {
        this.id = id;
        this.active = active;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.company_name = company_name;
    }
}
