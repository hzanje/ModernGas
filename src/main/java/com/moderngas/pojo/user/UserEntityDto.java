package com.moderngas.pojo.user;


import lombok.Data;

import java.util.List;

@Data
public class UserEntityDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String password;

    private String companyName;

    private String contactPerson;

    private List<String> role;

    private Long employerId;

    private boolean isForgetPassword;

    private boolean isOnboarding;

    //ResourceCentre

    // Employer Dto
        // id
        // name
        // company Name
        // address


}
