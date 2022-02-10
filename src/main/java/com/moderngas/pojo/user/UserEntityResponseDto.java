package com.moderngas.pojo.user;


import com.moderngas.pojo.admin.ResourceCentreDto;
import lombok.Data;

import java.util.List;

@Data
public class UserEntityResponseDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String password;

    private String companyName;

    private List<String> roles;

    private boolean isForgetPassword;

    private boolean isOnboard;

    private List<ResourceCentreDto> resourceCentreDtoList;

    private AdminDto adminDto;

}
