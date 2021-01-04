package com.moderngas.jpaentity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "gas_master")
public class GasMaster extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "is_avaliable", columnDefinition = "tinyint(1) DEFAULT 1")
    private boolean isAvaliable;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private CategoryMaster categoryMaster;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "gas_image_mapping", joinColumns = {@JoinColumn(name = "gas_id")}
    , inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Set<GasImageEntity> gasImageEntityList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "gas_cylinder_mapping", joinColumns = {@JoinColumn(name = "gas_id")}
            , inverseJoinColumns = {@JoinColumn(name = "cylinder_id")})
    private Set<CylinderTypeMaster> cylinderTypeMasterList;

}
