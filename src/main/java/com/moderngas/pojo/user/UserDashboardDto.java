package com.moderngas.pojo.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDashboardDto {

    List<String> imageURlList;
    private Long id;
    private String name;
}
