package com.moderngas.pojo.superadmin;

import lombok.Data;

import java.util.List;

@Data
public class AdminEntityDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String password;

    private String companyName;

    private String contactPerson;

    private List<String> roles;

    private List<GasNameCylinderTypeDto> gasNameCylinderTypes;

}
