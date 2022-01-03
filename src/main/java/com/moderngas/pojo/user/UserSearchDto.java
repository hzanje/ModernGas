package com.moderngas.pojo.user;

import lombok.Data;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Data
public class UserSearchDto {

    private Long id;

    private String name;

    private String companyName;

    public UserSearchDto(Long id, String name, String companyName) {
        this.id = id;
        this.name = name;
        this.companyName = ObjectUtils.isEmpty(companyName) ? "NA" : companyName;
    }
}
