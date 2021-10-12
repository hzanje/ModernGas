package com.moderngas.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCentreDto {

    private Long id;

    private String name;

    private String alias;
}
