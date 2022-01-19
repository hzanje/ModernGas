package com.moderngas.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchDto {

    private Long id;

    private String name;

    private String companyName;

    private Long mobileNumber;

    public UserSearchDto(Long id, String name, String companyName) {
        this.id = id;
        this.name = name;
        this.companyName = ObjectUtils.isEmpty(companyName) ? "NA" : companyName;
    }
}
