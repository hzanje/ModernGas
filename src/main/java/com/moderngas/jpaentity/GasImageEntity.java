package com.moderngas.jpaentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
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
