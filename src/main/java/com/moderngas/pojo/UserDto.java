package com.moderngas.pojo;

import com.moderngas.pojo.employee.PrivilegeDto;
import lombok.Data;
import lombok.NonNull;

import java.util.Set;

@Data
public class UserDto {

    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long mobileNumber;

    @NonNull
    private String email;

    private String companyName;

    private String password;

    private Set<PrivilegeDto> privilegeDtoList;
}
