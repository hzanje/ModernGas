package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(mappedBy = "gasImageEntityList")
    Set<GasMaster> gasMasterList;


}
