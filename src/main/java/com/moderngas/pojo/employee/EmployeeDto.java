package com.moderngas.pojo.employee;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private Set<PrivilegeDto> privilegeDtoList;
}
