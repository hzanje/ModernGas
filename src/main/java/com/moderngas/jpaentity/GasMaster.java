package com.moderngas.jpaentity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "gas_master")
public class GasMaster extends GenericEntity {

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "refil_range")
    private String refilRange;

    @Column(name = "is_avaliable", columnDefinition = "tinyint(1) DEFAULT 1")
    private boolean isAvaliable;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private CategoryMaster categoryMaster;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "gas_id", referencedColumnName = "id")
    private List<GasImageEntity> gasImageEntityList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cylinder_type", referencedColumnName = "id")
    private Set<CylinderTypeMaster> cylinderTypeMasterList;

}
