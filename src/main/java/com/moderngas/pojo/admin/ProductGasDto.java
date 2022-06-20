package com.moderngas.pojo.admin;

import com.moderngas.pojo.CylinderTypeIdPriceDto;
import lombok.Data;

import java.util.List;

@Data
public class ProductGasDto {

    private Long id;

    private String description;

    private List<CylinderTypeIdPriceDto> typeIdPriceDtoList;
}
