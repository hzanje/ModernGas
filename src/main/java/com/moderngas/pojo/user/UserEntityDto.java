package com.moderngas.pojo.user;


import lombok.Data;

@Data
public class UserEntityDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String password;

    private String companyName;

    private String contactPerson;

    private String role;


}
