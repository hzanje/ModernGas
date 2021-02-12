package com.moderngas.pojo.admin;

import lombok.Data;
import lombok.NonNull;

@Data
public class DeliveryVehicleDto {

    private String name;

    private String color;

    @NonNull
    private String number;

    @NonNull
    private Long userId;

}
