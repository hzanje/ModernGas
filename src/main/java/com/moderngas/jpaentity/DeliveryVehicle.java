package com.moderngas.jpaentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_vehicle")
public class DeliveryVehicle extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "user_id")
    private Long userId;
}
