package com.moderngas.pojo.superadmin;

import com.moderngas.jpaentity.ContactPersonEntity;
import com.moderngas.pojo.UserDto;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class AdminEntityDto {

    private UserDto userDto;

    private List<String> roles;

    private List<GasNameCylinderTypeDto> gasNameCylinderTypes;

    private Set<ContactPersonEntity> contactPersonSet;

}
