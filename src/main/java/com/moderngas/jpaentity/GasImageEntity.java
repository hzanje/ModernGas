package com.moderngas.jpaentity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "gasImageEntityList")
    Set<GasMaster> gasMasterList;


}
