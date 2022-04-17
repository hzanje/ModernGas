package com.moderngas.jpaentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE delivery_vehicle a SET active_flag = 0 WHERE id = ?")
@Table(name = "delivery_vehicle")
public class DeliveryVehicleEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "user_id")
    private Long userId;
}
