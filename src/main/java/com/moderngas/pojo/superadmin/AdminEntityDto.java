package com.moderngas.pojo.superadmin;

import com.moderngas.jpaentity.AddressEntity;
import com.moderngas.jpaentity.ContactPersonEntity;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class AdminEntityDto {

    private Long id;

    private String name;

    private Long mobileNumber;

    private String email;

    private String password;

    private String companyName;

    private List<String> roles;

    private List<GasNameCylinderTypeDto> gasNameCylinderTypes;

    private Set<ContactPersonEntity> contactPersonSet;

}
