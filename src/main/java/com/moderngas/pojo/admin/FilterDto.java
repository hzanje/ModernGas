package com.moderngas.pojo.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterDto {

    private List<String> cylinderType;

    private List<String> quantityOrdering;
}
