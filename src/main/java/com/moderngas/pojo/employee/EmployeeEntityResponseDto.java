package com.moderngas.pojo.employee;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeEntityResponseDto {

    private Long id;

    private String name;

    private String email;

    private Long mobileNumber;

    private String company;

    private Set<PrivilegeDto> privilegeDtoSet;
}
