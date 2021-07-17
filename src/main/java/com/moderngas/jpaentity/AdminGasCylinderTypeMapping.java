package com.moderngas.jpaentity;

import com.moderngas.enums.CylinderType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admin_gas_cylinder_type_mapping")
public class AdminGasCylinderTypeMapping {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    protected Long id;

    @Column(name = "cylinder_type")
    @Enumerated(EnumType.ORDINAL)
    private CylinderType cylinderType;
}
