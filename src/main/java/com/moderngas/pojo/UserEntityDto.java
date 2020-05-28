package com.moderngas.pojo;


import lombok.Data;

@Data
public class UserEntityDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String address;

    private String password;

    private String companyName;

    private String contactPerson;

    private Long companyContact;

}
