package com.moderngas.pojo.admin;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class DeliveryVehicleDto {

    @NonNull
    private List<String> numbers;

    @NonNull
    private Long userId;

}
