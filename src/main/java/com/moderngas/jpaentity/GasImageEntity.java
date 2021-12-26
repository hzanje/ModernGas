package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "gas_image")
public class GasImageEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "imageUrl")
    private String imageUrl;
}
