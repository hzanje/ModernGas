package com.moderngas.jpaentity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cylinder_type_master")
public class CylinderTypeMaster {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "cylinderTypeMasterList")
    Set<GasMaster> gasMasterList;
}
