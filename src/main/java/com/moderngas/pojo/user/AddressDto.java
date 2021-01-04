package com.moderngas.pojo.user;

import lombok.Data;

@Data
public class AddressDto {

    private Long id;

    private int pincode;

    private String landmark;

    private String address1;

    private String address2;

    private String city;

    private String state;
}
